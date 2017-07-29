package com.example.ryanelliott.mycraigsearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ITEM_SELECTED_EXTRA = "Item selected";
    // Initialize tags for instance state
    public static final String SELECTED_SEARCH_TERM = "Currently selected url search term";
   // public static final String CURRENT_MENU_OPTIONS = "Current menu options available";

    // Initialize the search

    private String urlSearchTerm = "bar%20stool%2A%20%2834%7C35%7C36%29%22";
    // Initialize the menu options
    //private String menuItemsList[] = {"bar stool* (35|36|37)", "foot stool", "coffee mug"};
    // Initialize the listView widget
    private ListView listListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the intent
        try {
            Intent intent = getIntent();
            Log.d(TAG, "onCreate: Intent Recieved: " + intent.getStringExtra(ITEM_SELECTED_EXTRA));
            urlSearchTerm = URLEncoder.encode(intent.getStringExtra(ITEM_SELECTED_EXTRA), "UTF-8");
        } catch (UnsupportedEncodingException e){
            Log.e(TAG, "onCreate: Error encoding URL from intent extra... " + e.getMessage() );
        }
        if (savedInstanceState != null) {
           // menuItemsList = savedInstanceState.getStringArray(CURRENT_MENU_OPTIONS);
            urlSearchTerm = savedInstanceState.getString(SELECTED_SEARCH_TERM);
        }
        setContentView(R.layout.activity_main);

        //Initialize imageloader (default settings) [https://github.com/nostra13/Android-Universal-Image-Loader]
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        listListings = (ListView) findViewById(R.id.listView);
        String urlBase = "https://denver.craigslist.org/search/sss?format=rss&query=";
        downloadUrl(urlBase + urlSearchTerm);
    }

    private void downloadUrl(String url) {
        // This method crates an instance of the DownloadData class and starts the download using
        //  it's .execute(string urlBase) method
        // downloadData's onPostExecute method is automatically called after the data is downloaded
        Log.d(TAG, "downloadUrl: Starting Download");
        //Create instance of DownloadData class
        DownloadData downloadData = new DownloadData();
        downloadData.execute(url);
        Log.d(TAG, "downloadUrl: Done");
    }

    // Class for performing async download of XML data
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... params) {
            // Call download xml method and return a string containing the xml file
            // Calling downloadData.execute(urlBase) will call this method. params[0] will contain the search urlBase
            String rssFeed = downloadXML(params[0]);
            Log.d(TAG, "doInBackground: Starting URL=" + params[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String xmlData) {
            super.onPostExecute(xmlData);
            Log.d(TAG, "onPostExecute: Started");
            // Parse and display the data
            ParseListings parseListings = new ParseListings();
            boolean parseStatus = parseListings.parse(xmlData);
            Log.d(TAG, "onPostExecute: parse completion status: " + parseStatus);
            // Create an instance of the listing adapter
            ListingAdapter listingAdapter = new ListingAdapter(MainActivity.this, R.layout.list_item, parseListings.getListings());
            Log.d(TAG, "onPostExecute: adapter created");
            // Assign the adapter to the listview
            listListings.setAdapter(listingAdapter);

            Log.d(TAG, "onPostExecute: done");

        }


        private String downloadXML(String urlPath) {
            // This method creates a urlBase with the string, opens a connection to the urlBase, buffers the
            //  received data and stores it in a string builder. It returns the data in a string
            StringBuilder xmlResult = new StringBuilder();

            try {
                // Generate the URL object
                URL url = new URL(urlPath);
                // Open the connection and log the response
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.d(TAG, "downloadXML: HTTP Response Code: " + connection.getResponseCode());
                // Create a buffer reader from a stream reader reading the connection
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Initialize character array for buffer
                char[] inputBuffer = new char[500];
                // Initialize an int to record how many characters have been read
                int charsRead;
                // Loop reader.read(charArray) method until it returns -1 for charsRead
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        // Break loop when the end of the document is reached
                        // Dont break when 0 chars are returned because the method may return 0 before the end
                        break;
                    } else if (charsRead > 0) {
                        // Store the buffered chars in the string builder
                        // Dont append entire inputBuffer because it may not be filled intirely each time
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                // Close the connection
                reader.close();
                // Return the xml as a string if everything worked
                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Bad URL... " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: Connection IO Error... " + e.getMessage());
            } catch (SecurityException e) {
                // Catch security exception. Probably permisison issue
                Log.e(TAG, "downloadXML: Security exception: check permisisons.." + e.getMessage());
            }
            // Return null if there were any problems
            return null;
        }
    }


    // MENU REMOVED
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Create the menu
//        getMenuInflater().inflate(R.menu.listings_menu, menu);
//        for (String s : menuItemsList) {
//            menu.add(s);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle menu selection
//        // Convert the menu text to UTF-8 and set the url search term
//        // Run downloadURL
//        int id = item.getItemId();
//        String itemText = item.getTitle().toString();
//
//        try {
//            urlSearchTerm = URLEncoder.encode(itemText, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG, "onOptionsItemSelected: URL Encoding error: " + e.getMessage());
//            return super.onOptionsItemSelected(item);
//        }
//        downloadUrl(urlBase + urlSearchTerm);
//        return true;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putStringArray(CURRENT_MENU_OPTIONS, menuItemsList);
        outState.putString(SELECTED_SEARCH_TERM, urlSearchTerm);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO: Save the users settings (search term list)
    }
}
