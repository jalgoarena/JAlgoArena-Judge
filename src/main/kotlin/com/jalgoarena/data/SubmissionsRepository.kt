package com.jalgoarena.data

import com.jalgoarena.domain.Submission

interface SubmissionsRepository {
    fun save(submission: Submission, token: String?): Submission?
}
