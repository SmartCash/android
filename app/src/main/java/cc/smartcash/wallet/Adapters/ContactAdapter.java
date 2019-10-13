package cc.smartcash.wallet.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cc.smartcash.wallet.R;

public class ContactAdapter extends CursorAdapter {

    int[] indices;

    public ContactAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        indices = new int[]{
                cursor.getColumnIndex(ContactsContract.Contacts._ID),
                cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY),
                cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        };
        return LayoutInflater.from(context).inflate(R.layout.contact_dialog_item, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtNome = view.findViewById(R.id.txtContactName);
        QuickContactBadge quickContactBadge = view.findViewById(R.id.qcbFoto);
        Uri uriContact = ContactsContract.Contacts.getLookupUri(cursor.getLong(indices[0]), cursor.getString(indices[1]));
        txtNome.setText(cursor.getString(indices[2]));
        quickContactBadge.assignContactUri(uriContact);
        Picasso.get().load(uriContact).placeholder(R.mipmap.ic_launcher).into(quickContactBadge);
    }
}
