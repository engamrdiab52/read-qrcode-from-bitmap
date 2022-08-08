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
    private lateinit var inputImage: InputImage
    private lateinit var scanner: BarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            ).build()
        scanner = BarcodeScanning.getClient(options)

        //Get the Bitmap
 /*       bitmap = BitmapFactory.decodeResource(resources, R.drawable.qrcodereal)
        Log.d(TAG, "bitmap...........      $bitmap")
*/

        //Get the Input Image
     /*   inputImage = InputImage.fromBitmap(bitmap, 0)
        Log.d(TAG, "image................................$inputImage")
*/



        button.setOnClickListener {
            openSomeActivityForResult()


            //i will call it from inside activity result

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
                    inputImage = imageUri?.let { InputImage.fromFilePath(this, it) }!!
                    getQrCodeValue()
                } catch (e: IOException) {
                    Log.d(TAG, e.message.toString())
                }

                //imageView.setImageURI(imageUri)
            }
        }
    private fun getQrCodeValue() {
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

    private fun getTask() = scanner.process(inputImage)

    companion object {
        const val TAG = "MainActivity"
    }
}