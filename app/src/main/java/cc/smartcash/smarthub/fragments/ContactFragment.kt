package cc.smartcash.smarthub.fragments

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.CursorAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import cc.smartcash.smarthub.Adapters.ContactAdapter

class ContactFragment : ListFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var adapter: CursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (adapter == null) {
            adapter = ContactAdapter(activity!!, null!!)
            listAdapter = adapter
            activity!!.supportLoaderManager.initLoader(0, null, this)
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val cursor = adapter!!.cursor
        cursor.moveToPosition(position)

        val uri = ContactsContract.Contacts.getLookupUri(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)))

        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
                activity!!,
                ContactsContract.Contacts.CONTENT_URI,
                COLUMNS,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null,
                ContactsContract.Contacts.DISPLAY_NAME
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        adapter!!.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter!!.swapCursor(null)
    }

    companion object {

        private val COLUMNS = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME)
    }
}
