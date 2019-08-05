package com.android.instagram.ui.main

import androidx.lifecycle.MutableLiveData
import com.android.instagram.data.model.Post
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.Event
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Ajay Deepak on 30-07-2019.
 */

class MainSharedViewModel(schedulerProvider: SchedulerProvider,
                          compositeDisposable: CompositeDisposable,
                          networkHelper: NetworkHelper
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    override fun onCreate() {
    }

    val homeRedirection: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val newPost: MutableLiveData<Event<Post>> = MutableLiveData()

    fun onHomeRedirect() {
         homeRedirection.postValue(Event(true))
    }
}