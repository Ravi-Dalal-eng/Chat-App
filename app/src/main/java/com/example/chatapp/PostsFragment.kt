package com.example.chatapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PostsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val pickimagee=5555
    private val pickimagecodee=5557
lateinit  var addapost :TextView
lateinit var postrecviewwwww:RecyclerView
    lateinit var storagereff: StorageReference
    lateinit var curentttuserr: FirebaseUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vvvv= inflater.inflate(R.layout.fragment_posts, container, false)
     addapost  =vvvv.findViewById<TextView>(R.id.addapost)
       postrecviewwwww=vvvv.findViewById<RecyclerView>(R.id.postrecviewww)
        curentttuserr= FirebaseAuth.getInstance().currentUser!!
        storagereff= FirebaseStorage.getInstance().getReference("posts/")
var postlist=ArrayList<Posts>()
        var adapt=PostAdapter(postlist,activity!!.application)
postrecviewwwww.layoutManager=LinearLayoutManager(activity!!.application)
        postrecviewwwww.adapter=adapt

        FirebaseDatabase.getInstance().getReference().child("Post")
                .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postlist.clear()
                snapshot.children.forEach {
                    var posttt=it.getValue(Posts::class.java)
                    postlist.add(posttt!!)
                }

                adapt.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                        }
                })











        addapost.setOnClickListener {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if(activity!!.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), pickimagee)
                }
                else{
                    pickTheImageFromGallery()
                }

            }
            else{
                pickTheImageFromGallery()
            }


        }
    return vvvv
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==pickimagee){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {pickTheImageFromGallery()}
            else
            { Toast.makeText(activity, " Please allow the Permission to select image", Toast.LENGTH_SHORT).show() }
        }
    }
    fun pickTheImageFromGallery(){
        val intentt= Intent(Intent.ACTION_GET_CONTENT)
        intentt.setType("image/*")
        startActivityForResult(Intent.createChooser(intentt, "Select an image..."), pickimagecodee)
    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       val progggdialog = ProgressDialog(context)
        if(resultCode== Activity.RESULT_OK && requestCode==pickimagecodee && data!=null){
            var urii=data.data!!
            val inflate_view=layoutInflater.inflate(R.layout.post_dialog,null)
            val imageee=inflate_view.findViewById<ImageView>(R.id.dialogimage)
            val caption=inflate_view.findViewById<EditText>(R.id.dialogcaption)

            imageee.setImageURI(urii)
            val alert_dialog=AlertDialog.Builder(context)
            alert_dialog.setView(inflate_view)
            alert_dialog.setCancelable(false)
            alert_dialog.setNegativeButton("CANCEL"){_,_->}
            alert_dialog.setPositiveButton("POST"){_,_->
                val taskk= storagereff.child(urii.lastPathSegment!!).putFile(urii)
            progggdialog.setMessage("Post is uploading")
            progggdialog.setTitle("Please wait...")
                progggdialog.setCancelable(false)
           progggdialog.show()


                taskk.addOnCompleteListener{
                  if(it.isSuccessful){
val ttt=storagereff.child(urii.lastPathSegment!!).downloadUrl
                      ttt.addOnSuccessListener {     var hashmm = HashMap<String, String>()
                          hashmm.put("publisher", curentttuserr!!.uid)
                          hashmm.put("uri",ttt.result.toString())
                          hashmm.put("caption", caption.text.toString())
                      var postid=    FirebaseDatabase.getInstance().getReference().child("Post").push().key

                          hashmm.put("postid",postid!!)
                          FirebaseDatabase.getInstance().getReference().child("Post").child(postid!!).setValue(hashmm)
                          progggdialog.dismiss()
                          Toast.makeText(activity, "Uploaded Sucessfully...", Toast.LENGTH_SHORT).show() }
                    ttt.addOnFailureListener {
                        progggdialog.dismiss()
                        Toast.makeText(activity, "Please try again...", Toast.LENGTH_LONG).show()
                    }


                }
                else if (it.isCanceled){
                    progggdialog.dismiss()
                    Toast.makeText(activity, "Please try again...", Toast.LENGTH_LONG).show()
                }
            }


            }
            alert_dialog.create().show()
//
//



        }
    }






}
