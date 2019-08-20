package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cc.smartcash.wallet.R;

public class SendEmailFragment extends Fragment {

    public static SendEmailFragment newInstance() {
        return new SendEmailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_send, container, false);
        return view;
    }
}