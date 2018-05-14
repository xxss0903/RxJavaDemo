package com.example.zack.rxjavademo.libphonenumberdemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.zack.rxjavademo.R

/**
 * Created by zack zeng on 2018/5/14.
 */
class CountryListAdapter : RecyclerView.Adapter<ViewHolder>(){

    private lateinit var onItemClickListener: OnItemClickListener
    var datas: MutableList<Country> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.textView?.text = datas[position].fullName
        holder?.itemView?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onItemClickListener.onClick(position, datas[position])
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_country_info, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return datas.size
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

}

interface OnItemClickListener{
    fun onClick(position: Int, country: Country)
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var textView: TextView

    init {
        textView = itemView.findViewById(R.id.tv_country_info)
    }
}