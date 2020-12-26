package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersAdapter(var user:ArrayList<Users>,var c:Context):RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

var curruser=FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
       return UsersAdapter.UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.useritem,parent,false))
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
       holder.username.text=user.get(position).name
        var status=user.get(position).status
        if(status.equals("online"))
        holder.online.visibility=View.VISIBLE
        else
            holder.online.visibility=View.INVISIBLE
        holder.linearlayout11.setOnClickListener {
            var intentt= Intent(c,ChatActivity::class.java)
            intentt.putExtra("uuid",user.get(position).id)
            intentt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            c.startActivity(intentt)
        }
        if (user.get(position).image != "default")
            Glide.with(c).load(Uri.parse(user.get(position).image)).into(holder.imagee)

        FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(curruser!!.uid)
                .child(user.get(position).id).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                      var messagee=  snapshot.getValue(String::class.java)
                        if(messagee=="true")
                         holder.message.visibility=View.VISIBLE
                        else
                          holder.message.visibility=View.INVISIBLE
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
    }

    override fun getItemCount(): Int {
        return user.size
    }

    class UsersViewHolder(itemmm:View):RecyclerView.ViewHolder(itemmm){
        var username=itemmm.findViewById<TextView>(R.id.useritemname)
        var online=itemmm.findViewById<ImageView>(R.id.useritemonline)
        var imagee=itemmm.findViewById<ImageView>(R.id.useritemimage)
        var linearlayout11=itemmm.findViewById<LinearLayout>(R.id.linearlayout11)
        var message=itemmm.findViewById<ImageView>(R.id.useritemmessage)
    }

}