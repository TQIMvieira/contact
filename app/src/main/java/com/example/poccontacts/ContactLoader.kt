package com.example.poccontacts

import android.content.Context
import android.provider.ContactsContract
import androidx.paging.DataSource
import androidx.paging.PagedList
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import java.util.*

class ContactLoader(val context: Context?) {

    fun load(): Observable<PagedList<Contact>> {
        return Observable.create { emitter -> getContacts(emitter) }
    }

    private fun getContacts(emitter: ObservableEmitter<PagedList<Contact>>) {
        val contacts = getContacts()
        contacts.let {
            emitter.onNext(contacts)
        }
        emitter.onComplete()
    }

    private fun getContacts(): DataSource.Factory<Int, Contact> {
        val list = DataSource.Factory<Int, Contact>()
        val cur = context?.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE ASC"
        )

        cur?.let {
            if (cur.count > 0) {
                while (cur.moveToNext()) {
                    val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                    if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt() > 0) {
                        val pCur = context?.contentResolver?.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id.toString()),
                            null
                        )
                        pCur?.let {
                            while (pCur.moveToNext()) {
                                val phoneNumber =
                                    pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                list.add(Contact(id = id, name = name, phone = phoneNumber))
                            }
                            pCur.close()
                        }
                    }
                }
            }
            cur.close()
        }
        return list
    }
}