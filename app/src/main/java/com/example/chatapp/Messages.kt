package com.example.chatapp

 data class Messages(val sender:String,val receiver:String,val message:String,val send:String) {

     constructor() : this("" ,"", "","")
}