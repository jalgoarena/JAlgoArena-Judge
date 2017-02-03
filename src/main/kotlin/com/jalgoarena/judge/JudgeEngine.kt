package com.jalgoarena.judge

import com.jalgoarena.domain.JudgeRequest
import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.Problem

interface JudgeEngine {
    fun judge(problem: Problem, judgeRequest: JudgeRequest): JudgeResult
}
