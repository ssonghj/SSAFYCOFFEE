package com.ssafy.smartcafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.google.android.material.internal.ViewUtils.getBackgroundColor
import com.ssafy.smartcafe.R

class ViewPagerAdapter(var context: Context,private val resource: Int, idolList: ArrayList<Int>)
    : RecyclerView.Adapter<PagerViewHolder>() {

    var item = idolList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(resource, parent, false)

        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.idol.setImageResource(item[position % item.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}

class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var idol:ImageView = itemView!!.findViewById(R.id.imageView_idol)
}