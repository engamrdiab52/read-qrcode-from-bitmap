package com.amrabdelhamiddiab.readqrcodefromgallery2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var button2: Button
    private lateinit var bitmap: Bitmap
    private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)


        button.setOnClickListener {
            openSomeActivityForResult()
        }
    }

    private fun openSomeActivityForResult() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val imageUri = data?.data
                // here i have the uri and will create the input image
                try {
                    val inputImage = imageUri?.let { InputImage.fromFilePath(this, it) }!!
                    getQrCodeValue(inputImage)
                } catch (e: IOException) {
                    Log.d(TAG, e.message.toString())
                }
            }
        }

    private fun getQrCodeValue(inputImage: InputImage) {
        scanner.process(inputImage).addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                when (barcode.valueType) {
                    Barcode.TYPE_TEXT -> {
                        Log.d(
                            TAG,
                            "Barcode.TYPE_TEXT......................${barcode.displayValue.toString()}"
                        )
                    }
                }
            }
        }.addOnFailureListener {
            Log.d(TAG, "Failure")
        }.addOnCanceledListener {
            Log.d(TAG, "Canceled")
        }
    }
    companion object {
        const val TAG = "MainActivity"
    }
}