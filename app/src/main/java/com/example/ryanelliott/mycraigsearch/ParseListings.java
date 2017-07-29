package com.example.ryanelliott.mycraigsearch;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ryand on 10/4/2016.
 */

public class ParseListings {
    private static final String TAG = "ParseListings";
    // Create an arraylist to hold the listing objects that have been parsed
    private ArrayList<Listing> listings;

    public ParseListings() {
        // Constructor initializes list
        this.listings = new ArrayList<>();
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    public void setListings(ArrayList<Listing> listings) {
        this.listings = listings;
    }

    // THE PARSE METHOD
    public boolean parse(String xmlData){
        // Method to parse the xmlData into listing objects. Adds a listing to the array list after parsing
        boolean status = true;
        boolean inItem = false; // Keeps track of wether the parser is in an item tag or not
        Listing currentListing = null;
        String tempText = "";
        Log.d(TAG, "parse: Starting Parse Method");

        try {
            // Crate a pull parser factory
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // Set the factories namespace awareness to true
            factory.setNamespaceAware(true);
            // Create an xml pull parser with the factory
            XmlPullParser xpp = factory.newPullParser();
            // Set the parser's input
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();

            // Parse through document untill the endType is END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT){
                // Get the name of the current tag being parsed
                String tagName = xpp.getName();
                //Log.d(TAG, "parse: tagname " + tagName);
                // switch to handle each parser event
                switch (eventType){
                    // If the event is start tag and the tag is item, set the inItem tracker to true
                    //  and initialize a new instance of currentListing
                    case XmlPullParser.START_TAG:
                        if ("item".equalsIgnoreCase(tagName)){
                            inItem = true;
                            currentListing = new Listing();
                        }
                        break;
                    // If the event is text, temporarily save it in the tempText string
                    case XmlPullParser.TEXT:
                        tempText = xpp.getText();
                        break;
                    // If we reach the end of one of the tags we want while inItem, save the text
                    case XmlPullParser.END_TAG:
                        if (inItem){
                            // If the endtag is reached for item, set inItem to false and add the listing to the array
                            //Log.d(TAG, "parse: Storing " + tagName);
                            if ("item".equalsIgnoreCase(tagName)){
                                inItem = false;
                                listings.add(currentListing);
                            } else if ("title".equalsIgnoreCase(tagName)){
                                // if any tag we are looking for is found in the item, add it to our current listing object

                                if (tempText.indexOf("&#x") > 0){
                                    currentListing.setTitle(tempText.substring(0,tempText.indexOf("&#x")));
                                } else {
                                    currentListing.setTitle(tempText);
                                }

                            } else if ("link".equalsIgnoreCase(tagName)){
                                currentListing.setPostLink(tempText);
                            } else if ("description".equalsIgnoreCase(tagName)){
                                currentListing.setDescription(tempText);
                            } else if ("date".equalsIgnoreCase(tagName)){
                                currentListing.setDatePosted(tempText);
                            } else if ("enclosure".equalsIgnoreCase(tagName)){
                                // If there is an image, store it
                                String imgURL =  xpp.getAttributeValue(0);
                                currentListing.setImageLink(imgURL);
                            }
                        }
                }

                // Next event
                eventType = xpp.next();
            }
        } catch (Exception e){
            Log.e(TAG, "parse: Error" + e.getMessage() );
            e.printStackTrace();
        }
        return status;
    }
}
