package com.jalgoarena

import com.google.common.io.Resources
import com.jalgoarena.config.TestApplicationConfiguration
import com.jalgoarena.domain.StatusCode
import com.jalgoarena.domain.Submission
import com.jalgoarena.web.ProblemsController
import com.jalgoarena.web.SubmissionsListener
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.rules.SpringClassRule
import org.springframework.test.context.junit4.rules.SpringMethodRule
import java.time.LocalDateTime
import javax.inject.Inject

@RunWith(JUnitParamsRunner::class)
@ContextConfiguration(classes = [TestApplicationConfiguration::class])
class JudgeControllerIntegrationTest {

    companion object {

        @ClassRule
        @JvmField val SCR = SpringClassRule()
    }

    @Rule
    @JvmField val springMethodRule = SpringMethodRule()

    @Inject
    private lateinit var submissionsListener: SubmissionsListener

    @Inject
    private lateinit var problemsController: ProblemsController

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
                .map { it.id }

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
    @Parameters("2-sum, TwoSum", "fib, FibFast", "stoi, MyStoi", "word-ladder, WordLadder", "is-string-unique, IsStringUnique2", "check-perm, CheckPerm", "palindrome-perm, PalindromePerm", "one-away, OneAway", "string-compress, StringCompress", "rotate-matrix, RotateMatrix", "zero-matrix, ZeroMatrix", "remove-dups, RemoveDups", "kth-to-last, KThToLast", "string-rotation, StringRotation", "sum-lists, SumLists", "sum-lists-2, SumLists2", "palindrome-list, PalindromeList", "binary-search, BinarySearch", "delete-tail-node, DeleteTailNode", "repeated-elements, RepeatedElements", "first-non-repeated-char, FirstNonRepeatedChar", "find-middle-node, FindMiddleNode", "horizontal-flip, HorizontalFlip", "vertical-flip, VerticalFlip", "single-number, SingleNumber", "preorder-traversal, PreorderTraversal", "inorder-traversal, InorderTraversal", "postorder-traversal, PostorderTraversal", "height-binary-tree, HeightOfBinaryTree", "sum-binary-tree, SumBinaryTree", "insert-stars, InsertStars", "transpose-matrix, TransposeMatrix", "merge-k-sorted-linked-lists, MergeKSortedLinkedLists")
    fun judgesJavaCorrectSolution(problemId: String, solutionId: String) {
        val sourceCode = Resources.toString(Resources.getResource("$solutionId.java"), Charsets.UTF_8)
        val result = submissionsListener.judge(Submission(sourceCode, "0-0", "0", problemId, LocalDateTime.now().toString()))

        assertThat(result.statusCode).isEqualTo(StatusCode.ACCEPTED.toString())
    }

    @Test
    fun returnsFormattedMessageIfCompilationError() {

        val skeletonCode = problemsController.problem("fib").skeletonCode!!
        val result = submissionsListener.judge(Submission(skeletonCode, "0-0", "3", "fib", LocalDateTime.now().toString()))

        assertThat(result.errorMessage).isEqualTo("Line:11: error: missing return statement\n    }\n    ^\n")
    }
}
