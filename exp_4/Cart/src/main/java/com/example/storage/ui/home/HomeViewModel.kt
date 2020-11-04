package com.example.storage.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storage.database.CommodityDBHelper
import com.example.storage.database.UserCartDBHelper

class HomeViewModel : ViewModel() {
    val commodityHelper: CommodityDBHelper? = null
    val cartHelper: UserCartDBHelper ?= null

    private val _text = MutableLiveData<String>().apply {

    }
    val text: LiveData<String> = _text
}