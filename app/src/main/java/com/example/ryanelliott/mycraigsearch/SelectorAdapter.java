package com.example.ryanelliott.mycraigsearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryand on 10/10/2016.
 */

public class SelectorAdapter extends ArrayAdapter {

    private static final String TAG = "SelectorAdapter";
    public static final String ITEM_SELECTED_EXTRA = "Item selected";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;

    private ArrayList<String> itemsList;

    public SelectorAdapter(Context context, int resource, ArrayList<String> itemsList) {
        super(context, resource);
        this.itemsList = itemsList;
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // View holder
        ViewHolder viewHolder;

        if (convertView == null){
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final String currentSelection = itemsList.get(position);

        viewHolder.itemName.setText(currentSelection);
        viewHolder.itemGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: current selection: " + currentSelection);
                goToMainActivity(currentSelection);
            }
        });
        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: DELETING: " + currentSelection);
                itemsList.remove(itemsList.indexOf(currentSelection));
                notifyDataSetChanged();

            }
        });



        return convertView;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    private class ViewHolder {
        final TextView itemName;
        final Button itemDelete;
        final Button itemGo;

        public ViewHolder(View view) {
            this.itemName = (TextView) view.findViewById(R.id.tvSvItemName);
            this.itemDelete = (Button) view.findViewById(R.id.btnSvDel);
            this.itemGo = (Button) view.findViewById(R.id.btnSvGo);
        }
    }

    private void goToMainActivity(String selection){
        // Intent taking user to listings for selected item
        Intent listingsActivityIntent = new Intent(getContext(), MainActivity.class);
        listingsActivityIntent.putExtra(ITEM_SELECTED_EXTRA,selection);
        getContext().startActivity(listingsActivityIntent);
    }
}
