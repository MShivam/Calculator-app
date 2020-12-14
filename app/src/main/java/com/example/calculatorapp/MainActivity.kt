package com.example.calculatorapp

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false
    private var equalFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClear(view: View){
        clearAnimation()
        clearTextView()
    }

    fun onBack(view: View){
        if (textView.text.toString().isNotEmpty()) {
            var value = textView.text.toString()
            val lastC = value[value.length - 1]
            if (value.substring(0, value.length - 1).isEmpty()) {
                setTextView()
                clearTextView()
            } else {
                setTextView()
                textView.text = value.substring(0, value.length - 1)
                value = textView.text.toString()

                if (lastC == '.') {
                    lastNumeric = true
                    lastDot = false
                } else if (value[value.length - 1] == '.') {
                    lastNumeric = false
                    lastDot = true
                } else if (value[value.length - 1].isDigit()) {
                    lastNumeric = true
                    lastDot = isOperatorAdded(textView.text.toString())
                } else {
                    lastNumeric = false
                    lastDot = textView.text.toString().contains(".")
                }
                equalFlag = false
            }
            textViewResult.text = ""
        }
    }

    fun onDigit(view: View){
        textView.append((view as Button).text)
        if(equalFlag) {
            setTextView()
            equalFlag = false
            clearTextView()
            textView.append(view.text)
        }
        if (isOperatorAdded(textView.text.toString())) {
            lastNumeric = true
            equal()
            equalFlag = false
            setTextView()
        }
        lastNumeric = true
    }

    fun onDecimal(view: View){
        if(equalFlag || textView.text.toString().isEmpty()) {
            setTextView()
            clearTextView()
            textView.text = "0."
            lastDot = true
        }
        if (lastNumeric && !lastDot){
            textView.append(".")
            lastNumeric = false
            lastDot = true
        } else if (!lastNumeric&&!lastDot){
            textView.append("0.")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View){
        val value = textView.text.toString()
        val operator = (view as Button).text
        if (isOperatorAdded(value) && lastNumeric) {
            equal()
            textView.text = roundOf(textViewResult.text.toString())
            setTextView()
            textView.append(view.text)
            lastNumeric = false
            lastDot = false
            equalFlag = false
        }
        if (isOperatorAdded(textView.text.toString()) && !lastNumeric && operator == "−") {
            if (textView.text.toString()[textView.text.toString().length-1] == '-')
                return
            if (textView.text.toString()[textView.text.toString().length-1] != '−')
                textView.append("-")
        }
        if (value.isEmpty() && operator == "−") {
            textView.text = "-"
        } else {
            if (equalFlag) {
                textView.text = roundOf(textViewResult.text.toString())
                setTextView()
                equalFlag = false
            }
            if (lastNumeric && !isOperatorAdded(value)) {
                textView.append(view.text)
                lastNumeric = false
                lastDot = false
            }
        }

    }

    fun onResult(view: View){
        var value = textView.text.toString()
        if (value == "-" || value[value.length-1] == '-' || value[value.length-1] == '−')
            return
        equal()
        animateTextSize()
        animateTextSizeResult()
    }

    private fun equal(){
        var value = textView.text.toString()

        if (value[value.length - 1] == '%')
            lastNumeric = true

        if(lastNumeric){
            try {
                if (value.contains("−")){
                    val splitValue = value.split("−")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    textViewResult.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                    settextViewResult()

                } else if (value.contains("+")){
                    val splitValue = value.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    textViewResult.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                    settextViewResult()

                } else if (value.contains("÷")){
                    val splitValue = value.split("÷")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    textViewResult.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    settextViewResult()

                } else if (value.contains("×")){
                    val splitValue = value.split("×")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    textViewResult.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                    settextViewResult()

                } else if (value.contains("%")){
                    val splitValue = value.split("%")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(two.isEmpty()){
                        two = "1"
                    }
                    textViewResult.text = removeZeroAfterDot(((one.toDouble() * two.toDouble()) / 100).toString())
                    settextViewResult()
                } else {
                    textViewResult.text = textView.text.toString()
                    settextViewResult()
                }
                equalFlag = true

            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if (result.toDouble() == 0.0)
            value = "0"
        else if((result[result.length - 2] == '.') && (result[result.length - 1] == '0')){
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if (value.startsWith("−")){
            false
        } else {
            value.contains("÷") || value.contains("×") || value.contains("−") || value.contains("+") || value.contains(
                "%"
            )
        }
    }

    private fun roundOf(result: String): String{
        var value = result
        if(result.contains(".")){
            value = ((result.toDouble() * 100).roundToInt().toDouble()/100).toString()
        }
        return value
    }

    private fun clearTextView(){
        textView.text = ""
        textViewResult.text = ""
        textView.textSize = 45F
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        lastNumeric = false
        lastDot = false
        equalFlag = false
    }

    private fun setTextView(){
        textViewResult.textSize = 30F
        textViewResult.typeface = Typeface.DEFAULT
        textViewResult.setTextColor(Color.parseColor("#989ea8"))
        textView.textSize = 45F
        textView.typeface = Typeface.DEFAULT
        textView.setTextColor(Color.parseColor("#FFFFFF"))
    }

    private fun settextViewResult(){
        textView.textSize = 30F
        textView.typeface = Typeface.DEFAULT
        textView.setTextColor(Color.parseColor("#989ea8"))
        textViewResult.textSize = 45F
        textViewResult.typeface = Typeface.DEFAULT
        textViewResult.setTextColor(Color.parseColor("#FFFFFF"))
    }

    private fun animateTextSize(){
        val startSize = 45F
        val endSize = 30F
        val animationDuration = 300 // Animation duration in ms
        val animator = ValueAnimator.ofFloat(startSize, endSize)
        animator.duration = animationDuration.toLong()

        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            textView.textSize = animatedValue
        }
        animator.start()
    }

    private fun animateTextSizeResult(){
        textViewResult.typeface = Typeface.DEFAULT_BOLD

        val startSize = 30F
        val endSize = 45F
        val animationDuration = 300 // Animation duration in ms
        val animator = ValueAnimator.ofFloat(startSize, endSize)
        animator.duration = animationDuration.toLong()

        animator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Float
            textViewResult.textSize = animatedValue
        }
        animator.start()
    }

    private fun clearAnimation() {
        if (textView.text.toString().isNotEmpty() || textViewResult.text.toString().isNotEmpty()) {
            val animation = AlphaAnimation(0f, 1f)
            animation.duration = 150
            animation.startOffset = 150
            animation.fillAfter = true
            textView.startAnimation(animation)
            textViewResult.startAnimation(animation)
        }
    }
}