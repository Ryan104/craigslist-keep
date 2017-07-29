package com.example.ryanelliott.mycraigsearch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by ryand on 10/4/2016.
 */

public class ListingAdapter extends ArrayAdapter {
    // The adapter to be used by the list view

    private static final String TAG = "ListingAdapter";

    // Constructor will get values for the layout resource (xml layout), the list of listing objects to populate the layout with,
    //  and the layout inflator which is created from the context parameter
    private final int layoutResource;
    private final LayoutInflater layoutInflater;

    private ArrayList<Listing> listings;

    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListingAdapter(Context context, int resource, ArrayList<Listing> listings) {
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.listings = listings;
    }

    @Override
    public int getCount() {
        // getCount needs to return the size of the list passed
        return listings.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize a viewholder
        Log.d(TAG, "getView: called");
        ViewHolder viewHolder;
        // If the listView has given us a convertView, retrieve the viewHolder stored in it's tag
        //  If not, create a new viewHolder for the current view by inflating the current view
        // //  and put it in the view's tag
        if (convertView == null){
            // Create the current view with our layout inflator
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Get the current listing in the list
        final Listing currentListing = listings.get(position);

        // Set the content of each widget using the view holder
        viewHolder.tvTitle.setText(currentListing.getTitle());
        viewHolder.tvDate.setText(currentListing.getDatePosted());
        viewHolder.tvDescription.setText(currentListing.getDescription());
        viewHolder.ivImage.setImageResource(R.color.colorPendingImage); //Reset to default while loading image
        viewHolder.btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),currentListing.getPostLink(),Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: " + currentListing.getPostLink());
                Uri url = Uri.parse(currentListing.getPostLink());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW,url);
                getContext().startActivity(launchBrowser);
            }
        });


        // Use image loader library to load images
        imageLoader.displayImage(currentListing.getImageLink(),viewHolder.ivImage);

        return convertView;

    }

    private class ViewHolder {
        // View Holder to optimize the creation of views
        final TextView tvTitle;
        final TextView tvDate;
        final TextView tvDescription;
        final ImageView ivImage;
        final Button btnLink;


        public ViewHolder(View v) {
            this.tvTitle = (TextView) v.findViewById(R.id.itemTitle);
            this.tvDate = (TextView) v.findViewById(R.id.itemDate);
            this.tvDescription = (TextView) v.findViewById(R.id.itemDescription);
            this.ivImage = (ImageView) v.findViewById(R.id.itemImage);
            this.btnLink = (Button) v.findViewById(R.id.itemLink);

        }


    }
}
