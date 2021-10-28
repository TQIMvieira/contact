package com.example.poccontacts

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.ContactsContract
import androidx.paging.PositionalDataSource

class ContactDataSource(context: Context) : PositionalDataSource<Contact>() {
    private val contentResolver: ContentResolver = context.contentResolver
    private var cursor: Cursor? = null

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Contact>) {
        val contacts = readContacts(params.requestedStartPosition, params.requestedLoadSize)
        callback.onResult(contacts, params.requestedStartPosition, cursor?.count ?: 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Contact>) {
        val contacts = readContacts(params.startPosition, params.loadSize)
        callback.onResult(contacts)
    }

    override fun invalidate() {
        super.invalidate()
        cursor?.close()
    }

    fun readContacts(startPosition: Int, loadSize: Int): MutableList<Contact> {
        if (cursor == null) {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                arrayOf(BaseColumns._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " NOT NULL",
                null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
        }
        val contacts = mutableListOf<Contact>()
        cursor?.let {
            for (i in startPosition until (loadSize + startPosition)) {
                if (it.moveToPosition(i)) {
                    val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
//                    val number = it.getString(2)
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    contacts.add(Contact(id, name, ""))
                }
            }
        }
        return contacts
    }
}