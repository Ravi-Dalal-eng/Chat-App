package com.example.chatapp

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    var currentuserrrr=FirebaseAuth.getInstance().currentUser
    var reference=FirebaseDatabase.getInstance().getReference("MyUsers")
    var chatreference=FirebaseDatabase.getInstance().getReference("MyChats")
    lateinit var listenerr:ValueEventListener
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        val mediaplayer = MediaPlayer.create(this, R.raw.buttonclick)
        val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
        val uidd = intent.getStringExtra("uuid")
        var list = ArrayList<Messages>()
        var topnamee = findViewById<TextView>(R.id.topname)
        var recview = findViewById<RecyclerView>(R.id.chatrecyclerview)
        var edittext = findViewById<EditText>(R.id.etmsg)
        var imageview = findViewById<ImageView>(R.id.ivmsg)
        var topimage = findViewById<ImageView>(R.id.topimage)
        reference.child(uidd!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                topnamee.text = snapshot.getValue(Users::class.java)!!.name
                var url = snapshot.getValue(Users::class.java)!!.image
                if (url != "default")
                    Glide.with(this@ChatActivity).load(Uri.parse(url)).into(topimage)
            }
            override fun onCancelled(error: DatabaseError)
            {
                TODO("Not yet implemented")
            }
        })


        var adapterrrr = ChatAdapter(list)
        recview.adapter = adapterrrr
        recview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        imageview.setOnClickListener {
            var textt = edittext.text.toString().trimEnd()
            if (TextUtils.isEmpty(textt)) {
                edittext.setText("")
                return@setOnClickListener
            }
            else {
                var hashm = HashMap<String, String>()
                hashm.put("sender", currentuserrrr!!.uid)
                hashm.put("receiver",uidd)
                hashm.put("message", textt)
                hashm.put("send", "Delivered")
             chatreference.push().setValue(hashm)
                reference.child(uidd).child(currentuserrrr!!.uid).setValue("true")
                edittext.setText("")
                val currentVolume: Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                val maxVolume: Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val percent = 0.8f
                val seventyVolume = (maxVolume * percent).toInt()
                if (currentVolume > seventyVolume)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, 0)
                mediaplayer.start()
            }
        }



             listenerr= chatreference.addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        snapshot.children.forEach {
                            var chat=it.getValue(Messages::class.java)
                            if(chat!!.sender==currentuserrrr!!.uid && chat!!.receiver==uidd || chat!!.sender==uidd && chat!!.receiver==currentuserrrr!!.uid)
                                list.add(chat)
                        }
                 var chatt=  snapshot.children.last()
                      var chattt=chatt.getValue(Messages::class.java)
                    if (chattt!!.sender==uidd && chattt!!.receiver==currentuserrrr!!.uid)
                  {
                     chatreference.child(chatt.key!!).child("send").setValue("Seen")
                  }
                        reference.child(currentuserrrr!!.uid).child(uidd).setValue("false")
                        recview.adapter = adapterrrr
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
             })

    }

    override fun onPause() {
        chatreference.removeEventListener(listenerr)
        super.onPause()
    }
}