package com.example.ryanelliott.mycraigsearch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.security.AccessController.getContext;


public class SelectorActivity extends AppCompatActivity {
    private static final String TAG = "SelectorActivity";
    public static final String ITEM_SELECTED_EXTRA = "Item selected";
    public static final String SAVE_SELECTOR_LIST = "Save the selector list to memory";
    // List of search options
    private String itemsArray[] = {"bar stool* (35|36|37)", "foot stool", "coffee mug"};
    //private List<String> itemsList = new ArrayList<>();
    private ArrayList<String> itemsArrayList = new ArrayList<>();
    private ListView listView;
    private FloatingActionButton fab;
    //private ArrayAdapter<String> arrayAdapter;
    private SelectorAdapter selectorAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    // NOT NEEDED FOR BUTTONS EMBEDED IN LISTVIEW
    //private MenuItem mnuGo;
    //private MenuItem mnuDelete;

    // Long click status/settings
//    boolean showLongClickOptions = false;
//    String longClickItem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        listView = (ListView) findViewById(R.id.selectorListView);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        for (String s : itemsArray) {
            //itemsList.add(s);
            itemsArrayList.add(s);
        }


        selectorAdapter = new SelectorAdapter(SelectorActivity.this, R.layout.selector_advanced, itemsArrayList);
        listView.setAdapter(selectorAdapter);

        // NOTE: ORIGINAL PLAN  WAS TO HAVE A LONG CLICK DISPLAY THE DELETE BUTTON. NOW THE DELETE BUTTON IS DISPLAYED IN THE LISTVIEW
        //  THE ARRAYADAPTER HAS BEEN CHANGED TO A CUSTOM TO ALLOW BUTTONS IN THE LISTVIEW
        //arrayAdapter = new ArrayAdapter<>(SelectorActivity.this,R.layout.selector_textview,itemsList);
        //listView.setAdapter(arrayAdapter);


        // Set an onclick listener for the arraylist
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Store the item selected as a string
//                String itemSelected = (String) parent.getItemAtPosition(position);
//                Log.d(TAG, "onItemClick: " + itemSelected);
//                // go to listings for chosen item
//                goToMainActivity(itemSelected);
//            }
//        });

        // Set an on long click listener to list item. Will display option to delete or advance to item
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                // Set the item long clicked and set the long click status to true
//                longClickItem = (String) parent.getItemAtPosition(position);
//                Log.d(TAG, "onItemLongClick: " + longClickItem);
//                showLongClickOptions = true;
//                MenuItem mnuGo = (MenuItem) findViewById(R.id.mnuSelectorGo);
//
//                mnuGo.setVisible(true);
//                MenuItem mnuDelete = (MenuItem) findViewById(R.id.mnuSelectorDelete);
//                mnuDelete.setVisible(true);
//
//
//
//
//                //
//
//                return true;
//            }
//        });


        // Floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: fab clicked");
                runAddDialog();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // Method for adding items with the fab
    private void runAddDialog() {
        // Create popup window that user can type a new search term into
        //  This term will be appended to the item list and appear in the list view
        String newTerm = "";
        // Inflate the add prompt layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View addPrompt = layoutInflater.inflate(R.layout.add_prompt, null);

        // Create the alert builder that will show the dialog to add a new item
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // Set the prompt to the alert dialog
        alertDialogBuilder.setView(addPrompt);

        final EditText etAdd = (EditText) addPrompt.findViewById(R.id.promptEditText);

        // Set the dialog message
        alertDialogBuilder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { // OK option, save text to list
                // get user input and add it to the list
                itemsArrayList.add(etAdd.getText().toString());
                //Tell the adapter that the list has new info to display
                //arrayAdapter.notifyDataSetChanged();
                selectorAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //Cancel option
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //Create the dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        //Show the dialog
        alertDialog.show();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.selector_menu, menu);
//        //mnuGo = (MenuItem) findViewById(R.id.mnuSelectorGo);
//        //mnuDelete = (MenuItem) findViewById(R.id.mnuSelectorDelete);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case (R.id.mnuSelectorGo):
//                goToMainActivity(longClickItem);
//                break;
//            case (R.id.mnuSelectorDelete):
//                itemsList.remove(itemsList.indexOf(longClickItem));
//                arrayAdapter.notifyDataSetChanged();
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //MIGRATED TO ADAPTER
//    private void goToMainActivity(String selection){
//        // Intent taking user to listings for selected item
//        Intent listingsActivityIntent = new Intent(this, MainActivity.class);
//        listingsActivityIntent.putExtra(ITEM_SELECTED_EXTRA,selection);
//        startActivity(listingsActivityIntent);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        // Initialize set to save on pause
        Set<String> savedSelectorList = new HashSet<String>();
        for (String s : itemsArrayList){
            savedSelectorList.add(s);
        }
        // Save the data
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(SAVE_SELECTOR_LIST, savedSelectorList);

        // save it
        editor.apply();
        Log.d(TAG, "onPause: data saved");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retrieve the string set
        Log.d(TAG, "onResume: retrieving data");
        Set<String> defaultStringSet = new HashSet<String>();
        defaultStringSet.add("Stool");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Set<String> savedSelectorList = sharedPref.getStringSet(SAVE_SELECTOR_LIST,defaultStringSet);

        // process data
        itemsArrayList.clear();
        for (String s : savedSelectorList){
            itemsArrayList.add(s);
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Selector Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
