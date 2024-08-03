package com.example.image_compressor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Gallery
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.jsibbold.zoomage.ZoomageView
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    val mpicking = 1
    lateinit var bitmap:Bitmap
    lateinit var image_show:ZoomageView
    lateinit var proceed:AppCompatButton
    lateinit var image_gallery : Gallery
    lateinit var load:ProgressBar
    lateinit var customGalleryAdapter:galleryAdapter

      lateinit var  arrayUri:ArrayList<String>
    lateinit var  arraybitmap:ArrayList<Bitmap>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var looti_1:LottieAnimationView=findViewById(R.id.lotti_1)
        load=findViewById(R.id.load)

        arrayUri= ArrayList<String>()
        arraybitmap= ArrayList<Bitmap>()
          image_gallery= findViewById(R.id.image_gallery)

        val upload_image:AppCompatButton = findViewById(R.id.upload_image)
        image_show=findViewById(R.id.image_show)
        proceed=findViewById(R.id.proceed)

         val main_layout:RelativeLayout=findViewById(R.id.main_layout)


        //image gallery
        val db= DBhelper(this,null)




        upload_image.setOnClickListener {
            db.deleteimage()
            image_gallery.visibility=View.GONE
            image_show.visibility=View.GONE
            arrayUri.clear()
            arraybitmap.clear()

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.type="image/*"
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),mpicking)
            load.visibility=View.VISIBLE

        }


            proceed.setOnClickListener {


                /*var byteArrayOutputStream = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream)
                var byte =byteArrayOutputStream.toByteArray()
                val base64image = Base64.encodeToString(byte,Base64.DEFAULT)*/
                main_layout.visibility=View.GONE
                load.visibility=View.VISIBLE
                if(arrayUri.size > 0) {

                    val inext = Intent(this, task_layout::class.java)
                    startActivity(inext)




                }
                Handler().postDelayed({
                    main_layout.visibility=View.VISIBLE
                        load.visibility=View.GONE

                },3000)


            }


    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mpicking && resultCode == Activity.RESULT_OK && data != null)
        {



           if(data.clipData != null)
           {
             var  mclipdata = data.clipData
               var count = data.clipData!!.itemCount
               if(count < 6) {
                   try {
                       for (i in 0..count - 1) {

                           var imageuri: String = data.clipData!!.getItemAt(i).uri.toString()
                           val DB = DBhelper(this, null)
                           DB.addimage(imageuri)
                           arrayUri.add(imageuri)
                           var byteArrayOutputStream = ByteArrayOutputStream()

                           var uri = data.clipData!!.getItemAt(i).uri
                           var bitmapnew: Bitmap =
                               MediaStore.Images.Media.getBitmap(contentResolver, uri)
                           bitmapnew.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
                           var byte = byteArrayOutputStream.toByteArray()

                           var bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)


                           arraybitmap.add(bitmap)

                       }
                   }
                   catch (e:Exception)
                   {
                       e.printStackTrace()
                   }
               }
               else
               {
                   Toast.makeText(this,"You can select maximum 5 images",Toast.LENGTH_SHORT).show()
                   load.visibility=View.GONE
                   return
               }
               try {


                   image_show.setImageBitmap(arraybitmap.get(0))
                   proceed.setBackgroundResource(R.drawable.newbutton)
                   customGalleryAdapter = galleryAdapter(applicationContext, arraybitmap)
                   image_gallery.adapter = customGalleryAdapter
                   image_gallery.visibility = View.VISIBLE
                   image_show.visibility = View.VISIBLE
                   load.visibility = View.GONE
                   image_gallery.setOnItemClickListener { adapterView, view, position, id ->


                       image_show.setImageBitmap(arraybitmap.get(position))
                   }
               }
               catch (e:Exception)
               {
                   e.printStackTrace()
               }



           }
            else
           {
               val imageUri:String = data.data!!.toString()
               arrayUri.add(imageUri)
               var byteArrayOutputStream = ByteArrayOutputStream()

               var uri:Uri=data.data!!
               var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
               bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
               arraybitmap.add(bitmap)
               image_show.setImageBitmap(arraybitmap.get(0))

               proceed.setBackgroundResource(R.drawable.newbutton)
               image_gallery.visibility=View.VISIBLE
               image_show.visibility=View.VISIBLE
               load.visibility = View.GONE

           }




        }
        else
        {
            Toast.makeText(this,"Image not found!!",Toast.LENGTH_SHORT).show()
            load.visibility = View.GONE
            return
        }
    }
}




