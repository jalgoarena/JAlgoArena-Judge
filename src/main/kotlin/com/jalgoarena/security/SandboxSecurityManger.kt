package com.jalgoarena.security

import java.security.Permission

class SandboxSecurityManger : SecurityManager() {
    override fun checkExit(p: Int) {
        throw SecurityException()
    }

    override fun checkExec(p0: String?) {
        throw SecurityException()
    }

    override fun checkPermission(p0: Permission?) {
        // allow on all others
    }

    override fun checkPermission(p0: Permission?, p1: Any?) {
        // allow on all others
    }
}