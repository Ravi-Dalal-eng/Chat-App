package com.example.chatapp

data class Comment(val posttid:String,val comment:String,val commentposter:String) {
    constructor() : this("" ,"", "")
}