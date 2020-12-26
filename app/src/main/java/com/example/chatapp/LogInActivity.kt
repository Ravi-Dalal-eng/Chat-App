package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {
    var b=false
    private lateinit var mAuth: FirebaseAuth

        fun finishActi()
        {
         finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
         b= activeNetworkInfo != null && activeNetworkInfo.isConnected
        if(b==false)
        {
            startActivity(Intent(this, Internet::class.java))
          Thread.sleep(900)
        }
        mAuth= FirebaseAuth.getInstance()
    }
    override fun onStart() {
        super.onStart()
        val user=mAuth.currentUser
        if(b==true && user!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        Thread.sleep(1000)
        }
        setContentView(R.layout.activity_log_in)
        val emaill=findViewById<EditText>(R.id.email)
        val passwordd=findViewById<EditText>(R.id.password)
        val loginnow=findViewById<TextView>(R.id.loginnow)
        val createnewacc=findViewById<TextView>(R.id.createnewacc)
        loginnow.setOnClickListener {
            val mail=emaill.text.toString().trimEnd()
            val password=passwordd.text.toString().trimEnd()
            if(TextUtils.isEmpty(mail))
            {
                Toast.makeText(this, "Enter Your Email",
                        Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Enter Your Password",
                        Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

             loginnow.visibility=View.INVISIBLE
            mAuth.signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = mAuth.currentUser
                            val intentt=Intent(this,MainActivity::class.java)
                            startActivity(intentt)
                          finish()
                        }

                        else
                        {
                            Snackbar
                                    .make(it, "Invalid Email or Password", Snackbar.LENGTH_LONG)
                                    .show()
                            loginnow.visibility=View.VISIBLE
                        }

                    }
        }
        createnewacc.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }
}