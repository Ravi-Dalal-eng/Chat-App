package com.example.chatapp


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {
lateinit var imagee:ImageView
private val pickimage=555
    private val pickimagecode=557
    lateinit var storageref:StorageReference
    lateinit var curentttuser:FirebaseUser
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        var v=inflater.inflate(R.layout.fragment_profile, container, false)
        var logout=v.findViewById<ImageView>(R.id.profilefragmentlogout)
        var namee=v.findViewById<TextView>(R.id.profilefragmentname)
         imagee=v.findViewById<ImageView>(R.id.profilefragmentimage)
         curentttuser= FirebaseAuth.getInstance().currentUser!!
          storageref= FirebaseStorage.getInstance().getReference("dp/"+curentttuser!!.uid)

        FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(curentttuser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                namee.text = snapshot.getValue(Users::class.java)!!.name
                val url = snapshot.getValue(Users::class.java)!!.image
                if (url != "default")
                    Glide.with(activity!!).load(Uri.parse(url)).into(imagee)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
imagee.setOnClickListener{
if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
{
    if(activity!!.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), pickimage)
    }
    else
    {
        pickImageFromGallery()
    }

}
    else
    {
    pickImageFromGallery()
}
}
        logout.setOnClickListener {

AlertDialog.Builder(context)
        .setTitle("Logout")
        .setMessage("Are you sure do you want to Logout ?").setCancelable(false)
        .setPositiveButton("Yes"){ _, _->
            FirebaseAuth.getInstance().signOut()
            activity!!.application.startActivity(Intent(activity!!.application, LogInActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            activity!!.finish()
        }.setNeutralButton("No"){ _, _->}.create().show()
        }
        return v
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       if(requestCode==pickimage){
if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
{
    pickImageFromGallery()
}
           else
           { Toast.makeText(activity, " Please allow the Permission to select image", Toast.LENGTH_SHORT).show()
           }
       }
    }
fun pickImageFromGallery()
{
    val intent=Intent(Intent.ACTION_GET_CONTENT)
    intent.setType("image/*")
    startActivityForResult(Intent.createChooser(intent, "Select an image..."), pickimagecode)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val dialog = ProgressDialog(context)
        if(resultCode==RESULT_OK && requestCode==pickimagecode && data!=null){
         var uri=data.data!!
     val task= storageref.putFile(uri)
            dialog.setMessage("Image is uploading")
            dialog.setTitle("Please wait...")
            dialog.setCancelable(false)
            dialog.show()
           task.addOnCompleteListener{
               if(it.isSuccessful) {
    val tt = storageref.downloadUrl
    tt.addOnSuccessListener {
        FirebaseDatabase.getInstance().getReference("MyUsers").child(curentttuser!!.uid).child("image").setValue(tt.result.toString())
        dialog.dismiss()
        Toast.makeText(activity, "Uploaded Sucessfully...", Toast.LENGTH_SHORT).show()
    }
    tt.addOnFailureListener {  dialog.dismiss()
        Toast.makeText(activity, "Please try again...", Toast.LENGTH_SHORT).show()
    }
}
     else if (it.isCanceled){
    dialog.dismiss()
    Toast.makeText(activity, "Please try again...", Toast.LENGTH_SHORT).show()
               }
           }
        }
    }
    }
