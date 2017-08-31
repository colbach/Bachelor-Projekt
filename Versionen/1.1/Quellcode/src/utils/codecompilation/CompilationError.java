package utils.codecompilation;

import java.io.ByteArrayOutputStream;

public class CompilationError extends Exception {

    private final String compilerOutput;

    public CompilationError(String message, String compilerOutput) {
        super(message);
        this.compilerOutput = compilerOutput;
    }

    public CompilationError(String message, ByteArrayOutputStream compilerOutput) {
        super(message);
        this.compilerOutput = new String(compilerOutput.toByteArray());
    }

    public CompilationError(String message) {
        super(message);
        this.compilerOutput = "";
    }

    public String getCompilerOutput() {
        if (compilerOutput == null) {
            return "";
        } else {
            return compilerOutput;
        }
    }

    public boolean hasCompilerOutput() {
        return compilerOutput != null && compilerOutput.length() > 0;
    }

    @Override
    public String toString() {
        if (hasCompilerOutput()) {
            String toString = getMessage();
            toString += "\n\nCompilerausgabe:\n\n";
            toString += getCompilerOutput();
            return toString;
        } else {
            return getMessage();
        }
    }
}
