package org.algohub.engine.judge;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Simple interface to Java compiler using JSR 199 Compiler API.
 */
final class MemoryJavaCompiler {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryJavaCompiler.class);
    private static final javax.tools.JavaCompiler javaCompiler;

    static {
        javaCompiler = ToolProvider.getSystemJavaCompiler();
        Preconditions.checkNotNull(
                javaCompiler, "Could not get Java compiler. Please, ensure that JDK is used instead of JRE."
        );
    }

    private StandardJavaFileManager standardFileManager() {
        return javaCompiler.getStandardFileManager(null, null, null);
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
     * @return [Object, Method], a class instance and the method
     */
    Object[] compileMethod(final String qualifiedClassName, final String methodName,
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

        MemoryJavaFileManager fileManager = new MemoryJavaFileManager(standardFileManager());
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        JavaCompiler.CompilationTask compilationTask = compilationTask(
                fileName, source, fileManager, diagnostics
        );

        if (!compilationTask.call()) {
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

    private JavaCompiler.CompilationTask compilationTask(
            String fileName,
            String source,
            MemoryJavaFileManager fileManager,
            DiagnosticCollector<JavaFileObject> diagnostics) {

        return javaCompiler.getTask(
                null,
                fileManager,
                diagnostics,
                javacOptions(),
                null,
                prepareTheCompilationUnit(fileName, source)
        );
    }

    private List<JavaFileObject> prepareTheCompilationUnit(String fileName, String source) {
        return Collections.singletonList(makeStringSource(fileName, source));
    }

    private List<String> javacOptions() {
        return Arrays.asList("-Xlint:all", "-deprecation");
    }

    private Map<String, byte[]> getCompilationError(
            DiagnosticCollector<JavaFileObject> diagnostics) throws CompileErrorException {

        final StringBuilder errorMsg = new StringBuilder();
        final List<Diagnostic<? extends JavaFileObject>> diagnostics1 = diagnostics.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> aDiagnostics1 : diagnostics1) {
            errorMsg.append(aDiagnostics1);
            errorMsg.append(System.lineSeparator());
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

    /**
     * ClassLoader that loads .class bytes from memory.
     */
    private static final class MemoryClassLoader extends URLClassLoader {

        private final Map<String, byte[]> classNameToBytecode;

        MemoryClassLoader(final Map<String, byte[]> classNameToBytecode) {
            super(new URL[0], ClassLoader.getSystemClassLoader());
            this.classNameToBytecode = classNameToBytecode;
        }

        @Override
        protected Class findClass(final String className) throws ClassNotFoundException {
            final byte[] buf = classNameToBytecode.get(className);
            if (buf == null) {
                return super.findClass(className);
            } else {
                clearByteMap(className);
                return defineClass(className, buf, 0, buf.length);
            }
        }

        private void clearByteMap(String className) {
            classNameToBytecode.put(className, null);
        }
    }
}
