package com.nagwa.mediagallery.ui.main.view

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nagwa.mediagallery.R
import com.nagwa.mediagallery.data.model.Media
import com.nagwa.mediagallery.data.repo.local.MediaRepoHelper
import com.nagwa.mediagallery.ui.base.ViewModelFactory
import com.nagwa.mediagallery.ui.main.adapter.MediaListAdapter
import com.nagwa.mediagallery.ui.main.viewmodel.MainViewModel
import com.nagwa.mediagallery.utils.Status
import androidx.core.app.ActivityCompat




class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MediaListAdapter
    private lateinit var listRecyclerView:RecyclerView
    private lateinit var loadingProgressBar:ProgressBar
    private val PERMISSION_REQUEST_CODE = 190
    private val filePath:String = "media_list.json"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupViewModel()
        setupObserver()
        requestPermission()
    }
    private fun setupUI() {
        listRecyclerView = findViewById(R.id.listRecyclerView)
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MediaListAdapter(arrayListOf())
        listRecyclerView.addItemDecoration(
            DividerItemDecoration(
                listRecyclerView.context,
                (listRecyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        listRecyclerView.adapter = adapter

        loadingProgressBar = findViewById(R.id.loadingProgressBar)
    }

    private fun setupObserver() {
        mainViewModel.getList().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loadingProgressBar.visibility = View.GONE
                    it.data?.let { users -> renderList(users) }
                    listRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    loadingProgressBar.visibility = View.VISIBLE
                    listRecyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    loadingProgressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(list: List<Media>) {
        adapter.addData(list)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(MediaRepoHelper(), applicationContext, filePath)
        ).get(MainViewModel::class.java)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }
}