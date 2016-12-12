
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File


fun main(args: Array<String>) {

    val out = File(File("tmp"), "out")

    val stdlib = File("lib/kotlin-runtime-1.0.1-2.jar")
    println("Stdlib path: ${stdlib.absolutePath}")
    val exitCode = K2JVMCompiler().exec(
            System.out,
            File("tmp/Solution.kt").absolutePath,
            "-d", out.absolutePath,
            "-no-stdlib",
            "-classpath", stdlib.absolutePath
    )

    println("ExitCode: $exitCode")
}