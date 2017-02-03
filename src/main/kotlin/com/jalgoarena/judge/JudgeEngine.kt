package com.jalgoarena.judge

import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.Problem

interface JudgeEngine {
    fun judge(problem: Problem, userCode: String): JudgeResult
}
