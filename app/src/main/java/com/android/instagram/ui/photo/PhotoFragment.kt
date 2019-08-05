package com.android.instagram.ui.photo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.instagram.R
import com.android.instagram.di.component.FragmentComponent
import com.android.instagram.ui.base.BaseFragment
import com.android.instagram.ui.main.MainSharedViewModel
import com.android.instagram.utils.common.Event
import com.android.instagram.utils.log.Logger
import com.mindorks.paracamera.Camera
import kotlinx.android.synthetic.main.fragment_photo.*
import java.io.FileNotFoundException
import javax.inject.Inject

/**
 * Created by Ajay Deepak on 10-07-2019.
 */
class PhotoFragment : BaseFragment<PhotoViewModel>() {


    companion object {

        const val TAG = "Photo Fragment"
        const val RESULT_GALLERY_IMG = 1001

        fun newInstance(): PhotoFragment {
            val args = Bundle()
            val fragment = PhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    @Inject
    lateinit var camera: Camera

    override fun provideLayoutId() = R.layout.fragment_photo

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(this, Observer {
            it.getIfNotHandled()?.run {
                mainSharedViewModel.newPost.postValue(Event(this)) // new post is been added
                mainSharedViewModel.onHomeRedirect() // redirected to home fragment
            }
        })

    }

    override fun setupView(view: View) {

        view_camera.setOnClickListener {
            try {
                camera.takePicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        view_gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RESULT_GALLERY_IMG)
                }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_GALLERY_IMG -> {
                    Logger.d("DEBUG", requestCode.toString())
                    try {
                        intent?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage("try again")
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage("try again")
                    }

                }
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
            }
        }
    }
}