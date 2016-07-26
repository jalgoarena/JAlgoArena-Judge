package org.algohub.engine.compiler.java;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * ClassLoader that loads .class bytes from memory.
 */
final class MemoryClassLoader extends URLClassLoader {
  /** ClassName -> bytecode. */
  private transient Map<String, byte[]> classBytes;

    private MemoryClassLoader(final Map<String, byte[]> classBytes, final String classPath,
                              final ClassLoader parent) {
    super(toUrls(classPath), parent);
    this.classBytes = classBytes;
  }

  MemoryClassLoader(final Map<String, byte[]> classBytes) {
    this(classBytes, null, ClassLoader.getSystemClassLoader());
  }

  private static URL[] toUrls(final String classPath) {
    if (classPath == null) {
      return new URL[0];
    }

    final List<URL> list = new ArrayList<>();
    final StringTokenizer st = new StringTokenizer(classPath, File.pathSeparator);
    while (st.hasMoreTokens()) {
      final String token = st.nextToken();
      final File file = new File(token);
      try {
        if (file.exists()) {
          list.add(file.toURI().toURL());
        } else {
          list.add(new URL(token));
        }
      } catch (MalformedURLException mue) {
      }
    }
    final URL[] res = new URL[list.size()];
    list.toArray(res);
    return res;
  }

  protected Class findClass(final String className) throws ClassNotFoundException {
    final byte[] buf = classBytes.get(className);
    if (buf == null) {
      return super.findClass(className);
    } else {
      // clear the bytes in map -- we don't need it anymore
      classBytes.put(className, null);
      return defineClass(className, buf, 0, buf.length);
    }
  }
}
