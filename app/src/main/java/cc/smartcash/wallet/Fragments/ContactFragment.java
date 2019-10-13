package cc.smartcash.wallet.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import cc.smartcash.wallet.Adapters.ContactAdapter;

public class ContactFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String[] COLUMNS = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME
    };

    private CursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (adapter == null) {
            adapter = new ContactAdapter(getActivity(), null);
            setListAdapter(adapter);
            getActivity().getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(position);

        Uri uri = ContactsContract.Contacts.getLookupUri(cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));

        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                COLUMNS,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
                null,
                ContactsContract.Contacts.DISPLAY_NAME
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
