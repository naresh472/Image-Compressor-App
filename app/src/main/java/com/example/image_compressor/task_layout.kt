package com.example.image_compressor

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.slider.Slider
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class task_layout : AppCompatActivity() {
    lateinit var bitmapnew:Bitmap
    lateinit var bitmap:Bitmap
     lateinit var radiogp:RadioGroup
     var total_image_size:Int = 0
    var costimaized_size:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_layout)

        val load1:ProgressBar=findViewById(R.id.load1)

        val task_layout:RelativeLayout=findViewById(R.id.task_layout)

        radiogp=findViewById(R.id.radiogp)
        var radio_auto:RadioButton=findViewById(R.id.auto)
        var customFileSize:RadioButton=findViewById(R.id.custom_size)
        var quality_auto:RadioButton=findViewById(R.id.quality_auto)
        var quality_custom:RadioButton=findViewById(R.id.quality_custom)
        var resolution_origional:RadioButton=findViewById(R.id.resolution_original)
        var resolution_custom:RadioButton=findViewById(R.id.resolution_custom)
        var customQuality:RadioButton=findViewById(R.id.Custom_quality)
        var tvResult:TextView=findViewById(R.id.tvResult)
        var imageSize:TextView=findViewById(R.id.image_size)
        var bytesize:TextView=findViewById(R.id.bytesize)
        var customSizeLayout:RelativeLayout=findViewById(R.id.task_custom_file_size)
        var qualityCustomLayout:RelativeLayout=findViewById(R.id.quality_custom_layout)
        var taskCustomQuality:RelativeLayout=findViewById(R.id.task_custom_quality)
        var taskCustomResolution:RelativeLayout=findViewById(R.id.task_custom_resoultion)
        var taskCustomWidthHeight:RelativeLayout=findViewById(R.id.resolution_custom_width_height)
        var imageWidth:AutoCompleteTextView=findViewById(R.id.image_width)
        var imageHeight:AutoCompleteTextView=findViewById(R.id.image_height)
        var count_image:TextView=findViewById(R.id.count_image)

        var compressed_btn:AppCompatButton=findViewById(R.id.compress_btn)


        var quality_per:TextView =findViewById(R.id.quality_per)


        val back_button:ImageView = findViewById(R.id.back_button)


        //Radio button selection

        radio_auto.setOnClickListener {
            customSizeLayout.visibility=View.GONE
            taskCustomQuality.visibility=View.GONE
            taskCustomResolution.visibility=View.GONE


        }



        customFileSize.setOnClickListener {

            customSizeLayout.visibility=View.VISIBLE
            taskCustomQuality.visibility=View.GONE
            taskCustomResolution.visibility=View.GONE
        }
        customQuality.setOnClickListener {
            customSizeLayout.visibility=View.GONE
            taskCustomQuality.visibility=View.VISIBLE
            taskCustomResolution.visibility=View.VISIBLE
        }

        //custom quality and resoulation radio button

        quality_auto.setOnClickListener {
            qualityCustomLayout.visibility=View.GONE

        }
        quality_custom.setOnClickListener {
            qualityCustomLayout.visibility=View.VISIBLE

            val quality_slider:Slider=findViewById(R.id.quality_slider)
            quality_slider.addOnChangeListener { slider, value, fromuser ->
                costimaized_size = value.toInt()
                val of_round =  (value * 100.0).roundToInt() /100.0
                quality_per.text=of_round.toString()+"%"
            }

        }

        //resolution and their radio button

        resolution_origional.setOnClickListener {
            taskCustomWidthHeight.visibility=View.GONE

        }

        resolution_custom.setOnClickListener {

            taskCustomWidthHeight.visibility=View.VISIBLE

        }



        // image get from acticity an deploy here

        var intent = intent
        var arrImageUri:ArrayList<String> = ArrayList<String>()
        val db = DBhelper(this, null)
       val cursor =db.getimage()
        while (cursor!!.moveToNext())
        {
            arrImageUri?.add(cursor.getString(0))
        }





        if (arrImageUri != null) {
            count_image.text = arrImageUri.size.toString()+" images"
            var total_size:Float = 0.0F

            for ( i  in 0..arrImageUri.size-1)
            {

                var new_byteArrayOutputStream = ByteArrayOutputStream()

                var new_uri: Uri = arrImageUri.get(i).toUri()
                bitmapnew = MediaStore.Images.Media.getBitmap(contentResolver, new_uri)
                bitmapnew.compress(Bitmap.CompressFormat.JPEG, 100, new_byteArrayOutputStream)

                var img_byte = new_byteArrayOutputStream.toByteArray()
                var size_KB = img_byte.size / 1024
                total_size += size_KB


            }
            total_image_size = (total_size / 100).roundToInt()
            if(total_size > 1000)
            {
                var extra:Float = (total_size / 1024)
                val roundoff =  (extra * 100.0).roundToInt() /100.0

                imageSize.text = "( "+ roundoff.toString()+ " MB )"


            }
            else
            {
                imageSize.text = "( "+ total_size.toInt().toString()+ " KB )"

            }




        }


        val w:String=bitmapnew.width.toString()
        imageWidth.setText(w)

        val he:String=bitmapnew.height.toString()
        imageHeight.setText(he)


       /* if (byte != null) {
            bitmapnew = BitmapFactory.decodeByteArray(byte,0,byte.size)

        }

        bitmap = bitmapnew
        var img_size = byte?.size

        if (img_size != null) {
            img_size = img_size / (1024)
        }

        var size_image = "( "+img_size.toString() + " KB )"
        imageSize.text=size_image*/

        // slider and kb converter

        val normalContinuousSlider: Slider = findViewById(R.id.normalContinuousSlider)
        normalContinuousSlider.addOnChangeListener { slider, value, fromUser ->
            val of_round =  (value * 100.0).roundToInt() /100.0
            tvResult.text = of_round.toString()
            costimaized_size = value.toInt()

                var tot_size:Double = total_image_size.toDouble()

                if (value >= 1 )
                {
                    if(value <= 92) {
                        var x = value * 0.3
                        tot_size = (tot_size * x)
                    }
                    else
                    {
                        var x = value * 0.8
                        tot_size = (tot_size * x)
                    }

                    if(tot_size > 1000)
                    {
                        var extra = tot_size / 1024
                        val roundoff =  (extra * 100.0).roundToInt() /100.0
                        bytesize.text = roundoff.toString()+ " MB"
                    }
                    else
                    {
                        bytesize.text =  tot_size.toInt().toString()+ " KB"
                    }
                }




            }


        compressed_btn.setOnClickListener {
            load1.visibility=View.VISIBLE
            task_layout.visibility=View.GONE

             var inext = Intent(this,img_result::class.java)

            if(radio_auto.isChecked)
            {
                inext.putExtra("code","150")
                inext.putExtra("qu","10")
            }
            else if (customFileSize.isChecked)
            {
                inext.putExtra("code","150")
                inext.putExtra("qu",costimaized_size.toString())
            }
            else {
                inext.putExtra("code","1500")
                if(quality_auto.isChecked) {
                    inext.putExtra("qu", "10")
                }
                else
                {
                    inext.putExtra("qu",costimaized_size.toString())

                }

                if(resolution_custom.isChecked)
                {
                    if (imageWidth.text.equals("") && imageHeight.text.equals(""))
                    {
                        Toast.makeText(this,"please enter image Width and Height",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        inext.putExtra("width", imageWidth.text.toString())
                        inext.putExtra("height", imageHeight.text.toString())
                    }

                }


            }

            startActivity(inext)
            Handler().postDelayed(
                {
                    load1.visibility=View.GONE
                    task_layout.visibility=View.VISIBLE
                },3000
            )
        }





        back_button.setOnClickListener {
            onBackPressed()
        }
    }
}