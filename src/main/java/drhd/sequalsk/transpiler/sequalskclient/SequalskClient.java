package drhd.sequalsk.transpiler.sequalskclient;


import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedback;
import drhd.sequalsk.transpiler.sequalskclient.feedback.TranspilerFeedbackExtractor;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerContext;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerResult;
import drhd.sequalsk.transpiler.sequalskclient.result.TranspilerTwoWayResult;
import drhd.sequalsk.transpiler.sequalskclient.transpiler.OnlineTranspiler;
import drhd.sequalsk.transpiler.sequalskclient.transpiler.SequalskTranspiler;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerResponseSplitter;
import drhd.sequalsk.transpiler.sequalskclient.utils.TwoWayResultConverter;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerLanguage;
import drhd.sequalsk.transpiler.sequalskclient.utils.enums.TranspilerRequestMode;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Processes {@link TranspilerRequest}s locally or webbased. Generates a {@link TranspilerResult} from the input request.
 */
public class SequalskClient {

    private final TranspilerConfiguration config;

    public SequalskClient(TranspilerConfiguration config) {
        this.config = config;
    }

    public TranspilerResult transpile(TranspilerRequest request) throws TranspilerException {
        try {
            if (request.isTwoWayRequest()) {
                return executeTwoWayRequestRequest(request);
            }
            return executeOneWayRequest(request);

        } catch (TranspilerException e) {
            throw e;
        } catch (Exception exception) {
            throw new TranspilerException(config, request);
        }
    }

    public CompletableFuture<TranspilerResult> transpileAsync(TranspilerRequest transpilerRequest) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return transpile(transpilerRequest);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }
        );
    }

    private TranspilerResult executeTwoWayRequestRequest(TranspilerRequest request) throws TranspilerException {
        TranspilerResult interimResult = executeOneWayRequest(request);

        TranspilerContext swiftRequestContext = TwoWayResultConverter.convertInterimResult(interimResult);
        TranspilerRequest swiftRequest = new TranspilerRequest(
                swiftRequestContext,
                TranspilerLanguage.SWIFT,
                TranspilerRequestMode.ONE_WAY
        );

        TranspilerResult finalResult = executeOneWayRequest(swiftRequest);
        finalResult = TwoWayResultConverter.convertFinalResult(finalResult, request);

        return new TranspilerTwoWayResult(interimResult, finalResult);
    }

    private TranspilerResult executeOneWayRequest(TranspilerRequest request) throws TranspilerException {
        SequalskTranspiler transpiler = new OnlineTranspiler();

        /* validate transpiler configuration  */
        if(!transpiler.validateConfiguration(config)) {
            throw new TranspilerException("invalid transpiler configuration", config, request);
        }

        /* transpile code */
        String transpiledCode = transpiler.transpile(request, config);
        final TranspilerFeedback feedback = new TranspilerFeedback();
        if (request.getInputLanguage() == TranspilerLanguage.KOTLIN) {
            transpiledCode = TranspilerFeedbackExtractor.extractFeedback(transpiledCode, feedback);
        }

        TranspilerResponseSplitter splitter = new TranspilerResponseSplitter(request.getContext());
        splitter.split(transpiledCode);

        return new TranspilerResult(
                request,
                splitter.getMainFile(),
                splitter.getAdditionalFiles(),
                transpiledCode,
                feedback
        );
    }
}