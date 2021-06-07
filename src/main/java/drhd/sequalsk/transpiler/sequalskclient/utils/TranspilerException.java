package drhd.sequalsk.transpiler.sequalskclient.utils;


import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;

public class TranspilerException extends Exception {

    private final TranspilerRequest transpilerRequest;
    private final TranspilerConfiguration transpilerConfiguration;

    public TranspilerException(TranspilerConfiguration transpilerConfiguration, TranspilerRequest transpilerRequest) {
        super();
        this.transpilerConfiguration = transpilerConfiguration;
        this.transpilerRequest = transpilerRequest;
    }

    public TranspilerException(String message, Throwable cause, TranspilerConfiguration transpilerConfiguration, TranspilerRequest transpilerRequest) {
        super(message, cause);
        this.transpilerConfiguration = transpilerConfiguration;
        this.transpilerRequest = transpilerRequest;
    }

    public TranspilerException(String message, TranspilerConfiguration transpilerConfiguration, TranspilerRequest transpilerRequest) {
        super(message);
        this.transpilerConfiguration = transpilerConfiguration;
        this.transpilerRequest = transpilerRequest;
    }

    public TranspilerException(Throwable cause, TranspilerConfiguration transpilerConfiguration, TranspilerRequest transpilerRequest) {
        super(cause);
        this.transpilerConfiguration = transpilerConfiguration;
        this.transpilerRequest = transpilerRequest;
    }

    public TranspilerConfiguration getTranspilerConfig() {
        return this.transpilerConfiguration;
    }

    public TranspilerRequest getTranspilerRequest() {
        return transpilerRequest;
    }
}
