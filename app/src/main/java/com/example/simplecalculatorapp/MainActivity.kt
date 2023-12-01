package com.example.simplecalculatorapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.view.children
import com.example.simplecalculatorapp.databinding.ActivityMainBinding

private var firstNumber = ""
private var currentNumber = ""
private var currentOperator = ""
private var result = ""

// TODO: Implement calculateResult fun to also understand float numbers with no errors
//  TODO: Also make sure operators have "=" function when pressed twice!!

class MainActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.LayoutMain.children.filterIsInstance<Button>().forEach{ button ->
            button.setOnClickListener{
                val buttonText = button.text.toString() // No idea if toString is necessary
                when {
                    buttonText.matches(Regex("[0-9]")) -> {
                        if (currentOperator.isEmpty())
                        {
                            firstNumber+=buttonText
                            bindingClass.tvResult.text = firstNumber
                        }
                        else {
                            currentNumber+=buttonText
                            bindingClass.tvResult.text = currentNumber
                        }

                    }
                    buttonText.matches(Regex("[+—*/]")) -> {
                        currentNumber = ""
                        if (bindingClass.tvResult.text.toString().isNotEmpty() && firstNumber.isNotEmpty())
                        {
                            currentOperator = buttonText
                            bindingClass.tvResult.text = "0" // No idea why would we have 0 here
                        }
                        else if (firstNumber.isEmpty()) {
                            currentOperator = buttonText
                            firstNumber = "0"
                            bindingClass.tvResult.text = "0" // this is newest my addition ELSE block
                        }
                    }
                    buttonText == "=" -> {
                        if (currentNumber.isNotEmpty() && currentOperator.isNotEmpty())
                        {
                            bindingClass.tvFormula.text = "$firstNumber$currentOperator$currentNumber"
                            result = calculatorResult(firstNumber, currentNumber, currentOperator)
                            firstNumber = result
                            bindingClass.tvResult.text = result
                        }
                    }
                    buttonText == "C" -> {
                        firstNumber = ""
                        currentNumber = ""
                        currentOperator = ""
                        result = ""
                        bindingClass.tvResult.text = "0"
                        bindingClass.tvFormula.text = ""
                    }

                    buttonText == "." -> {
                        if (currentOperator.isEmpty())
                        {
                            if(! firstNumber.contains("."))
                            {
                                if (firstNumber.isEmpty()) firstNumber += "0$buttonText"
                                else firstNumber += buttonText // Тут можно поменять текст кнопки на точку!
                                bindingClass.tvResult.text = firstNumber
                            }
                        }
                        else
                        {
                            if (! currentNumber.contains("."))
                            {
                                if (currentNumber.isEmpty()) currentNumber += "0$buttonText"
                                else currentNumber += buttonText
                                bindingClass.tvResult.text = currentNumber
                            }
                        }
                    }

                    buttonText == "%" -> {

                        if (currentNumber.isEmpty())
                        {
                            if (firstNumber.isEmpty() || firstNumber == "0") firstNumber += ""
                            else {
                                val number = firstNumber.toDouble()
                                firstNumber = (number / 100).toString()
                                bindingClass.tvResult.text = firstNumber
                            }
                        }
                        else
                        {
                            val number = currentNumber.toDouble()
                            currentNumber = (number / 100).toString()
                            bindingClass.tvResult.text = currentNumber
                        }

                    }
                    buttonText == "±" -> {
                        if (currentNumber.isEmpty())
                        {
                            if (firstNumber.isEmpty()) {
                                firstNumber = ""
                            }
                            else {
                                val number = firstNumber.toDouble()
                                firstNumber = convertToNegative(number).toString()
                                bindingClass.tvResult.text = firstNumber
                            }
                        }
                        else
                        {
                            val number = currentNumber.toDouble()
                            currentNumber = convertToNegative(number).toString()
                            bindingClass.tvResult.text = currentNumber
                        }
                    }
                }
            }
        }

    }
//FUNCTION for result

    fun convertToNegative(number:Double): Any {
        if (number == number.toInt().toDouble()) {
            return -number.toInt()
        }
        else {
            return -number
        }
    }

    fun calculatorResult(firstNumber:String,currentNumber:String,currentOperator:String):String {
        val num1 = firstNumber.toDouble()
        val num2 = currentNumber.toDouble()

        val result = when (currentOperator) {
            "+" -> num1 + num2
            "—" -> num1 - num2
            "/" -> num1 / num2
            "*" -> num1 * num2
            else -> return ""
        }

        return if (num1 % 1 == 0.0 && num2 % 1 == 0.0 && result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            result.toString()
        }
    }
}