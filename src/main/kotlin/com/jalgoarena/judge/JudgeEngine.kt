package com.jalgoarena.judge

import com.jalgoarena.domain.Submission
import com.jalgoarena.domain.JudgeResult
import com.jalgoarena.domain.Problem

interface JudgeEngine {
    fun judge(problem: Problem, submission: Submission): JudgeResult
}
