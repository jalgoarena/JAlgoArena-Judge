package org.algohub.engine.compiler.java;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 */
public final class MemoryJavaCompiler {

    public static final MemoryJavaCompiler INSTANCE = new MemoryJavaCompiler();

    private static final Logger LOG = LoggerFactory.getLogger(MemoryJavaCompiler.class);

    private final javax.tools.JavaCompiler tool;
    private final StandardJavaFileManager stdManager;

    private MemoryJavaCompiler() {
        tool = ToolProvider.getSystemJavaCompiler();
        Preconditions.checkNotNull(
                tool, "Could not get Java compiler. Please, ensure that JDK is used instead of JRE."
        );

        stdManager = tool.getStandardFileManager(null, null, null);
    }

    private static String getClassName(final String qualifiedClassName) {
        final int lastDot = qualifiedClassName.lastIndexOf('.');
        if (lastDot == -1) {
            return qualifiedClassName;
        } else {
            return qualifiedClassName.substring(lastDot + 1);
        }
    }

    private static JavaFileObject makeStringSource(String fileName, String code) {
        return new StringInputBuffer(fileName, code);
    }

    /**
     * Compile a single normal method.
     *
     * @return [Object, Method], a class instance and the method
     */
    public Object[] compileMethod(final String qualifiedClassName, final String methodName,
                                  final String source) throws ClassNotFoundException, CompileErrorException {
        final String className = getClassName(qualifiedClassName);
        final Map<String, byte[]> classBytes = compile(className + ".java", source);
        final MemoryClassLoader classLoader = new MemoryClassLoader(classBytes);
        final Class clazz = Class.forName(qualifiedClassName, true, classLoader);
        final Method[] methods = clazz.getDeclaredMethods();

        for (final Method method : methods) {
            if (method.getName().equals(methodName)) {
                try {
                    return new Object[]{clazz.newInstance(), method};
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("Error during creating of new class instance", e);
                    throw new IllegalStateException(e.getMessage());
                }
            }
        }
        throw new NoSuchMethodError(methodName);
    }

    /**
     * compile given String source and return bytecodes as a Map.
     *
     * @param fileName source fileName to be used for error messages etc.
     * @param source   Java source as String
     */
    private Map<String, byte[]> compile(String fileName, String source) throws CompileErrorException {

        Writer err = new PrintWriter(System.err);
        // create a new memory JavaFileManager
        MemoryJavaFileManager fileManager = new MemoryJavaFileManager(stdManager);

        // prepare the compilation unit
        List<JavaFileObject> compUnits = new ArrayList<>(1);
        compUnits.add(makeStringSource(fileName, source));

        return compile(compUnits, fileManager, err);
    }

    private Map<String, byte[]> compile(final List<JavaFileObject> compUnits,
                                        final MemoryJavaFileManager fileManager, Writer err)
            throws CompileErrorException {
        // javac options
        List<String> options = new ArrayList<>();
        options.add("-Xlint:all");
        options.add("-deprecation");

        // to collect errors, warnings etc.
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        // create a compilation task
        javax.tools.JavaCompiler.CompilationTask task =
                tool.getTask(err, fileManager, diagnostics, options, null, compUnits);

        if (!task.call()) {
            return getCompilationError(diagnostics);
        }

        Map<String, byte[]> classBytes = fileManager.getClassBytes();
        try {
            fileManager.close();
        } catch (IOException exp) {
            LOG.error("Cannot close in memory file", exp);
        }

        return classBytes;
    }

    private Map<String, byte[]> getCompilationError(DiagnosticCollector<JavaFileObject> diagnostics) throws CompileErrorException {
        final StringBuilder errorMsg = new StringBuilder();
        final List<Diagnostic<? extends JavaFileObject>> diagnostics1 = diagnostics.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> aDiagnostics1 : diagnostics1) {
            errorMsg.append(aDiagnostics1);
            errorMsg.append('\n');
        }
        throw new CompileErrorException(errorMsg.toString());
    }

    /**
     * A file object used to represent Java source coming from a string.
     */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        final String code;

        StringInputBuffer(String fileName, String code) {
            super(MemoryJavaFileManager.toUri(fileName), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }
    }
}
