package com.certified.verityscanningOne.Utils;

import android.location.Location;

import com.certified.verityscanningOne.ReportActivity;
import com.certified.verityscanningOne.beans.Locationbean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceJSONParser {
    String TAG = "PlaceJSONPArser";

    /**
     * Receives a JSONObject and returns a list
     */
    public List<HashMap<String, String>> parse(JSONObject jObject, String type) {

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        /** Invoking getPlaces with the array of json object
         * where each json object represent a place
         */
        return getPlaces(jPlaces, type);
    }


    private List<HashMap<String, String>> getPlaces(JSONArray jPlaces, String type) {
        int placesCount = jPlaces.length();
        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> place = null;

        if (placesCount == 0) {

        } else {
            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i), type);

                    placesList.add(place);

                } catch (JSONException e) {
                    e.printStackTrace();
                    CommonMethods.printFirebaseLogcat(TAG, e, null);

                }
            }

        }


        return placesList;
    }

    /**
     * Parsing the Place JSON object
     */
    private HashMap<String, String> getPlace(JSONObject jPlace, String type) {

        HashMap<String, String> place = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "0.00";
        String longitude = "0.00";


        try {
            // Extracting Place name, if available
            if (!jPlace.isNull("name")) {
                placeName = jPlace.getString("name");
            }

            // Extracting Place Vicinity, if available
            if (!jPlace.isNull("vicinity")) {
                vicinity = jPlace.getString("vicinity");
            }

            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");


            Locationbean locationbean = new Locationbean();
            locationbean.setLocationName(placeName + " : " + vicinity);
            locationbean.setLatitude(latitude);
            locationbean.setLongitude(longitude);


            String placeiD = jPlace.getString("id");
            locationbean.setPlaceIDFromGoolge(placeiD);
            locationbean.setType(type);

            Location selected_location = new Location("locationA");


            selected_location.setLatitude(ReportActivity.lat);
            selected_location.setLongitude(ReportActivity.longi);
            Location near_locations = new Location("locationA");




            double newlat = Double.parseDouble(String.format("%.2f", Double.parseDouble(latitude)));
            double newlong = Double.parseDouble(String.format("%.2f", Double.parseDouble(longitude)));


            near_locations
                    .setLatitude(newlat);
            near_locations
                    .setLongitude(newlong);


            double distance = selected_location
                    .distanceTo(near_locations);
            double newdistance = Double.parseDouble(String.format("%.2f", distance));

            locationbean.setDistance(newdistance);



            if (placeName.toLowerCase().replaceAll("\\s", "").indexOf(type.toLowerCase().replaceAll("\\s", "")) != -1) {

                System.out.println("I found the keyword");
                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);

                ReportActivity.locationbeanArrayList_taken.add(locationbean);
            } else {

                System.out.println("not found");
                ReportActivity.locationbeanArrayList_ignored.add(locationbean);

            }


            if (ReportActivity.stringArrayListHashMap_LocationWise.containsKey(type)) {
                ArrayList<Locationbean> locationbeanArrayList = ReportActivity.stringArrayListHashMap_LocationWise.get(type);
                locationbeanArrayList.add(locationbean);
                ReportActivity.stringArrayListHashMap_LocationWise.remove(type);
                ReportActivity.stringArrayListHashMap_LocationWise.put(type, locationbeanArrayList);


            } else {
                ArrayList<Locationbean> locationbeanArrayList = new ArrayList<>();
                locationbeanArrayList.add(locationbean);
                ReportActivity.stringArrayListHashMap_LocationWise.put(type, locationbeanArrayList);

            }


        } catch (JSONException e) {
            e.printStackTrace();
            CommonMethods.printFirebaseLogcat(TAG, e, null);

        }
        return place;
    }

    public double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}
