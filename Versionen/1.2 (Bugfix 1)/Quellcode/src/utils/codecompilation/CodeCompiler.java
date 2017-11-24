package utils.codecompilation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import logging.AdditionalLogger;
import reflection.customdatatypes.SourceCode;
import utils.files.TemporarilyFiles;
import utils.format.TimeFormat;

public class CodeCompiler {

    private static CodeCompiler instance = null;

    private final HashMap<String, Class<?>> compiledClasses;
    private JavaCompiler compiler = null;

    public static CodeCompiler getInstance() {
        if (instance == null) {
            instance = new CodeCompiler();
        }
        return instance;
    }

    private CodeCompiler() {
        compiledClasses = new HashMap<>();
    }

    public static boolean isJDKInstalled() {
        return ToolProvider.getSystemJavaCompiler() != null;
    }

    public static String determineClassName(String javaCode) throws CompilationError {
        javaCode = javaCode.replaceAll("\\s+", " ");
        String keyword = "public class";
        int indexOfKeyword = javaCode.indexOf(keyword);
        int indexOfbracket = javaCode.indexOf("{");
        if (indexOfKeyword == -1 || indexOfbracket == -1 || indexOfKeyword >= indexOfbracket) {
            throw new CompilationError("Klassennamen nicht identifizierbar");
        } else {
            return javaCode.substring(indexOfKeyword + keyword.length(), indexOfbracket).trim();
        }
    }
    
    public synchronized Class<?> compileAndLoadClass(SourceCode sourceCode) throws IOException, CompilationError {
        return compileAndLoadClass(sourceCode.getJavaCode());
    }

    public synchronized Class<?> compileAndLoadClass(String sourceCode) throws IOException, CompilationError {
        return CodeCompiler.this.compileAndLoadClass(sourceCode, new ByteArrayOutputStream());
    }
    
    public synchronized Class<?> compileAndLoadClass(SourceCode sourceCode, ByteArrayOutputStream compilerOutputStream) throws IOException, CompilationError {
        return compileAndLoadClass(sourceCode.getJavaCode(), compilerOutputStream);
    }

    public synchronized Class<?> compileAndLoadClass(String sourceCode, ByteArrayOutputStream compilerOutputStream) throws IOException, CompilationError {
        Class<?> clazz = compiledClasses.get(sourceCode);
        if (clazz == null) {
            clazz = compileAndLoadClassUncached(sourceCode, compilerOutputStream);
            compiledClasses.put(sourceCode, clazz);
        }
        return clazz;
    }

    private synchronized Class<?> compileAndLoadClassUncached(String sourceCode, ByteArrayOutputStream compilerOutputStream) throws IOException, CompilationError {

        String classname = determineClassName(sourceCode);

        File sourceFile = TemporarilyFiles.createTemporarilyFile(classname + ".java");
        File folder = sourceFile.getParentFile();
        Files.write(sourceFile.toPath(), sourceCode.getBytes(StandardCharsets.UTF_8));

        // .java kompilieren...
        long time = System.currentTimeMillis();
        if (compiler == null) {
            compiler = ToolProvider.getSystemJavaCompiler();
        }
        /* kein else */ if (/* immer noch */compiler == null) {
            throw new CompilationError("Java Development Kit nicht gefunden");
        }
        int compilationResult = compiler.run(null, compilerOutputStream, compilerOutputStream, sourceFile.getPath());
        String compilerOutput = new String(compilerOutputStream.toByteArray());
        AdditionalLogger.out.println("Compilerausgabe:\n" + compilerOutput);
        if (compilationResult == 0) {
            AdditionalLogger.out.println("Code (" + sourceCode.length() + " Zeichen) erfolgreich kompiliert (" + TimeFormat.format(System.currentTimeMillis() - time) + ")");
        } else {
            AdditionalLogger.err.println("Kompilieren von Code (" + sourceCode.length() + " Zeichen) fehlgeschlagen (" + TimeFormat.format(System.currentTimeMillis() - time) + ")");
            throw new CompilationError("Kompilieren fehlgeschlagen", compilerOutput);
        }

        // .class laden...
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{folder.toURI().toURL()});
        File createdClassFile = new File(folder, classname + ".class");
        createdClassFile.deleteOnExit();
        Class<?> clazz;
        try {
            clazz = Class.forName(classname, true, classLoader);
        } catch (ClassNotFoundException ex) {
            throw new CompilationError("Kompilierte Klasse (" + classname + ") nicht gefunden", compilerOutput);
        }
        return clazz;
    }
    
    public synchronized Object compileAndCreateNewInstance(SourceCode sourceCode) throws IOException, CompilationError {
        return compileAndCreateNewInstance(sourceCode.getJavaCode());
    }

    public synchronized Object compileAndCreateNewInstance(String sourceCode) throws IOException, CompilationError {
        return compileAndCreateNewInstance(sourceCode, new ByteArrayOutputStream());
    }
    
    public synchronized Object compileAndCreateNewInstance(SourceCode sourceCode, ByteArrayOutputStream compilerOutputStream) throws IOException, CompilationError {
        return compileAndCreateNewInstance(sourceCode.getJavaCode(), compilerOutputStream);
    }

    public synchronized Object compileAndCreateNewInstance(String sourceCode, ByteArrayOutputStream compilerOutputStream) throws IOException, CompilationError {

        Class<?> clazz = CodeCompiler.this.compileAndLoadClass(sourceCode, compilerOutputStream);

        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new CompilationError("Instanz kann nicht erstellt werden", compilerOutputStream);
        } catch (IllegalAccessException ex) {
            throw new CompilationError("Klasse besitzt keinen öffentlichen Constructor", compilerOutputStream);
        }

        return instance;
    }

}
