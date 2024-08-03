package com.example.image_compressor

import android.net.Uri

data class status_data(val imgUri: String,val original_imgSize:Int,val compress_imgSize:Int,val img_byte:ByteArray,val img_width:Int,val img_height:Int)
