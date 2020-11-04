package com.example.mortgagecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.view.*;
import org.jetbrains.anko.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load Year Spinner Set Adapter
        var loanYearSpinner = findViewById<Spinner>(R.id.sp1)
        val loanYearList = resources.getStringArray(R.array.years)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, loanYearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        loanYearSpinner!!.setAdapter(yearAdapter)

        // Interest Rate Spinner Set Adapter
        var interestRateSpinner = findViewById<Spinner>(R.id.sp2)
        val interestRateList = resources.getStringArray(R.array.baserate)
        val rateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, interestRateList)
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        interestRateSpinner!!.setAdapter(rateAdapter)

        // Listen to total calculation button
        var totalCal = findViewById<Button>(R.id.totalcal)
        totalCal.setOnClickListener {
            var tp = findViewById<EditText>(R.id.row1edit).text.toString()
            var mp = findViewById<EditText>(R.id.row2edit).text.toString()
            if (isNumberic(tp) && isNumberic(mp) && mp.toDouble() <= 100.0){
                findViewById<TextView>(R.id.showtotal).setText("其中贷款部分为："+(tp.toDouble()*mp.toDouble()*0.01).toString()+" 万")
            } else {
                findViewById<EditText>(R.id.row1edit).setText("");
                findViewById<EditText>(R.id.row2edit).setText("")
                toast("格式不对，再输一遍吧")
            }
        }

        // Listen to list detail button
        var detailBtn = findViewById<Button>(R.id.detail)
        detailBtn.setOnClickListener {
            showDetails();
        }
    }

    fun showDetails(){
        var tp = findViewById<EditText>(R.id.row1edit).text.toString()
        var mp = findViewById<EditText>(R.id.row2edit).text.toString()
        if (isNumberic(tp) && isNumberic(mp) && mp.toDouble() <= 100){
            findViewById<TextView>(R.id.showtotal).setText("其中贷款部分为："+(tp.toDouble()*mp.toDouble()*0.01).toString()+" 万")
        } else {
            toast("请把信息填写完整！")
            return
        }
        Log.e("61", tp+" "+mp)
        var intotal:Double = tp.toDouble()*mp.toDouble()*0.01
        var ifComm:Boolean = false
        var ifHouse:Boolean = false

        // Get the selected loan Year
        var loanYearSpn = findViewById<Spinner>(R.id.sp1)
        val loanYearArr = arrayOf(5,10,15,20,25,30)
        var loanYear:Int = loanYearArr[loanYearSpn.selectedItemPosition?:0]
        Log.e("79", "Loan Year"+loanYear.toString())

        // Get the selected base rate and commercial rate
        var baseRateSpn = findViewById<Spinner>(R.id.sp2)
        val commRateArr = arrayOf(4.9, 5.15, 5.4, 5.65, 5.9, 6.15, 6.15)
        val houseRateArr = arrayOf(3.25, 3.25, 3.5, 3.75, 4.0, 4.25, 4.5)
        var commRate:Double = commRateArr[baseRateSpn.selectedItemPosition?:0]
        var houseRate:Double =  houseRateArr[baseRateSpn.selectedItemPosition?:0]

        baseRateSpn.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                commRate = commRateArr[position]
                houseRate = houseRateArr[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                commRate = commRateArr[0]
                houseRate = houseRateArr[0]
            }
        }
        Log.e("98", "Commercial Rate: "+commRate.toString())
        Log.e("99", "House Rate: "+houseRate.toString())

        // Check Commerical Loan
        var checkComm = findViewById<CheckBox>(R.id.check1)
        if(checkComm.isChecked()) {
            ifComm = true
        }
        Log.e("106", ifComm.toString())

        // Check Housing Fund
        var checkHouse = findViewById<CheckBox>(R.id.check2)
        if(checkHouse.isChecked()) {
            ifHouse = true
        }
        Log.e("113", ifHouse.toString())

        // Ensure the refund type then jump to its calculation
        /*
         * 是否商贷：ifComm
         * 商贷：commNum
         * 是否公积金：ifHouse
         * 公积金：houseFund
         * 年限：loanYear
         * 商贷利率：commRate
         * 公积金利率：houseRate
        */
        var houseFund:Double ?= null
        var commNum:Double ?= null
        if (ifComm==true || ifHouse==true) {
            // Check if the checked label filled numbers
            val houseFundCheck = findViewById<EditText>(R.id.row5label)
            if(ifHouse) {
                houseFundCheck.checkBlack("商贷不能为空或格式错误")?:return
            }
            if(ifHouse) {
                houseFund = houseFundCheck.text.toString().toDouble()
            }
            Log.e("133", houseFundCheck.text.toString())

            val commNumCheck = findViewById<EditText>(R.id.row4label)
            if(ifComm) {
                commNumCheck.checkBlack("公积金不能为空或格式错误")?:return
            }
            if(ifComm) {
                commNum = commNumCheck.text.toString().toDouble()
            }
            Log.e("139", commNumCheck.text.toString())


            // Decide Loan Type
            var loanType = findViewById<RadioGroup>(R.id.radiogroup)
            val selectedType: Int = loanType.checkedRadioButtonId
            if(selectedType.equals(R.id.btn1)) {
                Log.e("146","btn1")
                equalInterest(ifComm, commNum, ifHouse, houseFund, loanYear, commRate*0.01/12.0, houseRate*0.01/12.0, intotal)
            } else if(selectedType.equals(R.id.btn2)) {
                Log.e("149","btn2")
                equalPrincipal(ifComm, commNum, ifHouse, houseFund, loanYear, commRate*0.01/12.0, houseRate*0.01/12.0, intotal)
            }
        } else {
            toast("必须选一种贷款种类哦！")
        }
    }

    // 等额本息
    fun equalInterest(ifComm:Boolean, commNum:Double?, ifHouse:Boolean, houseFund:Double?, loanYear: Int, commRate: Double, houseRate:Double, intotal:Double){
        if (ifComm && ifHouse == false) {
            Log.e("160","EI")
            var pertime = intotal!!*commRate!!*Math.pow((1+commRate), loanYear!!.toDouble()*12.0) /
                    (Math.pow(1+commRate, loanYear!!.toDouble()*12.0)-1)
            var total = pertime * loanYear.toDouble() * 12.0
            var extra = total-intotal
            findViewById<TextView>(R.id.alldetail).setText("您的贷款总额为"+String.format("%.2f", intotal)+"万元\n还款总额为"
                    + String.format("%.2f", total)+"万元\n其中利息总额为"+String.format("%.2f", extra)+"万元\n还款总时间为"
                    + (loanYear*12).toString() + "月\n每月还款金额为"+String.format("%.2f", pertime*10000)+"元")
        } else if (ifHouse && ifComm == false) {
            Log.e("169","EI")
            var pertime = intotal!!*houseRate!!*Math.pow((1+houseRate), loanYear!!.toDouble()*12.0) /
                    (Math.pow(1+houseRate, loanYear!!.toDouble()*12.0)-1)
            var total = pertime * loanYear.toDouble() * 12.0
            var extra = total - intotal
            findViewById<TextView>(R.id.alldetail).setText("您的贷款总额为"+String.format("%.2f", intotal)+"万元\n还款总额为"
                    + String.format("%.2f", total)+"万元\n其中利息总额为"+String.format("%.2f", extra)+"万元\n还款总时间为"
                    + (loanYear*12).toString() + "月\n每月还款金额为"+String.format("%.2f", pertime*10000)+"元")
        } else {
            if ((commNum!!+houseFund!!) == intotal) {
                Log.e("179","EI")
                var pertime1 = commNum!! * commRate!! * Math.pow(
                    (1 + commRate),
                    loanYear!!.toDouble() * 12.0
                ) /
                        (Math.pow(1 + commRate, loanYear!!.toDouble() * 12.0) - 1)
                var pertime2 = houseFund!! * houseRate!! * Math.pow(
                    (1 + houseRate),
                    loanYear!!.toDouble() * 12.0
                ) /
                        (Math.pow(1 + houseRate, loanYear!!.toDouble() * 12.0) - 1)
                var pertime = pertime1 + pertime2
                var total = pertime * loanYear * 12.0
                var extra = total - intotal
                findViewById<TextView>(R.id.alldetail).setText(
                    "您的贷款总额为" + String.format("%.2f", intotal) + "万元\n还款总额为"
                            + String.format("%.2f", total) + "万元\n其中利息总额为" + String.format(
                        "%.2f",
                        extra
                    ) + "万元\n还款总时间为"
                            + (loanYear * 12).toString() + "月\n每月还款金额为" + String.format(
                        "%.2f",
                        pertime * 10000
                    ) + "元"
                )
            } else {
                Log.e("205","EI")
                toast("填写的两项贷款总额不等于初始贷款额度，请重新填写")
            }
        }
    }

    // 等额本金
    fun equalPrincipal(ifComm:Boolean, commNum:Double?, ifHouse:Boolean, houseFund:Double?, loanYear: Int, commRate: Double, houseRate:Double, intotal:Double){
        if (ifComm && ifHouse == false) {
            Log.e("214", "EP")
            var array = arrayOfNulls<Double>(loanYear!!*12)
            var sum:Double = 0.0
            var text:String = "";
            for (i in 0..(loanYear*12)-1) {
                array[i] = intotal / loanYear.toDouble() / 12.0 + (intotal - sum) * commRate!!
                sum += array[i]!!;
                text+="\n第"+(i+1).toString() + "个月应还金额为："+String.format("%.2f", array[i]!!*10000);
            }
            var extra = sum - intotal
            findViewById<TextView>(R.id.alldetail).setText("您的贷款总额为"+String.format("%.2f", intotal)+"万元\n还款总额为"
                    + String.format("%.2f", sum)+"万元\n其中利息总额为"+String.format("%.2f", extra)+"万元\n还款总时间为"
                    + (loanYear*12).toString() + "月\n每月还款金额如下:"+text)
        } else if (ifHouse && ifComm == false) {
            Log.e("228", "EP")
            var array = arrayOfNulls<Double>(loanYear!!*12)
            var sum:Double = 0.0
            var text:String = "";
            for (i in 0..(loanYear*12)-1) {
                array[i] = intotal / loanYear.toDouble() / 12.0 + (intotal - sum) * houseRate!!
                sum += array[i]!!;
                text+="\n第"+(i+1).toString() + "个月应还金额为："+String.format("%.2f", array[i]!!*10000);
            }
            var extra = sum - intotal
            findViewById<TextView>(R.id.alldetail).setText("您的贷款总额为"+String.format("%.2f", intotal)+"万元\n还款总额为"
                    + String.format("%.2f", sum)+"万元\n其中利息总额为"+String.format("%.2f", extra)+"万元\n还款总时间为"
                    + (loanYear*12).toString() + "月\n每月还款金额如下:"+text)
        } else {
            Log.e("242", "EP")
            if ((commNum!!+houseFund!!) == intotal) {
                var month = loanYear!! * 12
                var array1 = arrayOfNulls<Double>(loanYear!! * 12)
                var array2 = arrayOfNulls<Double>(loanYear!! * 12)
                var sum1 = 0.0
                var sum2 = 0.0
                var text:String = ""
                for (i in 0..(month-1)) {
                    array1[i] = commNum/month + (commNum-sum1) * commRate!!
                    array2[i] = houseFund/month + (houseFund-sum2) * houseRate!!
                    sum1+=array1[i]!!
                    sum2+=array2[i]!!
                    text += "\n第"+(i+1)+"个月应还金额为："+String.format("%.2f", (array1[i]!!+array2[i]!!)*10000)+"元"
                }
                var total = sum1+sum2
                var extra = total - intotal
                findViewById<TextView>(R.id.alldetail).setText("您的贷款总额为"+String.format("%.2f", intotal)+"万元\n还款总额为"
                        + String.format("%.2f", total)+"万元\n其中利息总额为"+String.format("%.2f", extra)+"万元\n还款总时间为"
                        + month.toString() + "月\n每月还款金额如下:"+text)
            } else {
                toast("填写的两项贷款总额不等于初始贷款额度，请重新填写")
            }

        }
    }

    fun isNumberic(input: String): Boolean {
        Log.e("270", "IN")
        try {
            input.toDouble()
            return true
        } catch(e: NumberFormatException) {
            return false
        }
    }

    fun EditText.checkBlack(message: String): Double? {
        Log.e("272", "ET")
        val text = this.text.toString()
        if (text.isBlank() || text.toDouble()==null) {
            toast(message)
            return null
        }
        return text.toDouble()
    }
}