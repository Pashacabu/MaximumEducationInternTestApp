package com.pashacabu.maximumeducationinterntestapp.views

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.pashacabu.maximumeducationinterntestapp.R
import com.pashacabu.maximumeducationinterntestapp.model.network_response.DataItem
import com.pashacabu.maximumeducationinterntestapp.view_model.NewsDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsDetailsFragment : Fragment(R.layout.news_details_fragment) {

    private val viewModel: NewsDetailsViewModel by viewModels()
    private lateinit var url: String

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var desc: TextView
    private var news: DataItem? = null

    private lateinit var hostTransitionName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString("url") ?: ""
        findViews(view)
        viewModel.loadNewsData(url)
        observeViewModel()

    }

    private fun findViews(view: View) {
        hostTransitionName =
            arguments?.getString(resources.getString(R.string.hostTransitionNameTag)) ?: ""
        view.transitionName = hostTransitionName
        image = view.findViewById(R.id.newsDetailsImage)
        image.setOnClickListener {
            val manager = requireActivity().supportFragmentManager
            var fr = manager.findFragmentByTag(resources.getString(R.string.newsGalleryFragment))
            if (fr == null) {
                fr = GalleryFragment.newInstance(news?.image)
            }
            fr.sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration = resources.getInteger(R.integer.shared_element_duration).toLong()
                scrimColor = Color.TRANSPARENT
                fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                fitMode = MaterialContainerTransform.FIT_MODE_HEIGHT
            }
            fr.sharedElementReturnTransition = MaterialContainerTransform().apply {
                duration = resources.getInteger(R.integer.shared_element_duration).toLong()
                scrimColor = Color.TRANSPARENT
                fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
                fitMode = MaterialContainerTransform.FIT_MODE_AUTO
            }
            manager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(
                    R.id.main_fragment_container,
                    fr,
                    resources.getString(R.string.newsGalleryFragment)
                )
                .addSharedElement(image, image.transitionName)
                .addToBackStack(resources.getString(R.string.newsDetailsFragment))
                .commit()

        }
        title = view.findViewById(R.id.newsDetailsTitle)
        desc = view.findViewById(R.id.newsDetailsDesc)
    }

    private fun observeViewModel() {
        viewModel.liveNewsData.observe(this.viewLifecycleOwner, {
            showData(it)
            news = it
        })
    }

    private fun showData(_news: DataItem?) {

        Glide.with(requireContext())
            .load(_news?.image)
            .placeholder(R.drawable.news_placeholder)
            .error(R.drawable.news_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    image.transitionName = news?.image + "tn"
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    image.transitionName = news?.image + "tn"
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(image)


        title.text = _news?.title
        desc.text = _news?.description
    }

    companion object {
        fun newInstance(url: String): NewsDetailsFragment {
            val args = Bundle()
            args.putString("url", url)
            val fr = NewsDetailsFragment()
            fr.arguments = args
            return fr
        }
    }
}