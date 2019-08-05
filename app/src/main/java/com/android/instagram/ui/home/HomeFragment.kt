package com.android.instagram.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.instagram.R
import com.android.instagram.di.component.FragmentComponent
import com.android.instagram.ui.base.BaseFragment
import com.android.instagram.ui.home.posts.PostAdapter
import com.android.instagram.ui.main.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * Created by Ajay Deepak on 10-07-2019.
 */
class HomeFragment : BaseFragment<HomeViewModel>() {


    companion object {

        const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var mainSharedViewModel: MainSharedViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var postsAdapter: PostAdapter

    override fun provideLayoutId(): Int = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.loading.observe(this, Observer {
            progressBar.visibility = if(it) View.VISIBLE else View.GONE
        })


        mainSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run { viewModel.onNewPost(this) }
        })

        viewModel.posts.observe(this, Observer {
            it.data?.run {
                postsAdapter.appendData(this) }
        })

        viewModel.refreshPosts.observe(this, Observer {
            it.data?.run {
                postsAdapter.updateList(this)
                rvPosts.scrollToPosition(0)
            }
        })
    }

    override fun setupView(view: View) {
        rvPosts.apply {
            layoutManager = linearLayoutManager
            adapter = postsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager
                            && itemCount > 0
                            && itemCount == findLastVisibleItemPosition() + 1
                        )
                            viewModel.onLoadMore()
                    }
                }
            })
        }
    }

}