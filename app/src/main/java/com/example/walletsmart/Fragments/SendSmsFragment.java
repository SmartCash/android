package com.example.walletsmart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.walletsmart.R;

public class SendSmsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_send, container, false);
        return view;
    }

    public static SendSmsFragment newInstance() {
        return new SendSmsFragment();
    }
}
