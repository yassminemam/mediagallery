package com.nagwa.mediagallery.ui.main.adapter

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.nagwa.mediagallery.R
import com.nagwa.mediagallery.data.model.Media
import java.io.File
import java.util.*


class MediaListAdapter(
    private val mList: ArrayList<Media>,
) : RecyclerView.Adapter<MediaListAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.media_item, parent, false)

        return ViewHolder(view, parent.context)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemMedia = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.idTextView.text = itemMedia.id.toString()
        holder.typeTextView.text = itemMedia.type
        holder.nameTextView.text = itemMedia.name
        holder.bindDownloadBtnClick(itemMedia)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View, val context: Context) :
        RecyclerView.ViewHolder(ItemView) {
        val idTextView: TextView = itemView.findViewById(R.id.item_id)
        val typeTextView: TextView = itemView.findViewById(R.id.item_type)
        val nameTextView: TextView = itemView.findViewById(R.id.item_name)
        val downloadBTN: TextView = itemView.findViewById(R.id.btn_download)
        fun bindDownloadBtnClick(item: Media) {
            downloadBTN.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(
                    Uri.fromFile(File(item.name)),
                    item.type.lowercase(
                        Locale.getDefault()
                    )
                )
                downloadFile(
                    url = item.url.trim(),
                    fileName = item.name.replace(" ", ""),
                )

            }
        }

        fun downloadFile(url: String, fileName: String) {
            try {
                if (url.isNotEmpty()) {
                    val downloadManager =
                        context.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val uri = Uri.parse(url)
                    val request = DownloadManager.Request(uri)
                    request.setTitle(fileName)
                    request.setDescription("Downloading attachment..")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        fileName
                    )
                    val file = downloadManager.enqueue(request)
                    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
                        @SuppressLint("Range")
                        override fun onReceive(context: Context, intent: Intent) {
                            val action = intent.action
                            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                                val query = DownloadManager.Query()
                                query.setFilterById(file)
                                val c: Cursor = downloadManager.query(query)
                                if (c.moveToFirst()) {
                                    val columnIndex =
                                        c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                        val fileUri = downloadManager.getUriForDownloadedFile(file)
                                        val fileType =
                                            downloadManager.getMimeTypeForDownloadedFile(file)
                                        openFile(fileUri)
                                    }
                                }
                            }
                        }
                    }
                    context.registerReceiver(
                        receiver,
                        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun openFile(fileUri: Uri) {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    fileUri
                )
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.data = fileUri
                startActivity(context, intent, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun addData(list: List<Media>) {
        mList.addAll(list)
    }
}