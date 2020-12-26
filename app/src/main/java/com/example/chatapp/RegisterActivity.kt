package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        auth = FirebaseAuth.getInstance()
        val name=findViewById<EditText>(R.id.username)
        val getemail=findViewById<EditText>(R.id.email1)
        val getpassword=findViewById<EditText>(R.id.password1)
        val register=findViewById<TextView>(R.id.register)

register.setOnClickListener {
    val email=getemail.text.toString().trimEnd()
    val password=getpassword.text.toString().trimEnd()
    if(TextUtils.isEmpty(name.text.toString().trimEnd()))
    {
        Toast.makeText(this, "Enter Your Full Name",
            Toast.LENGTH_LONG).show()
        return@setOnClickListener
    }
    if(TextUtils.isEmpty(email))
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

register.visibility= View.GONE
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information

                val user = auth.currentUser
                var myref=FirebaseDatabase.getInstance()
                        .getReference("MyUsers").child(user!!.uid)
                var hashmap=HashMap<String,String>()
                hashmap.put("id",user!!.uid)
                hashmap.put("name",name.text.toString().trimEnd())
                hashmap.put("image","default")
                hashmap.put("status","online")
               val task= myref.setValue(hashmap)
                task.addOnSuccessListener {
                    val intentt=Intent(this@RegisterActivity,MainActivity::class.java)
                    startActivity(intentt)
                    LogInActivity().finishActi()
                    finish()
                }
            }
                else
                {
                    Snackbar
                    .make(it, "This Email is already register by someone", Snackbar.LENGTH_LONG)
                    .show();
                register.visibility= View.VISIBLE
                }
        }
}

    }
}