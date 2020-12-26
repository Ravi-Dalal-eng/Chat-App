package com.example.chatapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostAdapter(var posts:ArrayList<Posts>,var c: Context): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

var currentuser=FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostAdapter.PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.postitem,parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var post=posts[posts.size-1-position]
        Glide.with(c).load(Uri.parse(post.uri)).into(holder.image2)
        if (TextUtils.isEmpty(post.caption.trimEnd()))
            holder.caption.visibility=View.GONE
        else
        {
            holder.caption.visibility=View.VISIBLE
            holder.caption.text=post.caption}
        FirebaseDatabase.getInstance().getReference("MyUsers").child(post.publisher)
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.namme.text = snapshot.getValue(Users::class.java)!!.name
                val url = snapshot.getValue(Users::class.java)!!.image
                if (url != "default")
                    Glide.with(c).load(Uri.parse(url)).into(holder.image1)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        countlike(post.postid,holder.likee,holder.likenum)
        countcomment(post.postid,holder.commentnum)
        holder.likee.setOnClickListener {
            if (it.tag=="yes"){
                it.tag="no"
                FirebaseDatabase.getInstance().getReference().child("Like").child(post.postid)
                        .child(currentuser!!.uid).setValue("True")
            }
            else{
                it.tag="yes"
                FirebaseDatabase.getInstance().getReference().child("Like").child(post.postid)
                        .child(currentuser!!.uid).setValue(null)
            }
        }
holder.commentt.setOnClickListener {
    var intenttt= Intent(c,CommentActivity::class.java)
    intenttt.putExtra("postiidd",post.postid)
    intenttt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    c.startActivity(intenttt)
         }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(itemmm: View): RecyclerView.ViewHolder(itemmm){
var image1=itemmm.findViewById<ImageView>(R.id.postimage1)
        var namme=itemmm.findViewById<TextView>(R.id.postname1)
        var image2=itemmm.findViewById<ImageView>(R.id.postimageeee)
        var caption=itemmm.findViewById<TextView>(R.id.postcaption)
        var likee=itemmm.findViewById<ImageView>(R.id.like)
        var commentt=itemmm.findViewById<ImageView>(R.id.comment)
        var likenum=itemmm.findViewById<TextView>(R.id.likenumber)
        var commentnum=itemmm.findViewById<TextView>(R.id.commentnumber)
    }

fun countlike(ss:String,v1:ImageView,v2:TextView){
    FirebaseDatabase.getInstance().getReference().child("Like")
            .child(ss).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if(snapshot.hasChild(currentuser!!.uid))
              v1.setImageResource(R.drawable.notlike)
            else
              v1.setImageResource(R.drawable.like)
            v2.text=snapshot.childrenCount.toString()
        }
                override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}


    fun countcomment(ss:String,v2:TextView){
        FirebaseDatabase.getInstance().getReference().child("Comment")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var ii=0
                        snapshot.children.forEach {
                            var com=it.getValue(Comment::class.java)
                            if(com!!.posttid==ss)
                                ii++
                        }
                        v2.text=ii.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }
}