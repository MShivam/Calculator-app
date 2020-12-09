package com.example.calculatorapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    var lastNumeric: Boolean = false
    var lastDot: Boolean = false
    var equalFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClear(view: View){
        textView.text = ""
        textViewR.text = ""
        textView.textSize = 45F
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        lastNumeric = false
        lastDot = false
        equalFlag = false
    }

    fun onBack(view: View){
        if (textView.text.toString().substring(0, textView.text.toString().length-1).isEmpty()){
            setTextView()
            equalFlag = false
            onClear(view)
        }
        else {
            setTextView()
            textView.text =
                textView.text.toString().substring(0, textView.text.toString().length - 1)

            if (textView.text.toString()[textView.text.toString().length - 1].isDigit()) {
                lastNumeric = true
                lastDot = isOperatorAdded(textView.text.toString())
            } else if (textView.text.toString()[textView.text.toString().length - 1] == '.') {
                lastNumeric = false
                lastDot = true
            } else {
                lastNumeric = true
                lastDot = textView.text.toString().contains(".")
            }
            equalFlag = false
        }
    }

    fun onDigit(view: View){
        if(equalFlag) {
            textView.text = roundOf(textViewR.text.toString())
            setTextView()
            equalFlag = false
            onClear(view)
        }
        textView.append((view as Button).text)
        lastNumeric = true
    }

    fun onDecimal(view: View){
        if(equalFlag || textView.text.toString().isEmpty()) {
            setTextView()
            onClear(view)
            textView.text = "0."
            lastDot = true
        }
        if (lastNumeric && !lastDot){
            textView.append(".")
            lastNumeric = false
            lastDot = true
        } else {
            textView.append("0.")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View){
        if (textView.text.toString().isEmpty() && (view as Button).text == "-") {
            textView.text = "-"
        } else {
            if (equalFlag) {
                textView.text = roundOf(textViewR.text.toString())
                setTextView()
                equalFlag = false
            }
            if (lastNumeric && !isOperatorAdded(textView.text.toString())) {
                textView.append((view as Button).text)
                lastNumeric = false
                lastDot = false
            }
        }
    }

    fun onEqual(view: View){
        if (textView.text.toString()[textView.text.toString().length - 1] == '%')
            lastNumeric = true

        if(lastNumeric){
            var value = textView.text.toString()
            var prefix = ""
            try {
                if (value.startsWith("-")){
                    prefix = "-"
                    value = value.substring(1)
                }

                if (value.contains("-")){
                    val splitValue = value.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textViewR.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
                    setTextViewR()

                } else if (value.contains("+")){
                    val splitValue = value.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textViewR.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
                    setTextViewR()

                } else if (value.contains("/")){
                    val splitValue = value.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textViewR.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                    setTextViewR()

                } else if (value.contains("*")){
                    val splitValue = value.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textViewR.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                    setTextViewR()

                } else if (value.contains("%")){
                    val splitValue = value.split("%")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(two.isEmpty()){
                        two = "1"
                    }

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textViewR.text = removeZeroAfterDot(((one.toDouble() * two.toDouble())/100).toString())
                    setTextViewR()
                } else {
                    textViewR.text = textView.text.toString()
                    setTextViewR()
                }
                equalFlag = true
                //lastNumeric = false

            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if (result.toDouble() == 0.0)
            value = "0"
        else if((result[result.length -2] == '.') && (result[result.length -1] == '0')){
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if (value.startsWith("-")){
            false
        } else {
            value.contains("/") || value.contains("*") || value.contains("-") || value.contains("+") || value.contains("%")
        }
    }

    private fun roundOf(result: String): String{
        var value = result
        if(result.contains(".")){
            value = ((result.toDouble() * 100).roundToInt().toDouble()/100).toString()
        }
        return value
    }

    private fun setTextView(){
        textViewR.textSize = 30F
        textViewR.typeface = Typeface.DEFAULT
        textViewR.setTextColor(Color.parseColor("#989ea8"))
        textView.textSize = 45F
        textView.typeface = Typeface.DEFAULT_BOLD
        textView.setTextColor(Color.parseColor("#FFFFFF"))

    }

    private fun setTextViewR(){
        textView.textSize = 30F
        textView.typeface = Typeface.DEFAULT
        textView.setTextColor(Color.parseColor("#989ea8"))
        textViewR.textSize = 45F
        textViewR.typeface = Typeface.DEFAULT_BOLD
        textViewR.setTextColor(Color.parseColor("#FFFFFF"))
    }
}