package com.android.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.Event
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Ajay Deepak on 10-07-2019.
 */
class MainViewModel(schedulerProvider: SchedulerProvider,
                    compositeDisposable: CompositeDisposable,
                    networkHelper: NetworkHelper)
    :BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val profileNavigation =  MutableLiveData<Event<Boolean>>()
    val homeNavigation = MutableLiveData<Event<Boolean>>()
    val photoNavigation = MutableLiveData<Event<Boolean>>()

    override fun onCreate() {
        homeNavigation.postValue(Event(true))
    }

    fun onHomeSelected(){
        homeNavigation.postValue(Event(true))
    }

    fun onProfileSelected(){
        profileNavigation.postValue(Event(true))
    }

    fun onPhotoSelected(){
        photoNavigation.postValue(Event(true))
    }
}