package com.nagwa.mediagallery.ui.main.adapter
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nagwa.mediagallery.R
import com.nagwa.mediagallery.data.model.Media
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.lang.Exception

class MediaListAdapter(private val mList: ArrayList<Media>, private val downloadM :DownloadManager) : RecyclerView.Adapter<MediaListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)

        return ViewHolder(view, downloadM)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.idTextView.text = itemsViewModel.id.toString()
        holder.typeTextView.text = itemsViewModel.type
        holder.nameTextView.text = itemsViewModel.name
        holder.bindDownloadBtnClick(itemsViewModel.url)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, val downloadM :DownloadManager) : RecyclerView.ViewHolder(ItemView) {
        val idTextView: TextView = itemView.findViewById(R.id.item_id)
        val typeTextView: TextView = itemView.findViewById(R.id.item_type)
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val downloadBTN: TextView = itemView.findViewById(R.id.btn_download)
        fun bindDownloadBtnClick(url:String){
            downloadBTN.setOnClickListener {
                val uri: Uri =
                    Uri.parse(url.trim())
                try {
                    val request = DownloadManager.Request(uri)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    val reference: Long = downloadM.enqueue(request)
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }

            }
        }
    }

    fun addData(list: List<Media>) {
        mList.addAll(list)
    }
}