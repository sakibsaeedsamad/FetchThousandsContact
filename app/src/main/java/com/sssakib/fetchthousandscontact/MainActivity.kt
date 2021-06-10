package com.sssakib.fetchthousandscontact

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sssakib.mobilerechargecloneapp.Contact
import com.sssakib.mobilerechargecloneapp.ContactAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ContactAdapter.OnItemClickListener {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    private var model: MutableList<Contact>? = null
    private var adapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestContactPermission()

        model = getContacts()
        adapter = ContactAdapter(model!!, this)
        adapter?.notifyDataSetChanged()
        contactRecyclerView.layoutManager = LinearLayoutManager(this)
        contactRecyclerView.adapter = adapter


        phoneNumberOrNameET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {

                adapter?.filter?.filter(s)
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


    }

    private fun requestContactPermission() {

//        Dexter.withActivity(this)
//            .withPermission(Manifest.permission.READ_CONTACTS)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//                    // permission is granted
//                    getContacts()
//                }
//
//                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                    // check for permanent denial of permission
//                    if (response.isPermanentlyDenied) {
//                        showSettingsDialog()
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: PermissionRequest?,
//                    token: PermissionToken
//                ) {
//                    token.continuePermissionRequest()
//                }
//            }).check()


        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            //builder =
            getContacts()
            //listContacts.text = builder.toString()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            } else {
                requestContactPermission()
            }
        }
    }

//    private fun getContacts(): MutableList<Contact>? {
//        val contactList: MutableList<Contact> = ArrayList()
//        var no: String? = null
//        val contacts = contentResolver.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            null,
//            null,
//            null,
//            null
//        )
//        while (contacts!!.moveToNext()) {
//            val name =
//                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//            val number =
//                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//
//            if (number.length == 11) {
//                no = number
//            }
//            if (number.length == 14) {
//                no = number.subSequence(3, 14).toString()
//            } else {
//                no = number
//            }
//
//            val obj = Contact(0, name, no)
//
//            contactList.add(obj)
//        }
//
//        return contactList
//        contacts.close()
//    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onItemClickListener(contact: Contact) {
        val uName = contact.name
        val uNumber = contact.number


        phoneNumberOrNameET.setText(uNumber)
    }

    private fun getContacts(): MutableList<Contact>? {
        val contactList: MutableList<Contact> = ArrayList()
        var no: String? = null
        val cr = contentResolver

        val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {
                val nameIndex: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var name: String
                var number: String
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex)
                    number = cursor.getString(numberIndex)
                    if (number.length == 11) {
                        no = number
                    }
                    if (number.length == 14) {
                        no = number.subSequence(3, 14).toString()
                    } else {
                        no = number
                    }

                    val obj = Contact(0, name, no)

                    contactList.add(obj)
                }
            } finally {
                cursor.close()
            }
        }

        return contactList
        cursor?.close()
    }


}