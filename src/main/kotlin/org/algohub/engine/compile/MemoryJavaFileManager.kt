package org.algohub.engine.compile

import javax.tools.*
import javax.tools.JavaFileObject.Kind
import java.io.ByteArrayOutputStream
import java.io.FilterOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import java.util.HashMap

/**
 * JavaFileManager that keeps compiled .class bytes in memory.
 */
internal class MemoryJavaFileManager(fileManager: JavaFileManager) : ForwardingJavaFileManager<JavaFileManager>(fileManager) {

    var classBytes: MutableMap<String, ByteArray?>? = HashMap()

    @Throws(IOException::class)
    override fun close() {
        classBytes = null
    }

    @Throws(IOException::class)
    override fun getJavaFileForOutput(location: JavaFileManager.Location, className: String,
                                      kind: Kind, sibling: FileObject): JavaFileObject {
        if (kind == Kind.CLASS) {
            return ClassOutputBuffer(className)
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling)
        }
    }

    /**
     * A file object that stores Java bytecode into the classBytes map.
     */
    private inner class ClassOutputBuffer internal constructor(private val className: String)
        : SimpleJavaFileObject(Companion.toUri(className), Kind.CLASS) {

        override fun openOutputStream(): OutputStream {
            return object : FilterOutputStream(ByteArrayOutputStream()) {
                @Throws(IOException::class)
                override fun close() {
                    out.close()
                    val bos = out as ByteArrayOutputStream
                    classBytes!!.put(className, bos.toByteArray())
                }
            }
        }
    }

    companion object {

        private val JAVA_SOURCE_FILE_EXT = ".java"

        fun toUri(name: String): URI {
            val newUri = StringBuilder()
            newUri.append("mfm:///")
            newUri.append(name.replace('.', '/'))
            if (name.endsWith(JAVA_SOURCE_FILE_EXT)) {
                newUri.replace(newUri.length - JAVA_SOURCE_FILE_EXT.length, newUri.length, JAVA_SOURCE_FILE_EXT)
            }
            return URI.create(newUri.toString())
        }
    }
}
