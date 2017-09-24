package edu.wpi.alcoholcontextualizer.fragments.location;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.alcoholcontextualizer.R;
import edu.wpi.alcoholcontextualizer.database.DatabaseHandler;
import edu.wpi.alcoholcontextualizer.model.Location;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrequentLocationFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    DatabaseHandler dbHandler;

    private Marker mSelectedMarker;

    // Heatmap Variables
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;


    public FrequentLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return mView = inflater.inflate(R.layout.fragment_location_frequent, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
//        GoogleMap map = ((SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.map)).getMapAsync();
        //map.getMapAsync(this);

        mMapView = (MapView) mView.findViewById(R.id.freq_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHandler = new DatabaseHandler(this.getActivity());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        //additional map settings
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        addHeatMap();
        setUpMarkers(googleMap);

        // Zoom in on location set
        // padding between map edge and point
        int padding = 200;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(makeMapLocationBounds(getLocations()),
                padding);
        googleMap.moveCamera(cu);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (null != mSelectedMarker) {
            mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        }
        mSelectedMarker = marker;
        mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mSelectedMarker.showInfoWindow();
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (null != mSelectedMarker) {
            mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mSelectedMarker = null;

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(this, "Info window clicked",
//                Toast.LENGTH_SHORT).show();
    }

    // Create a heat map tile provider, passing it the latlngs
    private void addHeatMap() {

        // Create the color gradient.
        int[] colors = {
                Color.rgb(255, 225, 255), // white
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(makeMapDataFromLocation(getLocations()))
                .gradient(gradient)
                .build();

        mProvider.setRadius(45);

        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    /**
     * @param locations - list of location objects
     * @return array list of locations in weighted lat long format
     */
    private List<WeightedLatLng> makeMapDataFromLocation(List<Location> locations) {
        ArrayList<WeightedLatLng> list = new ArrayList<>();
        for (Location loc : locations) {
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            list.add(new WeightedLatLng(latLng, loc.getFrequencyAmount()));
        }

        return list;
    }

    /**
     * @return
     */
    private List<Location> getLocations() {
        return dbHandler.getAllLocations();

    }

    /**
     * Creates the bounds, based on the list of locations, for map to zoom in on.
     *
     * @param locList
     * @return
     */
    private LatLngBounds makeMapLocationBounds(List<Location> locList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Location location : locList) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        return bounds;

    }

    private void setUpMarkers(GoogleMap gMap) {
        List<Location> list = getLocations();
        for (Location location : list) {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            gMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(location.getLocationName())
                    .snippet("Number of Drinks: " + location.getFrequencyAmount())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }
}
