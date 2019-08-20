package cc.smartcash.wallet.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cc.smartcash.wallet.R;

public class SendSmsFragment extends Fragment {
    public static SendSmsFragment newInstance() {
        return new SendSmsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_send, container, false);
        return view;
    }
}
