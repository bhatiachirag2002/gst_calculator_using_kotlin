package com.bhatia.gstcalculatorusingkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bhatia.gstcalculatorusingkotlin.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

/**
 * MainActivity is the main entry point of the application which handles user interactions
 * with the calculator interface, including arithmetic operations and GST calculations.
 */
class MainActivity : AppCompatActivity() {
    // View binding for the activity
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up listeners for all buttons
        setUpButtonListeners()
    }

    /**
     * Sets up listeners for all buttons in the layout.
     */
    private fun setUpButtonListeners() {
        binding.apply {
            btnAllClear.setOnClickListener { allClear() }
            btnClear.setOnClickListener { clearLastCharacter() }
            // Number Buttons
            btnOne.setOnClickListener { appendToInput("1") }
            btnTwo.setOnClickListener { appendToInput("2") }
            btnThree.setOnClickListener { appendToInput("3") }
            btnFour.setOnClickListener { appendToInput("4") }
            btnFive.setOnClickListener { appendToInput("5") }
            btnSix.setOnClickListener { appendToInput("6") }
            btnSeven.setOnClickListener { appendToInput("7") }
            btnEight.setOnClickListener { appendToInput("8") }
            btnNine.setOnClickListener { appendToInput("9") }
            btnZero.setOnClickListener { appendToInput("0") }
            btnDecimal.setOnClickListener { appendDecimal() }
            // Operator Buttons
            btnAddition.setOnClickListener { appendOperator("+") }
            btnSubtract.setOnClickListener { appendOperator("-") }
            btnDivide.setOnClickListener { appendOperator("/") }
            btnMultiply.setOnClickListener { appendOperator("×") }
            btnEqual.setOnClickListener { evaluateExpression() }
            // Set up listeners for all GST Buttons
            setUpGSTButtons()
        }
    }

    /**
     * Evaluates the current mathematical expression and displays the result.
     */
    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun evaluateExpression() {
        var currentText = binding.TxtInput.text.toString()
        if (isLastCharOperator(currentText)) {
            currentText = currentText.dropLast(1)
        }
        try {
            // Replace custom multiplication symbol with standard one
            currentText = currentText.replace('×', '*')
            val expression = ExpressionBuilder(currentText).build()
            val result = expression.evaluate()

            var resultString = if (result.toString().contains('E')) {
                String.format("%.5E", result)
            } else {
                result.toString()
            }
            if (resultString.endsWith(".0")) {
                resultString = resultString.substring(0, resultString.length - 2)
            }
            binding.TxtResult.text = "= $resultString"
        } catch (e: Exception) {
            binding.TxtResult.text = "Error"
        }
        scrollToBottom()
    }

    /**
     * Sets up listeners for all GST calculation buttons.
     */
    private fun setUpGSTButtons() {
        binding.apply {
            btn3Perc.setOnClickListener { calculateGst(0.03, true) }
            btn5Perc.setOnClickListener { calculateGst(0.05, true) }
            btn12Perc.setOnClickListener { calculateGst(0.12, true) }
            btn18Perc.setOnClickListener { calculateGst(0.18, true) }
            btn28Perc.setOnClickListener { calculateGst(0.28, true) }
            btnMin3Perc.setOnClickListener { calculateGst(0.03, false) }
            btnMin5Perc.setOnClickListener { calculateGst(0.05, false) }
            btnMin12Perc.setOnClickListener { calculateGst(0.12, false) }
            btnMin18Perc.setOnClickListener { calculateGst(0.18, false) }
            btnMin28Perc.setOnClickListener { calculateGst(0.28, false) }
        }
    }

    /**
     * Calculates GST based on the given rate and whether it is an addition or subtraction.
     *
     * @param gstRate The GST rate to be applied.
     * @param isAddition True if GST is to be added, false if subtracted.
     */
    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun calculateGst(gstRate: Double, isAddition: Boolean) {
        var currentText = binding.TxtInput.text.toString()
        if (currentText.isEmpty()) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (isLastCharOperator(currentText)) {
            currentText = currentText.dropLast(1)
        }

        intent = Intent(this, GSTResult::class.java)
        try {
            currentText = currentText.replace('×', '*')
            val expression = ExpressionBuilder(currentText).build()
            val netPrice = expression.evaluate()

            if (netPrice.toString().contains('E')) {
                throw IllegalArgumentException("Scientific notation detected")
            }

            val gst = netPrice * gstRate
            val cgst = gst / 2
            val totalPrice = if (isAddition) netPrice + gst else netPrice - gst

            intent.putExtra("netPrice", String.format("%.2f", totalPrice))
            intent.putExtra("gst", String.format("%.2f", gst))
            intent.putExtra("totalPrice", String.format("%.2f", netPrice))
            intent.putExtra("cgst", String.format("%.2f", cgst))
        } catch (e: Exception) {
            intent.putExtra("totalPrice", "Error")
            intent.putExtra("cgst", "Error")
            intent.putExtra("gst", "Error")
            intent.putExtra("netPrice", "Error")
        }
        intent.putExtra("heading", if (isAddition) "Excl. GST" else "Incl . GST")
        startActivity(intent)
    }

    /**
     * Clears all input and result text fields.
     */
    @SuppressLint("SetTextI18n")
    private fun allClear() {
        binding.TxtInput.text = ""
        binding.TxtResult.text = ""
    }

    /**
     * Clears the last character in the input field.
     */
    private fun clearLastCharacter() {
        val currentTxt = binding.TxtInput.text.toString()
        if (currentTxt.isNotEmpty()) {
            binding.TxtInput.text = currentTxt.substring(0, currentTxt.length - 1)
            scrollToBottom()
        } else {
            allClear()
        }
        scrollToBottom()
    }

    /**
     * Appends a number to the input field.
     *
     * @param value The number to append.
     */
    private fun appendToInput(value: String) {
        binding.TxtInput.append(value)
        scrollToBottom()
    }

    /**
     * Appends an operator to the input field.
     *
     * @param operator The operator to append.
     */
    @SuppressLint("SetTextI18n")
    private fun appendOperator(operator: String) {
        val currentText = binding.TxtInput.text.toString()
        if (currentText.isNotEmpty() && !isLastCharOperator(currentText)) {
            binding.TxtInput.append(operator)
        } else if (isLastCharOperator(currentText)) {
            binding.TxtInput.text = currentText.dropLast(1) + operator
        }
        scrollToBottom()
    }

    /*
     * Appends a decimal point to the input field, ensuring only one decimal per number.
     */
    private fun appendDecimal() {
        val currentText = binding.TxtInput.text.toString()
        val lastNumber = currentText.split('+', '-', '×', '/').lastOrNull()

        if (lastNumber != null && !lastNumber.contains('.')) {
            appendToInput(".")
        }
    }

    /**
     * Checks if the last character in the text is an operator.
     *
     * @param text The text to check.
     * @return True if the last character is an operator, false otherwise.
     */
    private fun isLastCharOperator(text: String): Boolean {
        return text.lastOrNull() in listOf('+', '-', '×', '/')
    }

    /**
    Scrolls the ScrollView to the bottom.
     */
    private fun scrollToBottom() {
        binding.scrollView.post {
            binding.scrollView.fullScroll(android.view.View.FOCUS_DOWN)
        }
    }
}