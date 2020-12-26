package com.example.chatapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerrAdapter(val fm:FragmentManager):FragmentPagerAdapter(fm) {
    var arrylist=ArrayList<Fragment>()
    fun addFragment(fragment:Fragment){
        arrylist.add(fragment)
    }
    override fun getCount(): Int {
      return arrylist.size
    }

    override fun getItem(position: Int): Fragment {
return arrylist.get(position)
    }
}