package com.puj4pks.parchat;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.puj4pks.parchat.databinding.ActivityEventoBinding;

import java.io.IOException;
import java.util.List;

public class Evento extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityEventoBinding binding;
    private EditText lugar;
    private Marker searchMarker;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bogota = new LatLng(4.65, -74.05);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        this.lugar = findViewById(R.id.lugar);
        this.lugar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if( i == EditorInfo.IME_ACTION_SEARCH){
                    String address = lugar.getText().toString();
                    LatLng position = searchByName(address);
                    if( position != null & mMap != null ){
                        if(searchMarker != null ) searchMarker.remove();
                        searchMarker = mMap.addMarker( new MarkerOptions()
                                .position(position).title(address)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                    }
                }
                return false;
            }
        });
    }

    public LatLng searchByName(String name){
        LatLng position = null;
        if( !name.isEmpty() ){
            try {
                List<Address> addresses = geocoder.getFromLocationName(name, 2);
                if( addresses != null && !addresses.isEmpty() ){
                    Address addressResult = addresses.get(0);
                    position = new LatLng( addressResult.getLatitude(), addressResult.getLongitude() );
                }else{
                    Toast.makeText(this, "Direccion no encontrada", Toast.LENGTH_LONG);
                }
            }catch ( IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "La direccion esta vacia", Toast.LENGTH_LONG);
        }
        return position;
    }
}