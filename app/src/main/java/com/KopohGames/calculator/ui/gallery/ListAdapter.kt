package com.KopohGames.calculator.ui.gallery

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.KopohGames.calculator.R
import com.KopohGames.calculator.R.id.ivProfile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL


class ListAdapter(
    private val context: Context, private val Uri : List<Uri>
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_contact, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val Uri = Uri[i]
        Glide.with(context)
            .load(Uri)
            .apply(RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(SIZE_ORIGINAL))
            .transform(RoundedCorners(12))
            .into(viewHolder.img_android)



    /*Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .apply(RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(viewHolder.img_android)*/

    }

    override fun getItemCount(): Int {
        return Uri.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_android: ImageView

        init {
            img_android =
                view.findViewById<View>(ivProfile) as ImageView
        }
    }

}