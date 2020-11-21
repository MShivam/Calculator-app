package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var lastNumeric: Boolean = false
    var lastDot: Boolean = false
    //var equalFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onDigit(view: View){
        /*if(equalFlag) {
            onClear(view)
            equalFlag = false
        }*/
        textView.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View){
        textView.text = ""
        lastNumeric = false
        lastDot = false
    }

    fun onDecimal(view: View){
        if (lastNumeric && !lastDot /*&& !equalFlag*/){
            textView.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View){
        if (lastNumeric && !isOperatorAdded(textView.text.toString())){
            textView.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    fun onEqual(view: View){
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

                    textView.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())

                } else if (value.contains("+")){
                    val splitValue = value.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textView.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                } else if (value.contains("/")){
                    val splitValue = value.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textView.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())

                } else if (value.contains("*")){
                    val splitValue = value.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()){
                        one = prefix + one
                    }

                    textView.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                }
                //equalFlag = true

            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }

    private fun removeZeroAfterDot(result: String): String{
        var value = result
        if(result.contains(".0")){
            value = result.substring(0, result.length - 2)
        }
        return value
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if (value.startsWith("-")){
            false
        } else {
            value.contains("/") || value.contains("*") || value.contains("-") || value.contains("+")
        }
    }
}