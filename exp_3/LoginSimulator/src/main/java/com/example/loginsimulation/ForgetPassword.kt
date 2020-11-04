package com.example.loginsimulation

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        // Listen to the code get button
        var ifGetCode:Boolean = false
        var code = ""
        var btn1 = findViewById<Button>(R.id.code_btn2)
        btn1.setOnClickListener{
            code = ""
            for (i in 1..6){
                code += (0..9).random().toString()
            }
            Log.e("code", code)
            ifGetCode = true
            var builder = AlertDialog.Builder(this)
            builder.setTitle("请记住验证码")
            builder.setMessage("本次验证码是$code。请输入验证码")
            builder.setPositiveButton("确定"){ dialogInterface: DialogInterface, _: Int -> Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show()}
            builder.show()
        }

        // Change Password
        var btn2 = findViewById<Button>(R.id.new_pwd_btn)
        btn2.setOnClickListener {
            if (ifGetCode) {
                var pwd1 = findViewById<EditText>(R.id.new_pwd1).text.toString()
                var pwd2 = findViewById<EditText>(R.id.new_pwd2).text.toString()
                var inputCode = findViewById<EditText>(R.id.code2).text.toString()
                if (pwd1.equals(pwd2) && inputCode.equals(code)) {
                    returnValue(pwd1)
                } else {
                    Toast.makeText(this, "密码不匹配或验证码不匹配", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun returnValue (pwd:String){
        val intent = Intent()
        intent.putExtra("pwd", pwd)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}