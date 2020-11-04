package com.example.storage.ui.dashboard

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.storage.R
import com.example.storage.database.CommodityDBHelper
import com.example.storage.database.UserCartDBHelper
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.dip
object cartContract {
    object cartEntry : BaseColumns {
        const val TABLE_NAME = "cart"
        const val CART_ID = "id"
        const val GOOD_ID = "good_id"
        const val COUNT = "count"
        const val UPDATE_TIME = "update_time"
    }
}
object goodContract {
    object goodEntry : BaseColumns {
        const val TABLE_NAME = "commodity"
        const val ID ="id"
        const val NAME = "name"
        const val DESC = "desc"
        const val PRICE = "price"
        const val THUMB = "thumb_path"
        const val PIC = "pic_path"
    }
}
@SuppressLint("DefaultLocale")
class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var goodHelper: CommodityDBHelper?= null
    private var goodCursor: Cursor?= null
    private var cartHelper: UserCartDBHelper?=null
    private var cartCursor: Cursor?=null
    private var commList = arrayListOf<ArrayList<String>>()
    private var cartList = arrayListOf<ArrayList<String>>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        goodHelper = CommodityDBHelper(this.requireContext())
        goodHelper!!.buildDB()
        cartHelper = UserCartDBHelper(this.requireContext())
        cartHelper!!.buildDB()
        var lil = root.findViewById<ScrollView>(R.id.ll)
        lil.addView(initDB())
        Log.e("Cart Layout count", lil.childCount.toString())
        return root
    }

    fun initDB (): View? {
        val scrollView = ScrollView(context)
        cartCursor = cartHelper?.readDB()
        var cartCount = cartCursor!!.count
        Log.e("cartCount", cartCount.toString())
        if (cartCount <= 0) {
            scrollView.addView(emptyCart())
            return scrollView
        } else {
            var ll = initViews(cartCursor!!)
            cartCursor?.close()
            return ll
        }
    }

    @SuppressLint("ResourceAsColor")
    fun emptyCart(): LinearLayout {
        Log.e("Init View", "Start")
        val outerLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        outerLayoutParams.gravity = Gravity.CENTER
        var outerLinear = LinearLayout(context)
        var text = TextView(context)
        text.id = 0
        text.layoutParams = outerLayoutParams
        text.setText("你的购物车里没有东西哟,快去商城看看吧")
        text.setTextSize(dip(20).toFloat())
        text.setBackgroundResource(R.drawable.reshape_btn_border)
        text.setTextColor(R.color.orange)
        outerLinear.addView(text)
        return outerLinear
    }

    fun initViews (cursor: Cursor): LinearLayout{
        val ll2 = LinearLayout(context)
        ll2.orientation = LinearLayout.VERTICAL
        val ll3 = LinearLayout(context)
        ll3.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 0, 1F)
        ll3.orientation = LinearLayout.VERTICAL
        val table = TableLayout(context)
        table.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1.0F)
        table.setStretchAllColumns(true);
        var totalPrice = 0.0
        cursor.let{
            cursor.moveToFirst()
            val count = cursor.count -1

            // 表头
            var tr = TableRow(context)
            var col = arrayOf("图片","价格","数量","单价","总价")
            for (i in 0..4) {
                var tv = TextView(context)
                tv.setText(col[i])
                tr.addView(tv)
            }
            table.addView(tr)
            Log.e("Table column", tr.childCount.toString())

            // 表身
            for ( i in 0..count) {
                val good_id = cursor.getString(1)
                goodCursor = goodHelper?.findComm(good_id)
                    // thumb, name, count, price
                var thumb = goodCursor!!.getString(4)
                var name = goodCursor!!.getString(1)
                var mcount = cursor.getString(2)
                var price = goodCursor!!.getString(3)
                table.addView(getTable(thumb, name, mcount, price))
                totalPrice += count.toFloat() * price.toFloat()
                cursor.moveToNext()
                Log.e("TableRow count", table.childCount.toString())
            }
            ll3.addView(table)
            Log.e("Cart Layout count", ll2.childCount.toString())
        }
        cursor.close()
        ll3.addView(getCash(totalPrice))
        ll2.addView(ll3)
        return ll2
    }


    fun getTable(thumb:String ,name:String, count:String, price:String): TableRow {
        var tr = TableRow(context)
        var img = ImageView(context)
        img.setImageResource(thumb.toInt())
        tr.addView(img)

        var t1 = TextView(context)
        t1.setText(name)
        t1.setTextSize(20.0F)
        tr.addView(t1)

        var t2 = TextView(context)
        t2.setText(count)
        t2.setTextSize(20.0F)
        tr.addView(t2)

        var t3 = TextView(context)
        t3.setText(price)
        t3.setTextSize(20.0F)
        tr.addView(t3)

        var t4 = TextView(context)
        t4.setText((count.toFloat()*price.toFloat()).toString())
        t4.setTextSize(20.0F)
        tr.addView(t4)
        return tr
    }


    @SuppressLint("WrongConstant", "ResourceAsColor")
    fun getCash(count: Double): LinearLayout {
        var t1 = TextView(context)
        t1.layoutParams = LinearLayout.LayoutParams(100,50)
        t1.setText("总金额")
//        t1.setTextSize(25.0F)
        t1.setTextAlignment(4)

        var t2 = TextView(context)
        t2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        t2.setText(count.toString())
//        t2.setTextSize(35.0F)
        t2.setTextAlignment(4)
        t2.setTextColor(R.color.deepOrange)

        var btn1 = Button(context)
        btn1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btn1.setText("结算")
//        btn1.setTextSize(25.0F)

        var btn = Button(context)
        btn.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        btn.setText("清空")
//        btn.setTextSize(25.0F)
        btn.onClick {
            cartHelper?.clearDB()
        }

        var ll2 = LinearLayout(context)
        ll2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ll2.orientation = LinearLayout.HORIZONTAL
        ll2.gravity = Gravity.BOTTOM
        ll2.addView(t1)
        ll2.addView(t2)
        ll2.addView(btn)
        ll2.addView(btn1)
        return ll2
    }
}
