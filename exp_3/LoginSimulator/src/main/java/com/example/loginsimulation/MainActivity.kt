package com.example.loginsimulation

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    var mobile = "13000000000"
    var pwd = "19931011"
    var code = ""
    var ifGetCode:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Spinner Adapter
        var spn = findViewById<Spinner>(R.id.user_spinner)
        var userList = resources.getStringArray(R.array.user_list)
        val userAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userList)
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn!!.setAdapter(userAdapter)
        Log.e("30", "Set Adapter")

        // Change Surface
        var fg = findViewById<RadioGroup>(R.id.radiogroup)
        fg.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                if (checkedId.equals(R.id.btn1)) {
                    Log.e("37", "Change login type")
                    findViewById<RelativeLayout>(R.id.rl6).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.rl4).visibility = View.VISIBLE
                    findViewById<CheckBox>(R.id.memPwd).visibility = View.VISIBLE
                } else {
                    Log.e("42", "Change login type")
                    findViewById<RelativeLayout>(R.id.rl6).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.rl4).visibility = View.GONE
                    findViewById<CheckBox>(R.id.memPwd).visibility = View.GONE
                }
            }
        )

        // Get random code
        var code_btn = findViewById<Button>(R.id.code_btn)
        code_btn.setOnClickListener {
            code = ""
            for (i in 1..6){
                code += (0..9).random().toString()
            }
            Log.e("57", code)
            ifGetCode = true
            var builder = AlertDialog.Builder(this)
            builder.setTitle("请记住验证码")
            builder.setMessage("您的手机号码是$mobile，本次验证码是$code。请输入验证码")
            builder.setPositiveButton("确定返回"){ dialogInterface: DialogInterface, _: Int -> Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show()}
            builder.show()
        }

        //
        var login_btn = findViewById<Button>(R.id.login_btn)
        login_btn.setOnClickListener {
            if (fg.checkedRadioButtonId.equals(R.id.btn1)) {
                Log.e("69", "password login")
                pwd_login(mobile, pwd)
            } else {
                if (ifGetCode==false) {
                    Log.e("72", "Login without code")
                    Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("76", "Code login")
                    var code = findViewById<EditText>(R.id.code)
                    code_login(mobile, code.text.toString())
                }
            }
        }

        // forget password
        var pwd_btn = findViewById<Button>(R.id.pwd_btn)
        pwd_btn.setOnClickListener {
            Log.e("86", "Go to another intent")
            val intent = Intent()
            intent.setClass(this, ForgetPassword::class.java)
            startActivity(intent)
        }
    }

    // Change Password
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val returnData = data?.getStringExtra("pwd")
                if (returnData != null) {
                    Log.e("100", "change pwd successfully")
                    pwd = returnData
                } else {
                    Log.e("103", "fail to change pwd")
                    Toast.makeText(this, "你并没有更改到密码哦", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Login by password
    fun pwd_login(orgMobile:String, orgPwd:String){
        var mobile = findViewById<EditText>(R.id.mobile).text.toString()
        var pwd = findViewById<EditText>(R.id.pwd).text.toString()
        var spn = findViewById<Spinner>(R.id.user_spinner).selectedItem.toString()
        if (mobile.equals(orgMobile) && pwd.equals(orgPwd)){
            Log.e("116", "Pwd Login successfully")
            succeessfulLogin(mobile, spn)
        } else {
            Log.e("119", "Wrong pwd or mobile phone")
            Toast.makeText(this,"手机号码或者密码输入错误", Toast.LENGTH_SHORT).show()
        }

    }

    // Login by code
    fun code_login(orgMobile:String, orgCode:String){
        var mobile = findViewById<EditText>(R.id.mobile).text.toString()
        var code = findViewById<EditText>(R.id.code).text.toString()
        var spn = findViewById<Spinner>(R.id.user_spinner).selectedItem.toString()
        if (mobile.equals(orgMobile) && code.equals(orgCode)) {
            Log.e("131", "Login in successfully")
            succeessfulLogin(mobile, spn)
        } else {
            Log.e("134", "Wrong code")
            Toast.makeText(this, "验证码输入错误", Toast.LENGTH_SHORT).show()
        }
    }

    // Login successfully
    fun succeessfulLogin(mobile: String, spn:String) {
        Log.e("141", "Login successful function")
        var builder = AlertDialog.Builder(this)
        builder.setTitle("登录成功")
        builder.setMessage("您的手机号码是$mobile，类型是$spn。恭喜你通过登陆验证，点击'确认'按钮返回上个页面")
        builder.setPositiveButton("确定返回"){ dialogInterface: DialogInterface, _: Int -> Toast.makeText(this, "返回", Toast.LENGTH_SHORT).show()}
        builder.setNegativeButton("我再看看"){ dialogInterface: DialogInterface, _: Int -> Toast.makeText(this, "再看多会儿", Toast.LENGTH_SHORT).show()}
        builder.show()
    }
}