package com.android.instagram.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.android.instagram.R
import com.android.instagram.di.component.ActivityComponent
import com.android.instagram.ui.base.BaseActivity
import com.android.instagram.ui.dummy.DummyActivity
import com.android.instagram.ui.main.MainActivity
import com.android.instagram.utils.common.Status
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject
import kotlin.math.log

/**
 * Created by Ajay Deepak on 03-07-2019.
 */
class LoginActivity : BaseActivity<LoginViewModel>() {

    @Inject
    lateinit var loginViewModel: LoginViewModel

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {


        et_email.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.onEmailChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        et_password.addTextChangedListener(object : TextWatcher {


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.onPasswordChange(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        })

        login_btn.setOnClickListener { loginViewModel.onLogin() }
    }

    override fun setupObservers() {
        super.setupObservers()

        loginViewModel.launchMain.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })

        loginViewModel.emailField.observe(this, Observer {
            if (et_email.text.toString() != it) et_email.setText(it)
        })

        loginViewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> email_view.error = it.data?.run { getString(this) }
                else -> email_view.isErrorEnabled = false

            }
        })

        loginViewModel.passwordField.observe(this, Observer {
            if (et_password.text.toString() != it) et_password.setText(it)
        })

        loginViewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> password_view.error = it.data?.run { getString(this) }
                else -> password_view.isErrorEnabled = false

            }

        })

        loginViewModel.loggingIn.observe(this, Observer {
            login_progress.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}