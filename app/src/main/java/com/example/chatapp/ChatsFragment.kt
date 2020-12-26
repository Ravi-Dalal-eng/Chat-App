package com.example.chatapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
 private var dbreference=FirebaseDatabase.getInstance().getReference("MyUsers")
private var currentuser=FirebaseAuth.getInstance().currentUser
private var userlist=ArrayList<Users>()
private var chatfragrecview:RecyclerView?=null
    private var mainlayout: LinearLayout? = null
    private var useradapter:UsersAdapter?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var vvv=inflater!!.inflate(R.layout.fragment_chats, container, false)
        fillrecyclerview(vvv)
        return vvv
    }
fun fillrecyclerview(view:View){
    useradapter=UsersAdapter(userlist,activity!!.application)
mainlayout=view!!.findViewById(R.id.chatfragment)
   chatfragrecview=view!!.findViewById<RecyclerView>(R.id.chatfragrecview)
    chatfragrecview!!.layoutManager=LinearLayoutManager(activity)
    chatfragrecview!!.adapter=useradapter
    dbreference.addValueEventListener(object :ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            userlist.clear()
            snapshot.children.forEach {
                var userrr = it.getValue(Users::class.java)
                if (userrr!!.id != currentuser!!.uid)
                    userlist.add(userrr!!)
            }
            useradapter!!.notifyDataSetChanged()
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })
      }
}
