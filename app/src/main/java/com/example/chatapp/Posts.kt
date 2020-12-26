package com.example.chatapp

data class Posts(var publisher:String,var caption:String,var uri:String,var comments:String,var postid:String){
    constructor() : this("" ,"","", "","")
}