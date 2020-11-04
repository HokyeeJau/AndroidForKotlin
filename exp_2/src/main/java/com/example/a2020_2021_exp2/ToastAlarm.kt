package com.example.a2020_2021_exp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

class ToastAlarm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast_alarm)

        var myToast = Toast(this)

        //1.类型强转的写法
        var myToastView = LayoutInflater.from(this@ToastAlarm).inflate(R.layout.activity_toast_alarm,null) as RelativeLayout
        myToast.setGravity(Gravity.CENTER,0,0)

        //2.setView(myToastView) -> view = myToastView
        myToast.view =  myToastView
        var toastText = (myToast.view as RelativeLayout).findViewById<TextView>(R.id.toastText)

        //3.自动匹配类型    var str : String = "kotlin" 也可以
        var str = "You cannot add squart operator before you add another operator"

        //4.字符串模板
        toastText.text = str
        toastText.textSize = 50f
        myToast.show()
    }
}