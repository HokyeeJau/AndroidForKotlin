package com.example.storage.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.children
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import com.example.storage.MainActivity
import com.example.storage.R
import com.example.storage.database.CommodityDBHelper
import com.example.storage.database.UserCartDBHelper
import org.jetbrains.anko.dip
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.startActivity


object goodContract {
    object goodEntry : BaseColumns {
        const val TABLE_NAME = "commodity"
        const val NAME = "name"
        const val DESC = "desc"
        const val PRICE = "price"
        const val THUMB = "thumb_path"
        const val PIC = "pic_path"
    }
}

@SuppressLint("DefaultLocale")
class HomeFragment : Fragment() {

    private var dbHelper: CommodityDBHelper ?= null
    private var commCursor: Cursor ?= null
    private var commList = arrayListOf<ArrayList<String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // VM share data
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        var scrollView = root.findViewById<ScrollView>(R.id.scrollCommodity)
        initDB()
        initViews(scrollView)
        initOperations()

        return root
    }

    fun initDB() {
        dbHelper = CommodityDBHelper(requireContext())
        dbHelper!!.buildDB()
        commCursor = dbHelper!!.readDB()

        commCursor?.let {
            commCursor!!.moveToFirst()
            val cursorCount = commCursor!!.count
//            Log.e("Cursor Count:", cursorCount.toString())
            for (i in 0..(cursorCount-1)){
                var values = arrayOf(
                    commCursor!!.getString(commCursor!!.getColumnIndex(goodContract.goodEntry.NAME)),
                    commCursor!!.getString(commCursor!!.getColumnIndex(goodContract.goodEntry.DESC)),
                    commCursor!!.getString(commCursor!!.getColumnIndex(goodContract.goodEntry.PRICE)),
                    commCursor!!.getString(commCursor!!.getColumnIndex(goodContract.goodEntry.THUMB)),
                    commCursor!!.getString(commCursor!!.getColumnIndex(goodContract.goodEntry.PIC))
                )
//                Log.e(
//                    "initDB",
//                    values[0] + " " + values[1] + " " + values[2] + " " + values[3] + " " + values[4]
//                )
                commList.add(arrayListOf(values[0], values[1], values[2], values[3], values[4]))
                Log.e("Init DB", "$i Commodity List size is ${commList.size}")
                if ( i != cursorCount-1) {
                    commCursor!!.moveToNext()
                } else {
                    commCursor!!.close()
                }
            }
        }
//        Log.e("Init DB", "Commodity List size is ${commList.size}")
    }

    @SuppressLint("ResourceType")
    fun initViews(scrollView: ScrollView) {
        Log.e("Init View", "Start")
        val outerLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        var outerLinear = LinearLayout(context)
        outerLinear.layoutParams = outerLayoutParams
        outerLinear.orientation = LinearLayout.VERTICAL
//        Log.e("Init View", "Commodity List size is ${commList.size}")
        for ( i in 1..commList.size) {
            Log.e("CommList", i.toString())
            var layout = initComm(commList[i-1], i, scrollView)
            outerLinear.addView(layout)
        }
        scrollView.addView(outerLinear)
    }

    @SuppressLint("ResourceAsColor", "WrongConstant")
    fun initComm(arr: ArrayList<String>, idx: Int, scrollView: ScrollView): LinearLayout {
        Log.e("initComm", arr[0])
        var layout = LinearLayout(context)
        var linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.setMargins(dip(20), dip(10), dip(20), 0)
        layout.layoutParams = linearLayoutParams
        layout.setPadding(dip(5), dip(5), dip(5), dip(5))
        layout.setBackgroundResource(R.drawable.reshape_rl_border)
        layout.orientation = LinearLayout.HORIZONTAL

        // ImageView
        var img = ImageView(context)
        img.id = idx // set ImageView's id
        img.layoutParams = LinearLayout.LayoutParams(dip(180), dip(180))
        img.setPadding(dip(10), dip(10), dip(10), dip(10))
        img.setImageResource(arr[3].toInt())
        layout.addView(img)
        layout.setOnClickListener{
            scrollView.removeAllViews()
            scrollView.addView(initView(arr[2], arr[1], arr[4], idx.toString()))
        }


        // Text LinearLayout
        var innerLinear = LinearLayout(context)
        innerLinear.layoutParams = linearLayoutParams
        innerLinear.orientation = LinearLayout.VERTICAL

        var Params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // TextView for Name
        var name = TextView(context)
        name.id = idx
        name.layoutParams = Params
        name.setText(arr[0])
        name.setTextSize(dip(10).toFloat())
        name.setTextColor(R.color.grey)
        name.setTextAlignment(6)

        // TextView for Price
        var price = TextView(context)
        price.id = idx
        price.layoutParams = Params
        price.setText(arr[2])
        price.setTextSize(dip(8).toFloat())
        price.setTextAlignment(6)

        // Button for addToCart
        var btn = Button(context)
        btn.id = idx
        btn.layoutParams = Params
        btn.setText("加入购物车")
        btn.setTextSize(dip(6).toFloat())
        btn.setBackgroundResource(R.drawable.reshape_btn_border)
        btn.setTextColor(R.color.orange)
        btn.setOnClickListener{
            val cart = UserCartDBHelper(this.requireContext())
            cart.insertDB(idx.toString())
        }

        // Put into Layout
        innerLinear.addView(name)
        innerLinear.addView(price)
        innerLinear.addView(btn)
        layout.addView(innerLinear)

        return layout
    }

    fun initOperations() {

    }

    fun btnClicked(v:View){

    }



    override fun onResume() {
        super.onResume()


    }
    override fun onPause() {
        super.onPause()

    }


    @SuppressLint("ResourceAsColor")
    fun initView(price: String, desc: String, pic: String, id:String): LinearLayout {
        val ll = LinearLayout(context)
        var linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        ll.layoutParams = linearLayoutParams
        ll.orientation = LinearLayout.VERTICAL

        val img = ImageView(context) // set ImageView's id
        img.layoutParams = LinearLayout.LayoutParams(dip(300), dip(300))
        img.setImageResource(pic.toInt())
        ll.addView(img)

        val text1 = TextView(context)
        text1.layoutParams = linearLayoutParams
        text1.setText(price)
        text1.setTextColor(R.color.orange)
        text1.setTextSize(dip(7).toFloat())
        ll.addView(text1)

        val text = TextView(context)
        text.layoutParams = linearLayoutParams
        text.setText(desc)
        text.setTextSize(dip(8).toFloat())
        ll.addView(text)

        val btn = Button(context)
        btn.layoutParams = linearLayoutParams
        btn.setText("加入购物车")
        btn.setTextSize(dip(6).toFloat())
        btn.setOnClickListener{
            var db = UserCartDBHelper(this.requireContext())
            db.insertDB(id)
        }
        ll.addView(btn)
        return ll
    }
}
