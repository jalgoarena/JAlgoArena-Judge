package org.algohub.engine.compile

import java.io.ByteArrayOutputStream
import java.io.FilterOutputStream
import java.io.OutputStream
import java.net.URI
import java.util.*
import javax.tools.*
import javax.tools.JavaFileObject.Kind

internal class MemoryJavaFileManager(fileManager: JavaFileManager) : ForwardingJavaFileManager<JavaFileManager>(fileManager) {

    var classBytes: MutableMap<String, ByteArray?>? = HashMap()

    override fun close() {
        classBytes = null
    }

    override fun getJavaFileForOutput(location: JavaFileManager.Location, className: String,
                                      kind: Kind, sibling: FileObject): JavaFileObject {
        return if (kind == Kind.CLASS) {
            ClassOutputBuffer(className)
        } else {
            super.getJavaFileForOutput(location, className, kind, sibling)
        }
    }

    private inner class ClassOutputBuffer(val className: String)
        : SimpleJavaFileObject(Companion.toUri(className), Kind.CLASS) {

        override fun openOutputStream(): OutputStream {
            return object : FilterOutputStream(ByteArrayOutputStream()) {
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
            newUri.append("mfm:///${name.replace('.', '/')}")

            if (name.endsWith(JAVA_SOURCE_FILE_EXT)) {
                newUri.replace(newUri.length - JAVA_SOURCE_FILE_EXT.length, newUri.length, JAVA_SOURCE_FILE_EXT)
            }

            return URI.create(newUri.toString())
        }
    }
}
