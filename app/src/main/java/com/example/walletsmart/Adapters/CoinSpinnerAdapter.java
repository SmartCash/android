package com.example.walletsmart.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.walletsmart.Models.Coin;

import java.util.ArrayList;

public class CoinSpinnerAdapter extends ArrayAdapter<Coin> {

    private Context context;
    private ArrayList<Coin> coins;

    public CoinSpinnerAdapter(Context context, int position, ArrayList<Coin> coins) {
        super(context, position, coins);
        this.context = context;
        this.coins = coins;
    }

    @Override
    public int getCount(){
        return coins.size();
    }

    @Override
    public Coin getItem(int position){
        return coins.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(coins.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(coins.get(position).getName());

        return label;
    }
}