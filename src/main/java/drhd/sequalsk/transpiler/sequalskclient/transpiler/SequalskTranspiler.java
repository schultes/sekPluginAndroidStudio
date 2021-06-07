package drhd.sequalsk.transpiler.sequalskclient.transpiler;

import drhd.sequalsk.transpiler.sequalskclient.request.TranspilerRequest;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerConfiguration;
import drhd.sequalsk.transpiler.sequalskclient.utils.TranspilerException;

public interface SequalskTranspiler {
    boolean validateConfiguration(TranspilerConfiguration config);
    String transpile(TranspilerRequest request, TranspilerConfiguration config) throws TranspilerException;
}
