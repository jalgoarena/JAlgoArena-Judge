
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File
import java.util.*


fun main(args: Array<String>) {

    val sourceCode = """fun main(args: Array<String>) {
    println("Hello, World!")
}"""

    val randomUUID = UUID.randomUUID()

    val tmpDir = File("tmp", "$randomUUID")
    tmpDir.mkdirs()
    val sourceFile = File(tmpDir, "Solution.kt")
    sourceFile.writeText(sourceCode)
    val out = File(tmpDir, "out")

    val exitCode = K2JVMCompiler().exec(
            System.out,
            sourceFile.absolutePath,
            "-include-runtime",
            "-d", out.absolutePath,
            "-kotlin-home", File("kotlinHome").absolutePath
    )

    println("ExitCode: $exitCode")
    val byteCode = File(out, "SolutionKt.class").readBytes()

    println("Bytecode size: ${byteCode.size}")

    tmpDir.deleteRecursively()
}