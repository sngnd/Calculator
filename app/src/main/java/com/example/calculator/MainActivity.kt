package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import java.lang.ArithmeticException
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private var isEqualPressed: Boolean = false
    private val inputTvKey = "NAME_VARIABLE"
    private val resultTvKey = "TEXTVIEW_TEXT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle){
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(inputTvKey, tvInput.text.toString())
        savedInstanceState.putString(resultTvKey, tvResult.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val input = savedInstanceState.getString(inputTvKey)
        val result = savedInstanceState.getString(resultTvKey)
        tvInput.text = input
        tvResult.text = result
    }

    private fun initViews() {
        btnOne.setOnClickListener(clickListener)
        btnTwo.setOnClickListener(clickListener)
        btnThree.setOnClickListener(clickListener)
        btnFour.setOnClickListener(clickListener)
        btnFive.setOnClickListener(clickListener)
        btnSix.setOnClickListener(clickListener)
        btnSeven.setOnClickListener(clickListener)
        btnEight.setOnClickListener(clickListener)
        btnNine.setOnClickListener(clickListener)
        btnZero.setOnClickListener(clickListener)
        btnDivide.setOnClickListener(clickListener)
        btnDelete.setOnClickListener(clickListener)
        btnDelete.setOnLongClickListener{
            onClearPressed()
            true
        }
        btnMultiply.setOnClickListener(clickListener)
        btnAdd.setOnClickListener(clickListener)
        btnClear.setOnClickListener(clickListener)
        btnClosedBracket.setOnClickListener(clickListener)
        btnOpenBracket.setOnClickListener(clickListener)
        btnSubtract.setOnClickListener(clickListener)
        btnDot.setOnClickListener(clickListener)
        btnEquals.setOnClickListener(clickListener)
        btnE?.setOnClickListener(clickListener)
        btnPi?.setOnClickListener(clickListener)
        btnLn?.setOnClickListener(clickListener)
        btn1DivideX?.setOnClickListener(clickListener)
        btnLg?.setOnClickListener(clickListener)
        btnSin?.setOnClickListener(clickListener)
        btnCos?.setOnClickListener(clickListener)
        btnTan?.setOnClickListener(clickListener)
        btnXToTheY?.setOnClickListener(clickListener)
        btnRoot?.setOnClickListener(clickListener)
    }

    private val clickListener: View.OnClickListener = View.OnClickListener { view ->

        val text = (view as Button).text.toString()
        when (view.id) {
            R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine, R.id.btnZero, R.id.btnPi, R.id.btnE -> onDigitPressed(
                text
            )
            R.id.btnDot -> onDotPressed(text)
            R.id.btnAdd, R.id.btnSubtract, R.id.btnDivide, R.id.btnMultiply, R.id.btn1DivideX -> onOperationPressed(
                text
            )
            R.id.btnClear -> onClearPressed()
            R.id.btnDelete -> onDeletePressed()
            R.id.btnEquals -> onEqualsPressed()
            R.id.btnClosedBracket, R.id.btnOpenBracket -> onBracketPressed(text)
            R.id.btnLg, R.id.btnLn, R.id.btnSin, R.id.btnCos, R.id.btnTan -> onComplexOperationPressed(text)
            R.id.btnXToTheY, R.id.btnRoot -> onRootOperationPressed(text)
        }
    }

    private fun onRootOperationPressed(text: String) {
        if (isLastNumeric() || tvInput.text.last()==')') {
            if (isEqualPressed) {
                tvInput.text = tvResult.text
                tvResult.text = ""
                isEqualPressed = false
            }
            when (text) {
                "x^y" -> tvInput.append("^")
                "√x" -> tvInput.append("^(1/2)")
            }
        }
    }

    private fun onDigitPressed(text: String) {
        tvInput.append(text)
    }

    private fun onDotPressed(text: String) {
        if (!isLastDot() && isLastNumeric() && !doesNumberContainDot()) {
            tvInput.append(text)
        }
    }

    private fun onComplexOperationPressed(text: String) {
        if (!isLastDot() && !isLastNumeric()) {
            if (isEqualPressed) {
                tvInput.text = tvResult.text
                tvResult.text = ""
                isEqualPressed = false
            }
            tvInput.append("$text(")
        }
    }

    private fun doesNumberContainDot(): Boolean {
        var text = tvInput.text
        var number = ""
        var operations: Set<Char> = setOf('-', '+', '*', '/')
        for (i in text.length-1 downTo 0 step 1) {
            if (operations.contains(text[i]) || i == 0) break
            number = number.plus(text[i])
        }
        return number.contains('.')
    }

    private fun onOperationPressed(text: String) {
        if (isLastDot()) {
            tvInput.text = tvInput.text.toString().dropLast(1)
        }
        if (!isLastOperation()) {
            if (isEqualPressed) {
                tvInput.text = tvResult.text
                tvResult.text = ""
                isEqualPressed = false
            }
            if (text=="1/x") tvInput.append("^(-1)")
            else tvInput.append(text)
        }
    }

    private fun onClearPressed() {
        tvInput.text = ""
        tvResult.text = ""
        isEqualPressed = false
    }

    private fun onDeletePressed() {
        val text = tvInput.text
        if(text.isNotEmpty()) {
            tvInput.text = text.dropLast(1)
        }
        tvResult.text = ""
        isEqualPressed = false
    }

    private fun onEqualsPressed() {
        val log: Function = object : Function("log", 2) { //считаем log(a, b)=ln(a)/ln(b)
            override fun apply(vararg args: Double): Double {
                return ln(args[0]) / ln(args[1])
            }
        }

        val ln: Function = object : Function("ln", 1) {
            override fun apply(vararg args: Double): Double {
                return ln(args[0])
            }
        }
        val lg: Function = object : Function("lg", 1) {
            override fun apply(vararg args: Double): Double {
                return log10(args[0])
            }
        }
        if (isLastOperation()) tvInput.text = tvInput.text.dropLast(1) //убираем последний символ, если он операция

        val input = tvInput.text.toString()
        try {
            val expression = ExpressionBuilder(input).function(log).function(ln).function(lg).build()
            val result = expression.evaluate();
            if ("${result.toInt()}.0" == "$result") {
                tvResult.text = "${result.toInt()}"
            } else {
                val rounded = round(result * 10000000)/10000000; //избавляемся от погрешности чисел с плавающей точкой
                tvResult.text = rounded.toString()
            }
            isEqualPressed = true
        } catch (ex: Exception) {
            tvResult.text = "Error"
        }
    }

    private fun isLastNumeric(): Boolean {
        if (tvInput.text.isNotEmpty()) {
            val lastSymbol = tvInput.text.toString().last()
            if (lastSymbol.isDigit() || lastSymbol =='π' || lastSymbol == 'e') {
                return true
            }
        }
        return false
    }

    private fun isLastDot(): Boolean {
        if (tvInput.text.isNotEmpty()) {
            val text = tvInput.text.toString()
            if (text.last() == '.') {
                return true
            }
        }
        return false
    }

    private fun isLastOperation(): Boolean {
        if (tvInput.text.isNotEmpty()) {
            val text = tvInput.text.toString()
            if (text.last() == '+' || text.last() == '-' || text.last() == '/' || text.last() == '*') {
                return true
            }
        }
        return false
    }

    private fun onBracketPressed(text: String) {
        when(text) {
            "(" ->
                if (isLastNumeric()) {
                    tvInput.append("*$text")
                } else if (tvInput.text.isEmpty() || (!isLastDot() && tvInput.text.last() != '(')) {
                    tvInput.append(text)
                }
            ")" ->
                if ((!isLastDot() && isLastNumeric()) || (tvInput.text.last() == ')' && getNumberOfClosedBrackets() < getNumberOfOpenBrackets())) {
                    tvInput.append(text)
                }
        }
    }

    private fun getNumberOfOpenBrackets(): Int {
        var text = tvInput.text
        return text.filter{ it=='('}.count()
    }

    private fun getNumberOfClosedBrackets(): Int {
        var text = tvInput.text
        return text.filter{ it==')'}.count()
    }

}
