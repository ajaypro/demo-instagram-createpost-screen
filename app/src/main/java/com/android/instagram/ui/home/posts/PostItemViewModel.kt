package com.android.instagram.ui.home.posts

import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Ajay Deepak on 16-07-2019.
 */

class PostItemViewModel(schedulerProvider: SchedulerProvider,
                    compositeDisposable: CompositeDisposable,
                    networkHelper: NetworkHelper)
:BaseViewModel(schedulerProvider, compositeDisposable, networkHelper){

    override fun onCreate() {

    }
}