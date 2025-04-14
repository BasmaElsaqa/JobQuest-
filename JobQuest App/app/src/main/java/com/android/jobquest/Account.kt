package com.android.jobquest

interface Account {
    val ID:Int
    val email:String
    val password:String
    val name:String

    fun login(email:String,password:String):Boolean

    fun logout()

    fun resetPAssword(email:String):Boolean


}