package com.amrabdelhamiddiab.readqrcodefromgallery2

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.Surface
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var button2: Button
    private lateinit var bitmap: Bitmap
    private lateinit var image: InputImage
    private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.qrcodereal)
        Log.d(TAG, "bitmap...........      $bitmap")
        image = InputImage.fromBitmap(bitmap, 0)
        Log.d(TAG, "image................................$image")
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            ).build()
        scanner = BarcodeScanning.getClient(options)
        Log.d(TAG, "scanner.............................................$scanner")
        //  3. Get an instance of BarcodeScanner

        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        button.setOnClickListener {
            getTask().addOnSuccessListener { barcodes ->
                Log.d(TAG, barcodes.size.toString())
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT -> {
                            Log.d(
                                TAG,
                                "Barcode.TYPE_TEXT......................${barcode.displayValue.toString()}"
                            )
                        }
                        Barcode.TYPE_URL -> {
                            Log.d(
                                TAG,
                                "Barcode.TYPE_URL......................${barcode.displayValue.toString()}   ."
                            )
                            // Log.d(TAG, "Barcode.TYPE_UNKNOWN.......................")
                        }
                    }
                }

            }
                .addOnFailureListener {
                    Log.d(TAG, "Failure")
                }.addOnCanceledListener {
                    Log.d(TAG, "Canceled")
                }

        }
    }

    //**************************

    //****************************


    private fun getTask() = scanner.process(image)

    companion object {
        const val TAG = "MainActivity"
    }
}