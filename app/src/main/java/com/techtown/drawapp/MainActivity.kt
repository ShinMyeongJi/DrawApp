package com.techtown.drawapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var drawingView: DrawingView
    lateinit var resetButton : Button
    lateinit var saveButton : Button

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)

        resetButton = findViewById(R.id.resetBtn)
        saveButton = findViewById(R.id.saveBtn)

        resetButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                drawingView.reset()
            }
        })

        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                drawingView.save(this@MainActivity)
            }
        })

        var permissions : Array<String>
                = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        //checkPermission(permissions)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("WrongConstant")
    fun checkPermission() {
        if((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) or
            (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){

            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_LONG).show()
            }

            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }else{

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
