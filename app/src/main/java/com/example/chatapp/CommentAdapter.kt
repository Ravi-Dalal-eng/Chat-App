package com.example.chatapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommentAdapter(var commenttlistt:ArrayList<Comment> ,var cc:Context) : RecyclerView.Adapter<CommentAdapter.CommentHolderr>(){

    var cu= FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolderr {
        return CommentAdapter.CommentHolderr(LayoutInflater.from(parent.context).inflate(R.layout.comment_item,parent,false))
    }

    override fun onBindViewHolder(holder: CommentHolderr, position: Int) {
        var commm=commenttlistt[commenttlistt.size-1-position]
        holder.commcomm.text=commm.comment
        FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(commm.commentposter).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.commname.text = snapshot.getValue(Users::class.java)!!.name
                val url = snapshot.getValue(Users::class.java)!!.image
                if (url != "default")
                    Glide.with(cc).load(Uri.parse(url)).into(holder.commimag)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int {
        return  commenttlistt.size
    }
    class CommentHolderr( ite: View): RecyclerView.ViewHolder(ite){
        var commimag=ite.findViewById<ImageView>(R.id.commentitemimage)
        var commname=ite.findViewById<TextView>(R.id.commentitemname)
        var commcomm=ite.findViewById<TextView>(R.id.commentitemcommment)
    }
}