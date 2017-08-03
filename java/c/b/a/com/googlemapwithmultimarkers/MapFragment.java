package c.b.a.com.googlemapwithmultimarkers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;

public class MapFragment extends android.app.Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private MapView mMapView;
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<MarkerOptions> MarkersPosition = new ArrayList<>();
    LatLngBounds.Builder builder;
    private boolean visibility = true;
    //  private ArrayList<MarkerObj> CitiesName = new ArrayList<>();
    HashMap<Marker, MarkerObj> hashMap;

    public static MapFragment newInstance() {

        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = view.findViewById(R.id.mapView);
        setListeners(view);
        setUpMap();
    }

    private void setListeners(View view) {
        Button marker1 = view.findViewById(R.id.marker1);
        Button marker2 = view.findViewById(R.id.marker2);
        Button marker3 = view.findViewById(R.id.marker3);

        marker1.setOnClickListener(this);
        marker2.setOnClickListener(this);
        marker3.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setMapSettings();
        this.googleMap.setOnMarkerClickListener(this);
    }

    private void setUpMap() {
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMapSettings() {
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        this.googleMap.setTrafficEnabled(false);
        this.googleMap.setIndoorEnabled(true);
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void setMarkerLocation(int i) {
        this.googleMap.clear();
        builder = new LatLngBounds.Builder();
        latlngs.clear();
        hashMap = new HashMap<>();

        if (i == 0) {
            latlngs.add(new LatLng(12.334343, 33.43434));
            latlngs.add(new LatLng(17.385044, 38.486671));

        } else if (i == 1) {
            latlngs.add(new LatLng(65.334343, 33.43434));
            latlngs.add(new LatLng(64.385044, 38.486671));
            latlngs.add(new LatLng(64.334343, 36.43434));
            latlngs.add(new LatLng(64.334343, 34.43434));
        } else if (i == 2) {
            latlngs.add(new LatLng(65.334343, 33.43434));

        }

        for (int j = 0; j < latlngs.size(); j++) {
            inflateMarkerAndBindViews("Location " + j, latlngs.get(j));
        }

        if (latlngs.size() > 1)
            setToZoomAllMarkers();
        else
            setZoomToSingleMarker(latlngs.get(0));

    }

    private void setToZoomAllMarkers() {

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
    }


    private void setZoomToSingleMarker(LatLng latLng) {
        CameraUpdate cu = CameraUpdateFactory.newLatLng(latLng);
        googleMap.moveCamera(cu);
    }

    private void inflateMarkerAndBindViews(String storeLocation, LatLng latLng) {

        View viewMarker = ((LayoutInflater) getActivity().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);

        ImageView myImage = viewMarker.findViewById(R.id.imgMarkerIcon);
        TextView textview = viewMarker.findViewById(R.id.txtLocation);
        textview.setText(storeLocation);

        Drawable vectorDrawable = ResourcesCompat.getDrawable(getActivity()
                .getResources(), R.drawable.marker, null);

        Bitmap bitmap = null;
        if (vectorDrawable != null) {
            bitmap = ((BitmapDrawable) vectorDrawable).getBitmap();
        }

        myImage.setImageBitmap(bitmap);
        Bitmap bmp = createDrawableFromView(getActivity(), viewMarker);
        addItToBuilder(bmp, latLng, storeLocation);
    }

    private void addItToBuilder(Bitmap bmp, LatLng latlng, String storeLocation) {
        options.position(latlng);
        options.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        MarkersPosition.add(options);
        Marker marker = this.googleMap.addMarker(options);
        hashMap.put(marker, new MarkerObj(storeLocation, "Marker id"));
        builder.include(latlng);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.marker1:
                setMarkerLocation(0);
                break;
            case R.id.marker2:
                setMarkerLocation(1);
                break;

            case R.id.marker3:
                setMarkerLocation(2);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(), "click on window to get position" + marker.getPosition(), Toast.LENGTH_SHORT).show();
        marker.hideInfoWindow();

        if (visibility) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            visibility = false;
        } else {

            Toast.makeText(getActivity(), "" + marker.getId(), Toast.LENGTH_LONG).show();
            MarkerObj obj = hashMap.get(marker);
            Bitmap bmp = inflateMarkerAndBindViews(marker.getPosition(), obj.getLocationName());
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bmp));
            visibility = true;
        }
        return false;

    }


    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels,
                displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private Bitmap inflateMarkerAndBindViews(LatLng latLng, String Marker) {

        View viewMarker = ((LayoutInflater) getActivity().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);

        ImageView myImage = viewMarker.findViewById(R.id.imgMarkerIcon);
        TextView textview = viewMarker.findViewById(R.id.txtLocation);
        textview.setText(Marker);

        Drawable vectorDrawable = ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.marker, null);

        Bitmap bitmap = null;
        if (vectorDrawable != null) {
            bitmap = ((BitmapDrawable) vectorDrawable).getBitmap();
        }

        myImage.setImageBitmap(bitmap);
        return createDrawableFromView(getActivity(), viewMarker);
    }
}
