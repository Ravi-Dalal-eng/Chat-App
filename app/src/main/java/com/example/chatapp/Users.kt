package com.example.chatapp

data class Users(val id:String,val name:String,val image:String,val status:String)
{
    constructor() : this("" ,"", "","")
}