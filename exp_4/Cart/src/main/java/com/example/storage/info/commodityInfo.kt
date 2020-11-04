package com.example.storage.info

import com.example.storage.R
import java.util.*

class commodityInfo {
    var rowid = 0L       // 行号
    var xuhao = 0        // 序号
    var name = ""        // 名称
    var desc = ""        // 描述
    var price = 0f       // 价格
    var thumb_path = ""  // 小图的保存路径
    var pic_path = ""    // 大图的保存路径
    var thumb = 0        // 小图的资源编号
    var pic = 0          // 大图的资源编号

    companion object {
        // 声明一个手机商品的名称数组
        private val mNameArray = arrayOf(
            "iPhone8", "Mate10", "小米6", "OPPO R11", "vivo X9S", "魅族Pro6S"
        )

        // 声明一个手机商品的描述数组
        private val mDescArray = arrayOf(
            "Apple iPhone 8 256GB 玫瑰金色 移动联通电信4G手机",
            "华为 HUAWEI Mate10 6GB+128GB 全网通（香槟金）",
            "小米 MI6 全网通版 6GB+128GB 亮白色",
            "OPPO R11 4G+64G 全网通4G智能手机 玫瑰金",
            "vivo X9s 4GB+64GB 全网通4G拍照手机 玫瑰金",
            "魅族 PRO6S 4GB+64GB 全网通公开版 星空黑 移动联通电信4G手机"
        )

        // 声明一个手机商品的价格数组
        private val mPriceArray = floatArrayOf(6888f, 3999f, 2999f, 2899f, 2698f, 2098f)

        // 声明一个手机商品的小图数组
        private val mThumbArray = intArrayOf(
            R.drawable.iphone_s, R.drawable.huawei_s, R.drawable.xiaomi_s,
            R.drawable.oppo_s, R.drawable.vivo_s, R.drawable.meizu_s
        )

        // 声明一个手机商品的大图数组
        private val mPicArray = intArrayOf(
            R.drawable.iphone, R.drawable.huawei, R.drawable.xiaomi,
            R.drawable.oppo, R.drawable.vivo, R.drawable.meizu
        )

        // 获取默认的手机信息列表
        val defaultList: ArrayList<commodityInfo>
            get() {
                val comInfo = ArrayList<commodityInfo>()
                for (i in mNameArray.indices) {
                    val info = commodityInfo()
                    info.name = mNameArray[i]
                    info.desc = mDescArray[i]
                    info.price = mPriceArray[i]
                    info.thumb = mThumbArray[i]
                    info.pic = mPicArray[i]
                    comInfo.add(info)
                }
                return comInfo
            }
    }
}