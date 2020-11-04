package com.example.storage.ui.home

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.storage.R
import com.example.storage.database.UserCartDBHelper
import org.jetbrains.anko.dip

class goodDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_good_detail)
        val bundle = intent.extras
        val name = bundle?.getString("name")
        val price = bundle?.getString("price")
        val desc = bundle?.getString("desc")
        val pic = Uri.parse(bundle?.getString("pic"))
        val id = bundle?.getString("id")
        var actionbar = supportActionBar
        actionbar!!.title = name
//        var ll = findViewById<LinearLayout>(R.id.ll)
//        ll.addView(initView(price!!, desc!!, pic!!, id!!))
    }

    @SuppressLint("ResourceAsColor")
    fun initView(price: String, desc: String, pic: Uri, id:String): LinearLayout {
        val ll = LinearLayout(baseContext)
        var linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ll.layoutParams = linearLayoutParams
        linearLayoutParams.gravity = Gravity.CENTER

        val img = ImageView(baseContext) // set ImageView's id
        img.layoutParams = linearLayoutParams
        img.setImageURI(pic)
        ll.addView(img)

        val text1 = TextView(baseContext)
        text1.layoutParams = linearLayoutParams
        text1.setText(price)
        text1.setTextColor(R.color.orange)
        text1.setTextSize(dip(8).toFloat())
        ll.addView(text1)

        val text = TextView(baseContext)
        text.layoutParams = linearLayoutParams
        text.setText(desc)
        text.setTextSize(dip(4).toFloat())
        ll.addView(text)

        val btn = Button(baseContext)
        btn.layoutParams = linearLayoutParams
        btn.setText("加入购物车")
        btn.setTextSize(dip(6).toFloat())
        btn.setOnClickListener{
            var db = UserCartDBHelper(baseContext)
            db.insertDB(id)
        }
        ll.addView(btn)
        return ll
    }
}