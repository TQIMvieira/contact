package com.example.poccontacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvContact: RecyclerView
    lateinit var contactsList: LiveData<PagedList<Contact>>
    lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contactAdapter = ContactAdapter()

        rvContact = findViewById(R.id.rv_contact)
        rvContact.adapter = contactAdapter
        rvContact.layoutManager = LinearLayoutManager(this)
        rvContact.isNestedScrollingEnabled = false

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            REQUEST_CODE_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val config = PagedList.Config.Builder()
                .setPageSize(30)
                .setPrefetchDistance(60)
                .setEnablePlaceholders(false)
                .build()
            contactsList = LivePagedListBuilder(ContactsDataSourceFactory(application), config).build()

            val observer: Observer<PagedList<Contact>> = Observer { t -> contactAdapter.submitList(t) }
            contactsList.observe(this, observer)
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSION = 1337
    }
}