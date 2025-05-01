package com.example.jobquestbottomnavigation

class User: Account {
    override val ID: Int
        get() = TODO("Not yet implemented")
    override val email: String
        get() = TODO("Not yet implemented")
    override val password: String
        get() = TODO("Not yet implemented")
    override val name: String
        get() = TODO("Not yet implemented")

    override fun login(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun resetPassword(email: String): Boolean {
        TODO("Not yet implemented")
    }

}