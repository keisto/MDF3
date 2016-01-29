package com.tonykeiser.photomapping;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

/** Created By : Tony Keiser MDF3 TERM: 1601 **/
public class MapsFragment extends MapFragment implements GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener, android.location.LocationListener,
        GoogleMap.OnMapClickListener {

    private static final int REQUEST_ENABLE_GPS = 1;
    private GoogleMap mapView;
    private Location location;
    private LatLng myLatLng;
    private LocationManager myLocationManager;
    private Boolean paused = false;
    private ArrayList<PhotoMaps> photoMaps = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photoMaps = readData();
        mapView = getMap();
        getLocationService();

        // Add Saved Makers to map
        for (PhotoMaps item : photoMaps) {
            if (item.getFavorite()) {
                String favorite = "Favorite: Yes";
                mapView.addMarker(new MarkerOptions().position
                        (new LatLng(item.getLatitude(),
                                item.getLongitude())).title(item.getTitle()).snippet(favorite)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else {
                String favorite = "Favorite: No";
                mapView.addMarker(new MarkerOptions().position
                        (new LatLng(item.getLatitude(),
                                item.getLongitude())).title(item.getTitle()).snippet(favorite));
            }
        }
        mapView.setInfoWindowAdapter(new MapMarker());
        mapView.setOnInfoWindowClickListener(this);
        mapView.setOnMapLongClickListener(this);
        mapView.setOnMapClickListener(this);
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        // Get Item & Go to Details Page
        for (PhotoMaps item : photoMaps) {
            if (item.getTitle().equals(marker.getTitle())){
                Bundle clickedMaker = new Bundle();
                LatLng clickedLatLng = marker.getPosition();
                clickedMaker.putString  ("title",     item.getTitle());
                clickedMaker.putBoolean ("favorite",  item.getFavorite());
                clickedMaker.putString  ("photo",     item.getPhoto());
                clickedMaker.putDouble  ("latitude",  clickedLatLng.latitude);
                clickedMaker.putDouble  ("longitude", clickedLatLng.longitude);
                FragmentTransaction ft = this.getFragmentManager().beginTransaction();
                DetailFragment frag = new DetailFragment();
                frag.setArguments(clickedMaker);
                ft.replace(R.id.fragContainer, frag);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Get Clicked Coordinates and go to form
        Bundle coordinates = new Bundle();
        coordinates.putDouble("latitude", latLng.latitude);
        coordinates.putDouble("longitude", latLng.longitude);
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        FormFragment frag = new FormFragment();
        frag.setArguments(coordinates);
        ft.replace(R.id.fragContainer, frag);
        ft.addToBackStack(null);
        ft.commit();
    }
    @Override
    public void onMapClick(LatLng latLng) {
        // Get Clicked Coordinates and go to form
        Bundle coordinates = new Bundle();
        if(myLatLng != null) {
            coordinates.putDouble("latitude", myLatLng.latitude);
            coordinates.putDouble("longitude", myLatLng.longitude);
            FragmentTransaction ft = this.getFragmentManager().beginTransaction();
            FormFragment frag = new FormFragment();
            frag.setArguments(coordinates);
            ft.replace(R.id.fragContainer, frag);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15);
        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mapView.addMarker(new MarkerOptions().position
//                (new LatLng(location.getLatitude(), location.getLongitude()))
//                .title("Home Base"));
        mapView.animateCamera(cameraUpdate);
    }

    private class MapMarker implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoContents(Marker marker) {

            View v  = getActivity().getLayoutInflater().inflate(R.layout.marker_window, null);

            for (PhotoMaps item : photoMaps) {
                if (item.getTitle().equals(marker.getTitle()))
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Marker Clicked")
                            .setMessage(item.getTitle() + "\n" +
                                    "Lat: " + item.getLatitude() + "\n" +
                                    "Long: " + item.getLongitude());
                TextView title = ((TextView) v.findViewById(R.id.titleText));
                title.setText(marker.getTitle());
                TextView favorite = ((TextView) v.findViewById(R.id.favoriteText));
                favorite.setText(marker.getSnippet());
                ImageView icon = ((ImageView) v.findViewById(R.id.imageIcon));
                icon.setImageURI(Uri.parse(item.getPhoto()));
            }
            return v;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

    // Load Serializable Data
    public ArrayList readData() {
        ArrayList<PhotoMaps> photoMaps = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(
                    new File(getActivity().getApplicationContext().getFilesDir(),"")+
                            File.separator+"photos.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            photoMaps = (ArrayList<PhotoMaps>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoMaps;
    }

    public void getLocationService() {
        // Check Permissions if success, get current location
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapView.setMyLocationEnabled(true);
            myLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            String provider = myLocationManager.getBestProvider(criteria, false);
            if (provider != null && !provider.equals("")) {
                // Get the location from the given provider
                location = myLocationManager.getLastKnownLocation(provider);
                myLocationManager.requestLocationUpdates(provider, 50000, 1, MapsFragment.this);
            }
            if (location != null) {
                // Add a marker to current location and move camera to it
                LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
                mapView.addMarker(new MarkerOptions().position(coordinates).title("Home Base"));
                mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
            } else {
                // Request permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("GPS Unavailable")
                        .setMessage("Please enable GPS in the system settings.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsIntent =
                                        new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                            }
                        })
                        .show();
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        paused = true;
        //  getLocationService();
    }

    @Override
    public void onResume() {
        super.onResume();
        paused = false;
        // getLocationService();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
