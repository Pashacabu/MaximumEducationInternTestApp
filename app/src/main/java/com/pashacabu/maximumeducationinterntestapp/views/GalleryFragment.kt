package com.pashacabu.maximumeducationinterntestapp.views


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.aghajari.zoomhelper.ZoomHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.pashacabu.maximumeducationinterntestapp.R

class GalleryFragment : Fragment() {

    private lateinit var bigImage: ImageView
    private var url = ""
    private var counter = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        url = arguments?.getString("url") ?: ""
        return inflater.inflate(R.layout.gallery_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        loadImage()
    }

    private fun loadImage() {
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.news_placeholder)
            .error(R.drawable.news_placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
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
                    startPostponedEnterTransition()
                    return false
                }

            })
            .into(bigImage)
    }


    private fun findViews(view: View) {
        bigImage = view.findViewById(R.id.bigImage)
        bigImage.transitionName = url + "tn"
        ZoomHelper.addZoomableView(bigImage)
    }


    companion object {
        fun newInstance(image: String?): GalleryFragment {
            val args = Bundle()
            args.putString("url", image)
            val fr = GalleryFragment()
            fr.arguments = args
            return fr
        }
    }
}
