package com.bhatia.gstcalculatorusingkotlin

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bhatia.gstcalculatorusingkotlin.databinding.ActivityGstresultBinding

/**
 * Activity class that displays the GST calculation results.
 * This activity shows the net price, GST amount, CGST, SGST, and total price.
 */
class GSTResult : AppCompatActivity() {
    // View binding for the activity
    private lateinit var binding: ActivityGstresultBinding

    @Suppress("DEPRECATION")
    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityGstresultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the GST calculation data from the intent extras
        val heading = intent.getStringExtra("heading")
        val netPrice = intent.getStringExtra("netPrice")
        val gst = intent.getStringExtra("gst")
        val cgst = intent.getStringExtra("cgst")
        val totalPrice = intent.getStringExtra("totalPrice")

        // Set the retrieved data to the respective TextViews
        binding.TxtHeading.text = heading
        binding.TxtNetPrice.text = netPrice
        binding.TxtGST.text = gst
        binding.TxtSGST.text = cgst
        binding.TxtCGST.text = cgst
        binding.TxtTotalPrice.text = totalPrice

        // Set up the back button to return to the previous activity
        binding.BtnBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}