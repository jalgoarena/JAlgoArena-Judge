package org.algohub.engine

import com.google.common.base.Charsets
import com.google.common.io.Resources
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.algohub.engine.judge.StatusCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class JudgeControllerIntegrationTest {

    private val judgeController = JudgeController()
    private val problemsController = ProblemsController()

    @Test
    @Parameters("fib, Fibonacci", "2-sum, 2 Sum", "stoi, String to Integer (stoi)", "word-ladder, Word Ladder")
    fun findsExistingProblems(problemId: String, problemTitle: String) {
        val fib = problemsController.problem(problemId)
        assertThat(fib.title).isEqualTo(problemTitle)
    }

    @Test
    @Parameters("fib", "2-sum", "stoi", "word-ladder")
    fun includesProblemInListOfAllProblems(problemId: String) {
        val problemIds = problemsController.problems()
                .map({ it.id })

        val result = problemIds.contains(problemId)
        assertThat(result).isTrue()
    }

    @Test
    @Parameters("fib", "2-sum", "stoi", "word-ladder")
    fun generatesNonEmptyJavaSkeletonCode(problemId: String) {
        val skeletonCode = problemsController.problem(problemId).skeletonCode
        assertThat(skeletonCode).isNotEmpty()
    }

    @Test
    @Parameters("fib", "2-sum", "stoi", "word-ladder")
    fun generatesNonEmptyKotlinSkeletonCode(problemId: String) {
        val skeletonCode = problemsController.problem(problemId).kotlinSkeletonCode
        assertThat(skeletonCode).isNotEmpty()
    }

    @Test
    @Parameters("2-sum, TwoSum", "fib, FibFast", "stoi, MyStoi", "word-ladder, WordLadder", "is-string-unique, IsStringUnique2", "check-perm, CheckPerm", "palindrome-perm, PalindromePerm", "one-away, OneAway", "string-compress, StringCompress", "rotate-matrix, RotateMatrix", "zero-matrix, ZeroMatrix", "remove-dups, RemoveDups", "kth-to-last, KThToLast", "string-rotation, StringRotation", "sum-lists, SumLists", "sum-lists-2, SumLists2", "palindrome-list, PalindromeList", "binary-search, BinarySearch", "delete-tail-node, DeleteTailNode", "repeated-elements, RepeatedElements", "first-non-repeated-char, FirstNonRepeatedChar", "find-middle-node, FindMiddleNode", "horizontal-flip, HorizontalFlip", "vertical-flip, VerticalFlip", "single-number, SingleNumber", "preorder-traversal, PreorderTraversal", "inorder-traversal, InorderTraversal", "postorder-traversal, PostorderTraversal", "height-binary-tree, HeightOfBinaryTree", "sum-binary-tree, SumBinaryTree", "insert-stars, InsertStars", "transpose-matrix, TransposeMatrix")
    fun judgesJavaCorrectSolution(problemId: String, solutionId: String) {
        val sourceCode = Resources.toString(Resources.getResource(solutionId + ".java"), Charsets.UTF_8)
        val result = judgeController.judge(problemId, sourceCode)

        assertThat(result.statusCode).isEqualTo(StatusCode.ACCEPTED.toString())
    }

    @Test
    @Parameters("2-sum, TwoSum", "fib, FibFast", "stoi, MyStoi", "word-ladder, WordLadder", "is-string-unique, IsStringUnique2", "check-perm, CheckPerm", "palindrome-perm, PalindromePerm", "one-away, OneAway", "string-compress, StringCompress", "rotate-matrix, RotateMatrix", "zero-matrix, ZeroMatrix", "remove-dups, RemoveDups", "kth-to-last, KThToLast", "string-rotation, StringRotation", "sum-lists, SumLists", "sum-lists-2, SumLists2", "palindrome-list, PalindromeList", "binary-search, BinarySearch", "delete-tail-node, DeleteTailNode", "repeated-elements, RepeatedElements", "first-non-repeated-char, FirstNonRepeatedChar", "find-middle-node, FindMiddleNode", "horizontal-flip, HorizontalFlip", "vertical-flip, VerticalFlip", "single-number, SingleNumber", "preorder-traversal, PreorderTraversal", "inorder-traversal, InorderTraversal", "postorder-traversal, PostorderTraversal", "height-binary-tree, HeightOfBinaryTree", "sum-binary-tree, SumBinaryTree", "insert-stars, InsertStars", "transpose-matrix, TransposeMatrix")
    fun judgesKotlinCorrectSolution(problemId: String, solutionId: String) {
        val sourceCode = Resources.toString(Resources.getResource(solutionId + ".kt"), Charsets.UTF_8)
        val result = judgeController.judge(problemId, sourceCode)

        assertThat(result.statusCode).isEqualTo(StatusCode.ACCEPTED.toString())
    }

    @Test
    fun returnsFormattedMessageIfCompilationError() {

        val skeletonCode = problemsController.problem("fib").skeletonCode
        val result = judgeController.judge("fib", skeletonCode!!)

        assertThat(result.errorMessage).isEqualTo("Line:11: error: missing return statement\n    }\n    ^\n")
    }

    @Test
    fun returnsRuntimeErrorIfWePassWrongProblemId() {
        val result = judgeController.judge("dummy", "")

        assertThat(result.statusCode).isEqualTo(StatusCode.RUNTIME_ERROR.toString())
    }
}
