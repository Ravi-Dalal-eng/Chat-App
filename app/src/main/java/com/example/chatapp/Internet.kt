package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class Internet : AppCompatActivity() {
    lateinit var retry:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        retry=findViewById<Button>(R.id.retry)
        retry.setOnClickListener {
            val connectivityManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            val b= activeNetworkInfo != null && activeNetworkInfo.isConnected
            if(b==true) {
                startActivity(Intent(this, LogInActivity::class.java))
            finish()
            }else{
                Snackbar
                    .make(it, "PLEASE CHECK YOUR INTERNET CONNECTION", Snackbar.LENGTH_SHORT)
                .show()
            }}
    }
}