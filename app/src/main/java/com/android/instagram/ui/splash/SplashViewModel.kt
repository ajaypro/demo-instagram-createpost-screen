package com.android.instagram.ui.splash

import androidx.lifecycle.MutableLiveData
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.Event
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class SplashViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    // Event is used by the view model to tell the activity to launch another Activity
    // view model also provided the Bundle in the event that is needed for the Activity
    val launchDummy: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    override fun onCreate() {
        // Empty Bundle passed to Activity in Event that is needed by the other Activity
        // Here in actual application we will decide which screen to open based on
        // either the user is logged in or not

        if (userRepository.getCurrentUser() != null) {
            launchDummy.postValue(Event(emptyMap()))
        } else {
            launchLogin.postValue(Event(emptyMap()))
        }

    }
}