package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cc.smartcash.wallet.R;

public class SendEmailFragment extends Fragment {

    public static final String TAG = SendEmailFragment.class.getSimpleName();

    public static SendEmailFragment newInstance() {
        return new SendEmailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email_send, container, false);
    }
}