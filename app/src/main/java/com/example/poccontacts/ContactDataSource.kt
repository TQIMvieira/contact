/*
 * Copyright 2018 Carmen Alvarez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.poccontacts

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.paging.PositionalDataSource

class ContactDataSource(val application: Application) : PositionalDataSource<Contact>() {

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

    fun readContacts(limit: Int, offset: Int): MutableList<Contact> {

        val cursor = application.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY +
                    " ASC LIMIT " + limit + " OFFSET " + offset
        )

        val list = mutableListOf<Contact>()
        cursor?.let {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val number = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                list.add(Contact(id, name, number))
                cursor.moveToNext()
            }
            cursor.close()
        }

        return list
    }
}