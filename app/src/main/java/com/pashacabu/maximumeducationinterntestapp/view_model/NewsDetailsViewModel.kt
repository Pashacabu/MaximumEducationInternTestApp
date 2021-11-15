package com.pashacabu.maximumeducationinterntestapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashacabu.maximumeducationinterntestapp.model.db.DBHelper
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val dbHelper: DBHelper
) : ViewModel() {

    private val mutableNewsData : MutableLiveData<DataItem?> = MutableLiveData()
    val liveNewsData : LiveData<DataItem?> get() = mutableNewsData

    fun loadNewsData(url : String){
        viewModelScope.launch {
            mutableNewsData.postValue(dbHelper.getNewsDetails(url))
        }

    }

}