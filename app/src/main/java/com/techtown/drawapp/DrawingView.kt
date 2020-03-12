package com.techtown.drawapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.util.Log

import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.*
import androidx.core.content.ContextCompat

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DrawingView : View, View.OnTouchListener {

    var points : ArrayList<PaintPoint> =  ArrayList<PaintPoint>()
    var color  : Int
        get() {
            return Color.BLACK
        }
        set(value) {
            this.color = Color.BLACK
        }

    var lineWith : Float
        get() {
            return 1f
        }
        set(value) {
            this.lineWith = 1f
        }


    constructor(context: Context) : super(context){
        this.setOnTouchListener(this)
    }

    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        this.setOnTouchListener(this)
    }

    constructor(context: Context, attrs : AttributeSet, defStyleAttr : Int) : super(context, attrs, defStyleAttr){
        this.setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_MOVE -> {
                var p = Paint()
                p.setColor(color)
                p.strokeWidth = lineWith
                points.add(PaintPoint(event.getX(), event.getY(),true, p))
                invalidate()
            }
            MotionEvent.ACTION_UP -> {}
            MotionEvent.ACTION_DOWN -> {
                points.add(PaintPoint(event.getX(), event.getY(), false, null))
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for(i in 1..points.size-1) {
            if(!points.get(i).isDraw)continue

            canvas?.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, points.get(i).paint)
        }
    }

    fun reset() {
        points.clear()
        invalidate()
    }

    fun save(context : Context) {
        this.isDrawingCacheEnabled = true
        var screenshot = this.drawingCache

        var formatter =  SimpleDateFormat("yyyyMMddHHmmss")
        var currentDate = Date()
        var dateString = formatter.format(currentDate)
        var fileName = dateString + "_drawImage.png"

        try{
            if((ContextCompat.checkSelfPermission(context as MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
             or (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
                if(shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(context, "Read/Write external storage", Toast.LENGTH_LONG).show()
                }

                requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 2)

            }else{
                var file = File(Environment.getExternalStorageDirectory(), fileName)
                if(file.createNewFile()) Log.d("저장 파일 생성", file.name)
                var outStream = FileOutputStream(file) as OutputStream
                screenshot.compress(Bitmap.CompressFormat.PNG, 100, outStream)
                outStream.close()
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    var mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    var contentUri = Uri.fromFile(file)
                    mediaScanIntent.setData(contentUri)
                    context.sendBroadcast(mediaScanIntent)
                }else{
                    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())))
                }
                Toast.makeText(context.applicationContext, "저장완료", Toast.LENGTH_LONG).show()
            }
        }catch (e : Exception) {
            e.printStackTrace()
        }
        this.isDrawingCacheEnabled = false
    }




}