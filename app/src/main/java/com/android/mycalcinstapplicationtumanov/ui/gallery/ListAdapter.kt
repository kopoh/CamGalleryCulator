package com.android.mycalcinstapplicationtumanov.ui.gallery

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.mycalcinstapplicationtumanov.R
import com.android.mycalcinstapplicationtumanov.data.Contact
import com.bumptech.glide.Glide

class ListAdapter(private val context : Context, private val contacts : List<Contact>)
    : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val TAG = "ContactAdapter"

    // Usually involves inflating a layout from XML and returning the holder - THIS IS EXPENSIVE
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.img_container_fragment, parent, false))
    }

    // Returns the total count of items in the list
    override fun getItemCount() = contacts.size

    // Involves populating data into the item through holder - NOT expensive
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
           Glide.with(context).load(contact.imageUrl).into(g)
        }
    }
}

// Create the adapter by extending RecyclerView.Adapter. This custom ViewHolder will give access to your views
//class ListAdapter(private val context: Context, private val images: List<Contact>):RecyclerView.Adapter<ListAdapter.ViewHolder>() {
//
//
//    // Create a static inner class and provide references to all the Views for each data item.
//    class ViewHolder(inflater : LayoutInflater, itemView : View) : RecyclerView.ViewHolder(itemView) {
//
//        // Declare member variables for all the Views in a row
//        var rowImage : ImageView
//
//        // Create a constructor that accepts the entire row and search the View hierarchy to find each subview
//        init {
//            // Store the item subviews in member variables
//            rowImage = itemView.findViewById(R.id.imageView)
//        }
//        fun bind(contact : Contact) {
//            Glide.with(context).load(contact.imageUrl).into(rowImage)
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))
//    }
//
//
//    // Replace the contents of a view to be invoked by the layout manager
//    /*override fun onBindViewHolder(holder : ViewHolder, position : Int) {
//        // Get element from your dataset at this position and replace the contents of the View with that element
//        holder.rowImage.setImageResource(images[position])
//    }*/
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        //holder.rowImage.setImageBitmap(images[position])
//        val contact = images[position]
//        holder.bind(contact)
//    }
//
//    // Return the size of your dataset
//    //override fun getItemCount(): Int = images.size
//    override fun getItemCount() = images.size
//}












//package com.android.mycalcinstapplicationtumanov.ui.gallery
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//
//class ListAdapter(private val list: List<>) : RecyclerView.Adapter<>() {
//
//    private val TAG = "ListAdapter"
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return TableViewHolder(inflater, parent)
//    }
//
//    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
//        val table: TimeTable = list[position]
//        holder.bind(table)
//    }
//
//    override fun getItemCount(): Int = list.size
//}
//
//class TableViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
//    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false))
//{
//    private var mTimeView: TextView? = null
//    private var mGroupView: TextView? = null
//    private var mNameView: TextView? = null
//    private var mSubjectView: TextView? = null
//    private var mAuditoryView: TextView? = null
//
//    init {
//        mTimeView = itemView.findViewById(R.id.list_time)
//        mGroupView = itemView.findViewById(R.id.list_group)
//        mNameView = itemView.findViewById(R.id.list_name)
//        mSubjectView = itemView.findViewById(R.id.list_subject)
//        mAuditoryView = itemView.findViewById(R.id.list_description)
//    }
//
//    fun bind(table: TimeTable) {
//        mTimeView?.text = timeMap[table.timeId]
//        mGroupView?.text = table.group
//        mNameView?.text = table.teacherName
//        mSubjectView?.text = table.subject
//        mAuditoryView?.text = table.room
//    }
//}