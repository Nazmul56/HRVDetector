package com.example.android.ui.fragment.bgTestHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.db.BgTestData

class BgTestHistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
    var bgHistoryList: MutableList<BgTestData>? = null
}