package com.example.image_compressor

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Gallery
import android.widget.ImageView
import androidx.core.net.toUri
import com.jsibbold.zoomage.ZoomageView
import java.io.ByteArrayOutputStream

class galleryAdapter(private val context: Context,private val arraybitmap:ArrayList<Bitmap>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return  arraybitmap.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            val imageView = ImageView(context)
        try {
            imageView.scaleType = ImageView.ScaleType.FIT_XY

            imageView.setImageBitmap(arraybitmap.get(p0))
            imageView.layoutParams = Gallery.LayoutParams(200, 200)


        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        return imageView
    }
}