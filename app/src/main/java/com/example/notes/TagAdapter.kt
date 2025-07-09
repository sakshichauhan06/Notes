package com.example.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagAdapter (
    private val tags: List<TagWithCount>,
    private val onTagClick: (String) -> Unit
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagName: TextView = itemView.findViewById(R.id.tag_name)
        val tagCount: TextView = itemView.findViewById(R.id.tag_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tags_item, parent, false)
        return TagViewHolder(view)

    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tagItem = tags[position]
        holder.tagName.text = tagItem.tag
        holder.tagCount.text = "${tagItem.count} notes"

        holder.itemView.setOnClickListener {
            onTagClick(tagItem.tag)
        }
    }

    override fun getItemCount(): Int = tags.size
    }

}