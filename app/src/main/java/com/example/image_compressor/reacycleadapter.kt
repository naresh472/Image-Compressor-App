package com.example.image_compressor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.roundToInt

class reacycleadapter(private val mList: List<status_data>) :
    RecyclerView.Adapter<reacycleadapter.ViewHolder>() {

    private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setonItemClickListener(listener: onItemClickListener )
    {
        mListener=listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.status_layout,parent,false)
        return ViewHolder(view,mListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var statusData=mList[position]
        var byte= statusData.img_byte
        var bitmap:Bitmap
        bitmap=BitmapFactory.decodeByteArray(byte,0,byte.size)
         holder.status_image.setImageBitmap(bitmap)
        holder.width.text=statusData.img_width.toString()
        holder.height.text=statusData.img_height.toString()
        var o_size =statusData.original_imgSize/1024

        if(o_size > 1024)
        {
            var ex:Double=o_size.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            holder.original_size.text=ex.toString()+" MB"
        }
        else
        {
            holder.original_size.text=o_size.toString()+" KB"
        }

        var c_size=statusData.compress_imgSize/1024
        if(c_size >1024)
        {
            var ex:Double=c_size.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            holder.custom_size.text=ex.toString()+" MB"
        }
        else
        {
            holder.custom_size.text=c_size.toString()+" KB"
        }





    }

    class ViewHolder(ItemView : View ,listener: onItemClickListener) : RecyclerView.ViewHolder(ItemView) {

        val status_image:ImageView=itemView.findViewById(R.id.status_image)
        var original_size:TextView=itemView.findViewById(R.id.status_img_originalSize)
        var custom_size:TextView=itemView.findViewById(R.id.status_img_customSize)
        var width:TextView=itemView.findViewById(R.id.status_img_width)
        var height:TextView=itemView.findViewById(R.id.status_img_height)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

}