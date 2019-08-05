package com.android.instagram.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.instagram.R
import com.android.instagram.di.component.FragmentComponent
import com.android.instagram.ui.base.BaseFragment
import com.android.instagram.ui.photo.ProfileViewModel

/**
 * Created by Ajay Deepak on 10-07-2019.
 */

class ProfileFragment: BaseFragment<ProfileViewModel>() {


    companion object{
        const val TAG = "Profile Fragment"

        fun newInstance(): ProfileFragment{
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId() = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {}
}