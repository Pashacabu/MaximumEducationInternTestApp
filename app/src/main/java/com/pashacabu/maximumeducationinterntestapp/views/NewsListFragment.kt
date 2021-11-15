package com.pashacabu.maximumeducationinterntestapp.views

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.pashacabu.maximumeducationinterntestapp.R
import com.pashacabu.maximumeducationinterntestapp.model.db.data_classes.State
import com.pashacabu.maximumeducationinterntestapp.model.network.ConnectionChecker
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem
import com.pashacabu.maximumeducationinterntestapp.view_model.NewsListViewModel
import com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list.NewsListAdapter
import com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list.LoadMoreListener
import com.pashacabu.maximumeducationinterntestapp.views.adapters.news_list.ShowDetailsInterface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NewsListFragment : Fragment() {

    private val newsDetailsInterface = object : ShowDetailsInterface {

        override fun showDetails(news: DataItem, view: View) {
            var fragment =
                requireActivity().supportFragmentManager.findFragmentByTag(resources.getString(R.string.newsDetailsFragment))
            if (fragment == null) {
                fragment = news.url?.let { NewsDetailsFragment.newInstance(it) }
            }
            if (fragment != null) {
                val args = fragment.arguments
                args?.putString(
                    resources.getString(R.string.hostTransitionNameTag),
                    view.transitionName
                )
                fragment.arguments = args
                fragment.sharedElementEnterTransition = MaterialContainerTransform().apply {
                    duration = resources.getInteger(R.integer.shared_element_duration).toLong()
                    fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                    scrimColor = Color.WHITE
                }
                fragment.sharedElementReturnTransition = MaterialContainerTransform().apply {
                    duration = resources.getInteger(R.integer.shared_element_duration).toLong()
                    fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                    scrimColor = Color.WHITE
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(
                        R.id.main_fragment_container,
                        fragment,
                        resources.getString(R.string.newsDetailsFragment)
                    )
                    .addToBackStack(resources.getString(R.string.newsListFragment))
                    .addSharedElement(view, view.transitionName)
                    .commit()
            }

        }

    }


    private val adapter = NewsListAdapter(newsDetailsInterface)
    private lateinit var newsRecycler: RecyclerView
    private val viewModel: NewsListViewModel by viewModels()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var noConnectionWarning: TextView
    private lateinit var loadingIndicator: TextView
    private var offset = 0
    private var page = 0

    @Inject
    lateinit var connectionChecker: ConnectionChecker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        exitTransition = MaterialElevationScale(false).apply {
            duration =
                requireContext().resources.getInteger(R.integer.shared_element_duration).toLong()
        }
        return inflater.inflate(R.layout.news_list_fragment_layout, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeConnection()
        findViews(view)
        observeViewModel()
        viewModel.loadData(offset, false)
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun observeConnection() {
        connectionChecker.observe(this.viewLifecycleOwner, {
            viewModel.setConnectionStatus(it)
        })
    }

    private fun findViews(view: View) {
        noConnectionWarning = view.findViewById(R.id.no_connection_warning)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        swipeRefresh = view.findViewById(R.id.news_list_swipe_refresh)
        swipeRefresh.setOnRefreshListener { refresh() }
        newsRecycler = view.findViewById(R.id.news_list_recyclerView)
        newsRecycler.adapter = adapter
        when (requireContext().resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                newsRecycler.layoutManager = LinearLayoutManager(requireContext()).apply {
                    this.orientation = LinearLayoutManager.VERTICAL
                }
            }
            else -> {
                newsRecycler.layoutManager = GridLayoutManager(requireContext(), 2).apply {
                    this.orientation = GridLayoutManager.VERTICAL
                }
            }
        }
        val loadMoreListener = LoadMoreListener {
            page += 1
            offset += 25 * page
            viewModel.loadData(offset, true)
            adapter.setLoading(true)
        }
        newsRecycler.addOnScrollListener(loadMoreListener)
    }

    private fun observeViewModel() {
        viewModel.newsList.observe(this.viewLifecycleOwner, {
            adapter.submitList(it)
            adapter.setLoading(false)
        })
        viewModel.liveLoadingState.observe(this.viewLifecycleOwner, {
            loadingIndicator(it)
        })
        viewModel.liveConnectionState.observe(this.viewLifecycleOwner, {
            when (it) {
                true -> {
                    noConnectionWarning.visibility = View.GONE
                }
                else -> {
                    noConnectionWarning.visibility = View.VISIBLE
                }
            }
        })
        viewModel.liveErrorState.observe(this.viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                it.message,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun loadingIndicator(loading: Boolean?) {
        when (loading) {
            true -> {
                showLoading()
            }
            false -> {
                hideLoading()
            }
            else -> {
            }
        }
    }

    private fun showLoading() {
        loadingIndicator.animate().translationY(-300F).apply {
            duration = 200
            interpolator = OvershootInterpolator(5f)
        }.withEndAction {
            flickering(loadingIndicator)
        }
    }

    private fun flickering(view: View) {
        Log.d("List", "Flickering")
        view.animate().alpha(0.5f).apply {
            duration = 800
            interpolator = CycleInterpolator(2f)
        }.withEndAction { flickering(view) }
    }

    private fun hideLoading() {
        loadingIndicator.animate().translationY(0F).apply {
            duration = 200
            interpolator = AnticipateInterpolator()
        }.start()
    }

    private fun refresh() {
        swipeRefresh.isRefreshing = true
        page = 0
        offset = 0
        viewModel.refreshData(offset, false)
        swipeRefresh.isRefreshing = false
    }
}