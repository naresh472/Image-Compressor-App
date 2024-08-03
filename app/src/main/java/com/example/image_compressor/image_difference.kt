package com.example.image_compressor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jsibbold.zoomage.ZoomageView
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class image_difference : AppCompatActivity() {

    lateinit var bitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_image_difference)

        val cancel_btn:ImageView=findViewById(R.id.cancel_btn)
        val originalSize:TextView=findViewById(R.id.originalSize)
        val compressSize:TextView=findViewById(R.id.compressedSize)
        val originalImage:ZoomageView=findViewById(R.id.original_image_show)
        val compressImage:ZoomageView=findViewById(R.id.compressed_image_show)

        var intent =intent
         var uri:Uri = intent.getStringExtra("uri")!!.toUri()
        var o_size=intent.getIntExtra("oS",0)/1024
        var c_size=intent.getIntExtra("cS",0)/1024
        var qu=intent.getIntExtra("qu",10)
        var width=intent.getIntExtra("w",1024)
        var height=intent.getIntExtra("h",2024)


        var byteArrayOutputStream = ByteArrayOutputStream()

        bitmap =MediaStore.Images.Media.getBitmap(contentResolver,uri)
        bitmap.compress(Bitmap.CompressFormat.JPEG, qu, byteArrayOutputStream)
        var byte = byteArrayOutputStream.toByteArray()

        var bitmap1: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
        bitmap1=Bitmap.createScaledBitmap(bitmap1,width,height,false)
        originalImage.setImageBitmap(bitmap)
        compressImage.setImageBitmap(bitmap1)

        if(o_size > 1024)
        {
            var ex:Double=o_size.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            originalSize.text="Original "+ex + " MB"

        }
        else
        {
            originalSize.text="Original "+o_size + " KB"
        }


        if(c_size >1024)
        {
            var ex:Double=c_size.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            compressSize.text="Compressed "+ex + " MB"

        }
        else
        {
            compressSize.text="Compressed "+c_size + " KB"
        }












        cancel_btn.setOnClickListener {
           onBackPressed()
        }


    }
}