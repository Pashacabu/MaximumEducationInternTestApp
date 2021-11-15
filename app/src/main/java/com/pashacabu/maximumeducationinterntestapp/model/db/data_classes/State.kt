package com.pashacabu.maximumeducationinterntestapp.model.db.data_classes

import android.content.res.Resources
import com.pashacabu.maximumeducationinterntestapp.MaximumEducationInternTestApp
import com.pashacabu.maximumeducationinterntestapp.R

sealed class State(_message: String){

    val message = _message



    object NoNetDBLoad : State(MaximumEducationInternTestApp.instance.getString(R.string.no_network_DB_load))
    object BasicNetworkError : State(MaximumEducationInternTestApp.instance.getString(R.string.net_err_basic))
    object NetErr1xx : State(MaximumEducationInternTestApp.instance.getString(R.string.net_err_1xx))
    object NetErr2xx : State(MaximumEducationInternTestApp.instance.getString(R.string.net_err_2xx))
    object NetErr4xx : State(MaximumEducationInternTestApp.instance.getString(R.string.net_err_4xx))
    object NetErr5xx : State(MaximumEducationInternTestApp.instance.getString(R.string.net_err_5xx))
}
