package com.example.jobquestbottomnavigation

interface Account {
    val ID:Int
    val email:String
    val password:String
    val name:String

    fun login(email:String,password:String):Boolean

    fun logout()

    fun resetPassword(email:String):Boolean


}