package com.example.chatapp

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.commenttttt)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        val uidd = intent.getStringExtra("postiidd")
        val sendpost=findViewById<ImageView>(R.id.commentpost)
        val edittextpost=findViewById<EditText>(R.id.commentedittext)
        val commrecview=findViewById<RecyclerView>(R.id.commentrecview)
        val ccuu=FirebaseAuth.getInstance().currentUser
        commrecview.layoutManager=LinearLayoutManager(this)
        var commentlist=ArrayList<Comment>()
        var adap=CommentAdapter(commentlist,this)
        commrecview.adapter=adap
        sendpost.setOnClickListener {
            if(TextUtils.isEmpty(edittextpost.text.toString().trimEnd())) {
                edittextpost.setText("")
                return@setOnClickListener
            }
            var hash=HashMap<String,String>()
            hash.put("posttid",uidd!!)
            hash.put("comment",edittextpost.text.toString().trimEnd())
            hash.put("commentposter",ccuu!!.uid)
            FirebaseDatabase.getInstance().getReference()
                    .child("Comment").push().setValue(hash)
            edittextpost.setText("")
        }
        FirebaseDatabase.getInstance().getReference()
                .child("Comment").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentlist.clear()
                snapshot.children.forEach {
                    var commentt=it.getValue(Comment::class.java)
                    if(commentt!!.posttid==uidd)
                        commentlist.add(commentt!!)
                }

                commrecview.adapter=adap
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}