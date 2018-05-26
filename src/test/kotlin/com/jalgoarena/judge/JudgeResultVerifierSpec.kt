package com.jalgoarena.judge

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import com.jalgoarena.type.Pair
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class JudgeResultVerifierSpec {
    private val verifier = JudgeResultVerifier()

    @Test
    @Parameters(
            "30.0 0.0,30.00000 0.00000",
            "60.76252177357674 39.2374782760938,60.76252 39.23748",
            "348.5204582909743 -94.1352420548598,348.52046 -94.13524",
            "311.34912128249806 -88.13334698478381,311.34912 -88.13335",
            "282.61216456691426 -82.24021683136623,282.61216 -82.24022",
            "240.32114321986836 -71.20545203487079,240.32114 -71.20545",
            "154.9193338553111 22.54033302267392,154.91933 22.54033"
    )
    fun verifies_correctly_pair_of_doubles(pair1Input: String, pair2Input: String) {
        val pair1Elements = pair1Input.split(" ")
        val pair1 = Pair(pair1Elements[0].toDouble(), pair1Elements[1].toDouble())
        val pair2Elements = pair2Input.split(" ")
        val pair2 = Pair(pair2Elements[0].toDouble(), pair2Elements[1].toDouble())
        assertThat(verifier.isValidAnswer(pair1, pair2)).isTrue()
    }

    @Test
    fun verifies_correctly_pair_of_doubles_with_int() {
        val pairInt = Pair(30, 0)
        val pairDouble = Pair(30.0, 0.0)

        assertThat(verifier.isValidAnswer(pairInt, pairDouble)).isTrue()
    }

    @Test
    fun verifies_correctly_doubles() {
        assertThat(verifier.isValidAnswer(504.34452136357623, 504.34452)).isTrue()
    }
}