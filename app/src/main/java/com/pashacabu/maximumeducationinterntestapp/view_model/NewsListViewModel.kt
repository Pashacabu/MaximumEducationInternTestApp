package com.pashacabu.maximumeducationinterntestapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashacabu.maximumeducationinterntestapp.model.db.DBHelper
import com.pashacabu.maximumeducationinterntestapp.model.db.data_classes.State
import com.pashacabu.maximumeducationinterntestapp.model.network.MENetwork
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val db: DBHelper,
    private val network: MENetwork,
) : ViewModel() {

    private var mutableNewsList: MutableLiveData<MutableList<DataItem?>?> = MutableLiveData()
    val newsList: LiveData<MutableList<DataItem?>?> get() = mutableNewsList

    private var mutableLoadingState: MutableLiveData<Boolean?> = MutableLiveData(null)
    val liveLoadingState: LiveData<Boolean?> get() = mutableLoadingState

    private var mutableConnectionState: MutableLiveData<Boolean> = MutableLiveData()
    val liveConnectionState: LiveData<Boolean> get() = mutableConnectionState

    private var errorState: MutableLiveData<State> = MutableLiveData()
    val liveErrorState: LiveData<State> get() = errorState

    private val loadedData : MutableList<DataItem?> = mutableListOf()

    fun loadData(offset: Int, isLoadingMore: Boolean) {
        if (isLoadingMore || (!isLoadingMore && loadedData.isNullOrEmpty())) {
            viewModelScope.launch {
                var timer = 0
                while (mutableConnectionState.value == null && timer < 50) { //waiting for connection status to be actual. it's delayed somehow
                    delay(1)
                    timer += 1
                }
                try {
                    if (mutableConnectionState.value == true) {
                        mutableLoadingState.postValue(true)
                        val list = network.network.getNews(offset = offset).data
                        if (!list.isNullOrEmpty() && !isLoadingMore) {
                            clearDB(mutableConnectionState.value ?: true)
                        }
                        db.saveNews(list)
                        if (list != null) {
                            loadedData.addAll(list)
                        }
                    } else {
                        errorState.postValue(State.NoNetDBLoad)
                        loadedData.clear()
                        loadedData.addAll(db.getNews())
                    }
                    val newList = mutableListOf<DataItem?>() //have to copy list because liveData refers to the same "loadedData" object
                    for (item in loadedData){                //and as we change it async it causes diffUtills to throw "inconsistency detected"
                        newList.add(item?.copy())            //when dispatching changes
                    }
                    mutableNewsList.postValue(newList)
                    mutableLoadingState.postValue(false)

                } catch (e: Exception) {
                    e.printStackTrace()
                    mutableLoadingState.postValue(false)
                    when (network.responseCode / 100) {
                        in 0 until 1 -> {
                            errorState.postValue(State.BasicNetworkError)
                        }
                        in 1 until 2 -> {
                            errorState.postValue(State.NetErr1xx)
                        }
                        2 -> {
                            errorState.postValue(State.NetErr2xx)
                        }
                        in 4 until 5 -> {
                            errorState.postValue(State.NetErr4xx)
                        }
                        in 5 until 6 -> {
                            errorState.postValue(State.NetErr5xx)
                        }
                        else -> {
                            errorState.postValue(State.NetErr2xx)

                        }
                    }
                }

            }
        }
    }

    private fun clearDB(_connected: Boolean) {
        if (_connected) {
            viewModelScope.launch {
                db.deleteAll()
            }
        }

    }

    private fun clearAll(_connected: Boolean) {
        mutableNewsList.value = mutableListOf()
        loadedData.clear()
        clearDB(_connected)
    }

    fun refreshData(offset: Int, isLoadingMore: Boolean) {
        mutableConnectionState.value?.let { clearAll(it) }
        loadData(offset, isLoadingMore)
    }

    fun setConnectionStatus(_connected: Boolean) {
        mutableConnectionState.value = _connected
    }


}