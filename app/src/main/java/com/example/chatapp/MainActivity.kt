package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout:TabLayout
    lateinit var viewPager:ViewPager
    var firebarecurrentuser=FirebaseAuth.getInstance().currentUser
    var firebarereff=FirebaseDatabase.getInstance().getReference("MyUsers").child(firebarecurrentuser!!.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        tabLayout=findViewById<TabLayout>(R.id.tab_layout)
        viewPager=findViewById<ViewPager>(R.id.view_pager)
        val pagerrAdapter=PagerrAdapter(supportFragmentManager)
        pagerrAdapter.addFragment(ChatsFragment())
        pagerrAdapter.addFragment(PostsFragment())
        pagerrAdapter.addFragment(ProfileFragment())
        viewPager.adapter=pagerrAdapter
        tabLayout.setOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
         viewPager.setCurrentItem(tab!!.position)
                if(tab.position==0 || tab.position==1 || tab.position==2)
                    pagerrAdapter.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        viewPager.addOnPageChangeListener(object :TabLayout.TabLayoutOnPageChangeListener(tabLayout){})
    }
    override fun onResume() {
        super.onResume()
        firebarereff.child("status").setValue("online")
    }

    override fun onDestroy() {
        firebarereff.child("status").setValue("offline")
        super.onDestroy()
    }

}