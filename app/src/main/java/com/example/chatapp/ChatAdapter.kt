package com.example.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(var listt:ArrayList<Messages> ) :RecyclerView.Adapter<ChatAdapter.Holderr>(){

var cu=FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holderr {

        if(viewType==1)
           return ChatAdapter.Holderr(LayoutInflater.from(parent.context).inflate(R.layout.chatitem,parent,false))

        else
            return ChatAdapter.Holderr(LayoutInflater.from(parent.context).inflate(R.layout.leftchatitem,parent,false))
    }
    override fun onBindViewHolder(holder: Holderr, position: Int) {
     holder.textview.text=listt.get(listt.size-1-position).message
        if(position==0 && cu!!.uid==listt.get(listt.size-1).sender)
        {holder.seenview.visibility=View.VISIBLE
        holder.seenview.text=listt.get(listt.size-1).send}
        else
            holder.seenview.visibility=View.GONE
      }

    override fun getItemCount(): Int {
       return  listt.size
    }
    class Holderr( itemview:View):RecyclerView.ViewHolder(itemview)
    {
        var textview=itemview.findViewById<TextView>(R.id.chattext)
        var seenview=itemview.findViewById<TextView>(R.id.chattextseen)
    }

    override fun getItemViewType(position: Int): Int {
        if (listt.get(listt.size-1-position).sender==cu!!.uid)
        return 1
        else
            return 2
    }
}