package org.algohub.engine.compiler.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * ClassLoader that loads .class bytes from memory.
 */
final class MemoryClassLoader extends URLClassLoader {
    /**
     * ClassName -> bytecode.
     */
    private transient Map<String, byte[]> classBytes;

    private MemoryClassLoader(final Map<String, byte[]> classBytes,
                              final ClassLoader parent) {

        super(new URL[0], parent);
        this.classBytes = classBytes;
    }

    MemoryClassLoader(final Map<String, byte[]> classBytes) {
        this(classBytes, ClassLoader.getSystemClassLoader());
    }

    protected Class findClass(final String className) throws ClassNotFoundException {
        final byte[] buf = classBytes.get(className);
        if (buf == null) {
            return super.findClass(className);
        } else {
            clearByteMap(className);
            return defineClass(className, buf, 0, buf.length);
        }
    }

    private void clearByteMap(String className) {
        classBytes.put(className, null);
    }
}
