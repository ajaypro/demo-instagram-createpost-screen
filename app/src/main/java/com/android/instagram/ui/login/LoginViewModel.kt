package com.android.instagram.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.instagram.data.repository.UserRepository
import com.android.instagram.ui.base.BaseViewModel
import com.android.instagram.utils.common.*
import com.android.instagram.utils.network.NetworkHelper
import com.android.instagram.utils.rx.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Ajay Deepak on 03-07-2019.
 */
class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDispoable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDispoable, networkHelper) {


    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()

    // dummyvalidation for controlling navigation
    val launchMain: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    // below 5 mutablelivedat to control UI
    val emailField: MutableLiveData<String> = MutableLiveData()
    val passwordField: MutableLiveData<String> = MutableLiveData()
    val loggingIn: MutableLiveData<Boolean> = MutableLiveData()

    // Both email and password will be updated once validationlist is update which will again run the
    // filtervalidation()

    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)


    private fun filterValidation(field: Validation.Field) =

        // the transformations will run once again when validationList(mutablelivedata) is validated

        Transformations.map(validationList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run this.resource }
                ?: Resource.unknown()
        }

    fun onEmailChange(email: String) = emailField.postValue(email)

    fun onPasswordChange(password: String) = passwordField.postValue(password)

    fun onLogin() {

        val email = emailField.value
        val password = passwordField.value

        val validations = Validator.validationFields(email, password)

        //updating the mutablelivedata of the validationlist
        validationList.postValue(validations)

        if (validations.isNotEmpty() && email != null && password != null) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                loggingIn.postValue(true) // progress bar loading shows
                compositeDisposable.addAll(
                    userRepository.doLoginCall(email, password)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            userRepository.saveCurrentUser(it)
                            loggingIn.postValue(false)
                            launchMain.postValue(Event(emptyMap()))

                        },
                            {
                                handleNetworkError(it)
                                loggingIn.postValue(false)

                            })


                )
            }
        }
    }


   /* override fun onCleared() {
        super.onCleared()
    }

    override fun forcedLogoutUser() {
        super.forcedLogoutUser()
    }*/

    override fun onCreate() {}
}