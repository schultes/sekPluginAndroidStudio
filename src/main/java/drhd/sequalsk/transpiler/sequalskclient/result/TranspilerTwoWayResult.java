package drhd.sequalsk.transpiler.sequalskclient.result;

/**
 * The result of a two-way-request (Kotlin->Swift->Kotlin).
 * All fields inherited from {@link TranspilerResult} contain the values of the final result / the result
 * of the second request (Swift->Kotlin).
 * The results of the first request (Kotlin->Swift) are stored as "interimResult".
 */
public class TranspilerTwoWayResult extends TranspilerResult {

    /** The result of the first part of the two-way-request (kotlin->swift). */
    protected final TranspilerResult interimResult;

    public TranspilerTwoWayResult(
            TranspilerResult interimResult,
            TranspilerResult finalResult
    ) {
        super(
                interimResult.getRequest(),
                finalResult.getMainFile(),
                finalResult.getAdditionalFiles(),
                finalResult.getTranspilerResponse(),
                interimResult.getTranspilerFeedback()
        );
        this.interimResult = interimResult;
    }

    /** @see TranspilerTwoWayResult#interimResult */
    public TranspilerResult getInterimResult() {
        return interimResult;
    }
}
