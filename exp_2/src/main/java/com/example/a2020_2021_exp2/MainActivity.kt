package com.example.a2020_2021_exp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.example.a2020_2021_exp2.ParsingTree
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    val optPtn = Regex("""[*/\-+]|[<>]""")
    var clear:View ?= null
    var delete:View ?= null
    var point:View ?= null
    var equal:View ?= null

    var subtract:View ?= null
    var multiply:View ?= null
    var add:View ?= null
    var minus:View ?= null
    var squart:View ?= null

    var zero:View ?= null
    var one:View ?= null
    var two:View ?= null
    var three:View ?= null
    var four:View ?= null
    var five:View ?= null
    var six:View ?= null
    var seven:View ?= null
    var eight:View ?= null
    var nine:View ?= null

    var result:View ?= null
    var expr = ""
    // @param lastType, 0 -> number, 1 ->operator
    var lastType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialization()
        clickListener()
    }

    fun initialization() {
        clear = findViewById<Button>(R.id.clear)
        delete = findViewById<Button>(R.id.delete)
        point = findViewById<Button>(R.id.point)
        equal = findViewById<Button>(R.id.equal)

        subtract = findViewById<Button>(R.id.subtract)
        multiply = findViewById<Button>(R.id.multiply)
        add = findViewById<Button>(R.id.add)
        minus = findViewById<Button>(R.id.minus)
        squart = findViewById<Button>(R.id.squart)

        zero = findViewById<Button>(R.id.zero)
        one = findViewById<Button>(R.id.one)
        two = findViewById<Button>(R.id.two)
        three = findViewById<Button>(R.id.three)
        four = findViewById<Button>(R.id.four)
        five = findViewById<Button>(R.id.five)
        six = findViewById<Button>(R.id.six)
        seven = findViewById<Button>(R.id.seven)
        eight = findViewById<Button>(R.id.eight)
        nine = findViewById<Button>(R.id.nine)

        result = findViewById(R.id.equation_result)
        expr = ""
        // @param lastType, 0 -> number, 1 ->operator
        lastType = 0
    }

    fun clickListener() {
        // Add Numbers
        nine?.setOnClickListener   {
            lastType = 0
            addComponent("9")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        eight?.setOnClickListener  {
            lastType = 0
            addComponent("8")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        seven?.setOnClickListener  {
            lastType = 0
            addComponent("7")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        six?.setOnClickListener    {
            lastType = 0
            addComponent("6")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        five?.setOnClickListener   {
            lastType = 0
            addComponent("5")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        four?.setOnClickListener   {
            lastType = 0
            addComponent("4")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        three?.setOnClickListener  {
            lastType = 0
            addComponent("3")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        two?.setOnClickListener    {
            lastType = 0
            addComponent("2")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        one?.setOnClickListener    {
            lastType = 0
            addComponent("1")
            (result as EditText).setSelection((result as EditText).text.length)
        }
        zero?.setOnClickListener   {
            lastType = 0
            addComponent("0")
            (result as EditText).setSelection((result as EditText).text.length)
        }

        // Add Arithmetic Operators
        add?.setOnClickListener        {
            if (lastType == 1){
                delComponent(false)
            }
            addComponent("+")
            (result as EditText).setSelection((result as EditText).text.length)
            lastType = 1
        }
        minus?.setOnClickListener      {
            if (lastType == 1) {
                delComponent(false)
            }
            addComponent("-")
            (result as EditText).setSelection((result as EditText).text.length)
            lastType = 1
        }
        multiply?.setOnClickListener   {
            if (lastType == 1) {
                delComponent(false)
            }
            addComponent("*")
            (result as EditText).setSelection((result as EditText).text.length)
            lastType = 1
        }
        subtract?.setOnClickListener  {
            if (lastType == 1) {
                delComponent(false)
            }
            addComponent("/")
            (result as EditText).setSelection((result as EditText).text.length)
            lastType = 1
        }

        squart?.setOnClickListener     {
            if (lastType == 0) {
                toast("You cannot add squart directly followed the numbers!")
            } else {
                addComponent("<>")
                (result as EditText).setSelection((result as EditText).text.length-1)
                lastType = 1
            }
        }
        point?.setOnClickListener      {
            if (lastType == 1) {
                delComponent(false)
            }
            addComponent(".")
            (result as EditText).setSelection((result as EditText).text.length)
            lastType = 1
        }

        // Add Basic Operators
        delete?.setOnClickListener     {
            if (expr.length == 1 || expr == R.string.equation.toString()) {
                delComponent(true)
            } else if (lastType == 1){
                if (expr[expr.length-1]=='>'){
                    delComponent(false)
                } else {
                    delComponent(false)
                    lastType = 0
                }
            } else {
                if (optPtn.containsMatchIn(expr[expr.length-2].toString())){
                    lastType = 1
                    delComponent(false)
                } else {
                    lastType = 0
                    delComponent(false)
                }
            }
        }

        clear?.setOnClickListener      { delComponent(true) }
        equal?.setOnClickListener      { findViewById<TextView>(R.id.equation_result).setText(calEquation(expr)) }
    }

    fun calEquation(equa:String):String{
        var pt = ParsingTree(equa)
        expr = pt.getResult()
        return expr
    }

    fun addComponent(comp:String){
        expr += comp
        findViewById<TextView>(R.id.equation_result).setText(expr)
    }

    fun delComponent(all:Boolean) {
        if (all) {
            findViewById<TextView>(R.id.equation_result).setText(R.string.equation)
            expr = ""
            lastType = 0
        } else {
            expr = expr.substring(0, expr.length-1)
            findViewById<TextView>(R.id.equation_result).setText(expr)
            (result as EditText).setSelection((result as EditText).text.length)
        }
    }


}