package com.llj.commondemo.sliding_conflict

import android.R
import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.llj.commondemo.base.BaseActivity
import com.llj.commondemo.databinding.ActivitySlidingConflict2Binding


/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2023
 */
class SlidingConflictActivity2 : BaseActivity<ActivitySlidingConflict2Binding>() {

    override fun buildBinding(): ActivitySlidingConflict2Binding = ActivitySlidingConflict2Binding.inflate(layoutInflater)

    override fun onCreate() {
        binding.close.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            initList()
        }
        initList()
    }

    private fun initList() {
        val list = ArrayList<String>()
        repeat(100) {
            list.add("数据${it}")
        }
        binding.recyclerView.adapter = ItemAdapter(list)
    }

    class ItemAdapter(private val itemList: List<String>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = itemList[position]
            holder.textView.text = item
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView

            init {
                textView = itemView.findViewById<TextView>(R.id.text1)
            }
        }
    }


}