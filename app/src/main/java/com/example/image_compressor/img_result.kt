package com.example.image_compressor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt

class img_result : AppCompatActivity() {
    lateinit var  arrbyte:ArrayList<ByteArray>
         lateinit var extra_bitmap:Bitmap
    lateinit var builder: Notification.Builder
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    private val channelId = "i.apps.notifications"
    lateinit var  arrData:ArrayList<status_data>
    lateinit var bitmap_logo:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_img_result)
        arrbyte=ArrayList<ByteArray>()
        var original_img_size:TextView=findViewById(R.id.result_original_size)
        var custom_img_size:TextView=findViewById(R.id.result_custom_size)
        val cancel_btn:ImageView=findViewById(R.id.cancel_btn)

        arrData = ArrayList<status_data>()
        arrData.clear()

        var recycle:RecyclerView=findViewById(R.id.status_recycle)
        recycle.layoutManager = LinearLayoutManager(this)

        var intent = intent
        var arrImageUri = ArrayList<String>()
        var quality = intent.getStringExtra("qu")

        var code:Int=intent.getStringExtra("code")!!.toInt()




        val db = DBhelper(this, null)
        val cursor =db.getimage()
        while (cursor!!.moveToNext())
        {
            arrImageUri?.add(cursor.getString(0))
        }

        var totalOriginalSize:Int=0
        var totalCustomSize:Int=0

        for (i in 0..arrImageUri!!.size-1)
        {
            var qu = quality?.toInt()
            var byteArrayOutputStream = ByteArrayOutputStream()

            var uri = arrImageUri.get(i).toUri()
            var bitmap:Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                if(code == 1500) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, intent.getStringExtra("width")!!.toInt(),
                        intent.getStringExtra("height")!!.toInt(), false)
                }

             bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
            var original_byte = byteArrayOutputStream.toByteArray()
            var original_size = original_byte.size
            var ocustom_byte:ByteArray
            var compress_size:Int=0

            var byteArrayOutputStream_1 = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,qu!!,byteArrayOutputStream_1)



                ocustom_byte= byteArrayOutputStream_1.toByteArray()

                compress_size=ocustom_byte.size




            totalOriginalSize +=original_size
            totalCustomSize+=compress_size
            arrData.add(status_data(arrImageUri.get(i),original_size,compress_size,ocustom_byte,bitmap.width,bitmap.height))
            extra_bitmap=bitmap




            try {
                val filename = "my_image_${System.currentTimeMillis()}.jpg"
                var fos: OutputStream? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    contentResolver?.also { resolver ->
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                            put(
                                MediaStore.MediaColumns.RELATIVE_PATH,
                                Environment.DIRECTORY_PICTURES
                            )
                        }
                        val imageUri: Uri? =
                            resolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues
                            )
                        fos = imageUri?.let { resolver.openOutputStream(it) }
                    }


                } else {
                    val imagesDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val image = File(imagesDir, filename)
                    fos = FileOutputStream(image)
                }

                fos?.use {
                    if (qu != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG,qu, it)
                    }
                  //  Toast.makeText(this, "Downloded", Toast.LENGTH_SHORT).show()


                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }




        }

        totalOriginalSize = totalOriginalSize / 1024
        if(totalOriginalSize > 1024)
        {
            var ex:Double=totalOriginalSize.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            original_img_size.text=ex.toString()+ " MB"
        }
        else
        {
            original_img_size.text=totalOriginalSize.toString()+" KB"
        }

        totalCustomSize = totalCustomSize / 1024
        if(totalCustomSize > 1024)
        {
            var ex:Double=totalCustomSize.toDouble() /1024
            ex =(ex * 100.0).roundToInt() /100.0
            custom_img_size.text=ex.toString()+ " MB"
        }
        else
        {
            custom_img_size.text=totalCustomSize.toString()+" KB"
        }





        val Adapter = reacycleadapter(arrData)
        recycle.adapter=Adapter

        Adapter.setonItemClickListener(object  : reacycleadapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                var uri = arrData.get(position).imgUri
                var originalSize=arrData.get(position).original_imgSize
                var compressSize=arrData.get(position).compress_imgSize
                var qu = quality?.toInt()


                if (qu != null) {
                    next(uri,originalSize,compressSize,qu,extra_bitmap)
                }


            }
        })
        /*
       bitmap_logo = BitmapFactory.decodeResource(resources, R.drawable.header_logo)

         notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             builder = Notification.Builder(this, channelId)
                .setLargeIcon(bitmap_logo)
                .setSmallIcon(R.drawable.mainlogo)
                .setContentText("Image downloaded")
                .setSubText("New message")
        } else {

             builder = Notification.Builder(this)
                .setLargeIcon(bitmap_logo)
                .setSmallIcon(R.drawable.mainlogo)
                .setContentText("New message")
                .setSubText("Image downloaded")
        }

        notificationManager.notify(1234, builder.build())*/



        cancel_btn.setOnClickListener {
            onBackPressed()
        }






    }

    fun next(uri:String,oS:Int,cS:Int,qu:Int,bitmap:Bitmap)
    {
        var iback=Intent(this,image_difference::class.java)
        iback.putExtra("uri",uri)
        iback.putExtra("oS",oS)
        iback.putExtra("cS",cS)
        iback.putExtra("qu",qu)
        iback.putExtra("w",bitmap.width)
        iback.putExtra("h",bitmap.height)


        startActivity(iback)
    }
}