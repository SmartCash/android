package cc.smartcash.smarthub.adapters

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.QuickContactBadge
import android.widget.TextView
import cc.smartcash.smarthub.R
import com.squareup.picasso.Picasso

class ContactAdapter(context: Context, c: Cursor) : CursorAdapter(context, c) {

    private var indices: IntArray? = null

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        indices = intArrayOf(cursor.getColumnIndex(ContactsContract.Contacts._ID), cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY), cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        return LayoutInflater.from(context).inflate(R.layout.contact_dialog_item, null)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val txtNome = view.findViewById<TextView>(R.id.txtContactName)
        val quickContactBadge = view.findViewById<QuickContactBadge>(R.id.qcbPhoto)
        val uriContact = ContactsContract.Contacts.getLookupUri(cursor.getLong(indices!![0]), cursor.getString(indices!![1]))
        txtNome.text = cursor.getString(indices!![2])
        quickContactBadge.assignContactUri(uriContact)
        Picasso.get().load(uriContact).placeholder(R.mipmap.ic_launcher).into(quickContactBadge)
    }
}
