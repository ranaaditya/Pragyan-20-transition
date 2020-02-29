package com.rana_aditya.pragyan_transition.CircularRecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rana_aditya.pragyan_transition.R

class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false))

    override fun getItemCount() = 200

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
}