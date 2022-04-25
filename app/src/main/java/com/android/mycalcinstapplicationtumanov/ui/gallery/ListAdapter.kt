package com.android.mycalcinstapplicationtumanov.ui.gallery

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.mycalcinstapplicationtumanov.R
import com.android.mycalcinstapplicationtumanov.R.id.ivProfile
import com.android.mycalcinstapplicationtumanov.data.Contact
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target


class ListAdapter(
    private val context: Context, private val contacts : List<Contact>
) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_contact, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val contact = contacts[i]
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(FitCenter(), RoundedCorners(16))
        Glide.with(context)
            .load(contact.imageUrl)
            .apply(requestOptions)
            .apply(RequestOptions()
                .fitCenter()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(Target.SIZE_ORIGINAL))
            .skipMemoryCache(true)//for caching the image url in case phone is offline
            .into(viewHolder.img_android)

    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var img_android: ImageView

        init {
            img_android =
                view.findViewById<View>(ivProfile) as ImageView
        }
    }

}