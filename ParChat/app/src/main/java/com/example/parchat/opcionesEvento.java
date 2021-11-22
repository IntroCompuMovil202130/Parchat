package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parchat.databinding.ActivityOpcionesEventoBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class opcionesEvento extends FragmentActivity implements OnMapReadyCallback {
    //permisos y ids
    private static final String permisosMapa = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int SOLICITUD_GPS = 1;
    public static final int MAPAID = 2;

    //cuadros de texto
    private TextView busqueda;
    private TextView nombreEvento;
    private TextView fecha;
    private Button eliminar;
    private Button editar;
    private Evento ev;

    //mapa y localizacion
    private GoogleMap mMap;
    private @NonNull ActivityOpcionesEventoBinding binding;
    private Marker busquedaMarker;
    private Marker miUbicacion;
    private double latitud;
    private double longitud;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    //sensor de luminosidad
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private boolean otroUsuario = true;
    private boolean regEnEvento = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        latitud = 0;
        longitud = 0;

        binding = ActivityOpcionesEventoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        busqueda = findViewById(R.id.barraBuscar);
        nombreEvento = findViewById(R.id.nEvento);
        fecha = findViewById(R.id.fecha);
        eliminar = findViewById(R.id.imageButton12);
        editar = findViewById(R.id.imageButton11);
        ev = new Evento();

        mLocationCallback = createLocationCallBack();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = crearListener();

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("eventos");

        revisarGPS();
        solicitarPermisos(this, permisosMapa, "Acceso a GPS");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        cargarDatos();
        iniciarActLocalizacion();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
        detenerActLocalizacion();
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
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        colocarPuntoReunion();
    }

    public void colocarPuntoReunion(){
        LatLng posicion = new LatLng(ev.latitud,ev.longitud);
        busquedaMarker = mMap.addMarker(new MarkerOptions()
                .position(posicion).title(ev.lugar)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void editarEvento(View v){
        Intent intent = new Intent(v.getContext(),editarEvento.class);
        intent.putExtra("eventoM", ev);
        startActivity(intent);
        finish();
    }

    public void eliminarEvento(View v){
        String usuarioKey = mAuth.getCurrentUser().getUid();
        myRef.child(usuarioKey).child(ev.id).removeValue();
        finish();
    }

   //---------cargar los datos del evento-------------------
    private void cargarDatos(){

        Bundle extras = getIntent().getExtras();
        ev = (Evento) extras.getSerializable("eventoM");
        busqueda.setText(ev.lugar);
        nombreEvento.setText(ev.nombreEvento);
        fecha.setText(ev.fecha);

        otroUsuario = extras.getBoolean("oUsuario");
        verificarParticipacion(otroUsuario);
        permisosEdicion(otroUsuario);
    }

    private void permisosEdicion(boolean otroUsuario) {
        if(otroUsuario){
            binding.imageButton11.setVisibility(View.GONE);
            binding.imageButton12.setVisibility(View.GONE);
            if(!regEnEvento){
                binding.unirseAEvento.setVisibility(View.VISIBLE);
            }
            else{
                binding.unirseAEvento.setVisibility(View.GONE);
            }
        }
        else if(ev.organizador){
            binding.imageButton11.setVisibility(View.VISIBLE);
            binding.imageButton12.setVisibility(View.VISIBLE);
            binding.unirseAEvento.setVisibility(View.GONE);
        }
        else{
            binding.imageButton11.setVisibility(View.GONE);
            binding.imageButton12.setVisibility(View.GONE);
            binding.unirseAEvento.setVisibility(View.GONE);
        }
    }

    public void verificarParticipacion(boolean otroUsuario){

        String miId = mAuth.getCurrentUser().getUid();
       if(otroUsuario){
           Bundle extras = getIntent().getExtras();
           String usuarioId = extras.getString("oUsuId");
           myRef = FirebaseDatabase.getInstance().getReference("eventos").child(usuarioId).child(ev.id);
       }
       else{
           myRef = FirebaseDatabase.getInstance().getReference("eventos").child(miId).child(ev.id);
       }
       myRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               Evento evento = snapshot.getValue(Evento.class);
               for (Map.Entry<String,Posicion> entry : evento.participantes.entrySet()){
                   if(entry.getKey().equals(miId)){
                    regEnEvento = true;
                   }
               }
           }
           @Override
           public void onCancelled(@NonNull DatabaseError error) {}
       });
    }

    public void unirseAEvento(View v){
        Bundle extras = getIntent().getExtras();
        String usuarioId = extras.getString("oUsuId");
        myRef = FirebaseDatabase.getInstance().getReference("eventos").child(usuarioId).child(ev.id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String miId = mAuth.getCurrentUser().getUid();
                Evento evento = snapshot.getValue(Evento.class);
                Log.i("eventoO", evento.participantes.toString());
                if(latitud != 0 & longitud != 0){
                    Posicion pos = new Posicion();
                    pos.latitud = latitud;
                    pos.longitud = longitud;
                    evento.participantes.put(miId,pos);
                    myRef.setValue(evento);


                    Toast.makeText(opcionesEvento.this, "Te has unido al evento", Toast.LENGTH_SHORT).show();

                }
                myRef = FirebaseDatabase.getInstance().getReference("eventos").child(miId);
                HashMap<String, Object> nuevoEvento = new HashMap<>();
                evento.organizador = false;
                nuevoEvento.put(evento.id,evento);
                myRef.updateChildren(nuevoEvento);
                startActivity(new Intent(opcionesEvento.this,Perfil.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //-----sensor de luminosidad-------------------------
    private SensorEventListener crearListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(mMap != null){
                    if(sensorEvent.values[0] < 10){
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(opcionesEvento.this,R.raw.modo_oscuro));
                    }
                    else{
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(opcionesEvento.this,R.raw.modo_claro));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
    }
    //-----MiUbicacion-----------------------------------------------

    private LocationRequest createLocationRequest() {
        return LocationRequest.create()
                .setInterval(6000)
                .setFastestInterval(4000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private LocationCallback createLocationCallBack() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if(locationResult.getLastLocation() != null){
                    Location locacion = locationResult.getLastLocation();
                    latitud = locacion.getLatitude();
                    longitud = locacion.getLongitude();

                    if(miUbicacion != null){
                        miUbicacion.remove();
                    }
                    LatLng miU = new LatLng(latitud,longitud);
                    miUbicacion = mMap.addMarker(new MarkerOptions().position(miU).title("mi ubicacion"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(miU));
                }
            }
        };
    }
    //-------------------acceder a la ubicacion--------------------------------

    private void solicitarPermisos(Activity crearEvento, String permisosMapa, String acceso) {
        if (ContextCompat.checkSelfPermission(crearEvento,permisosMapa) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(crearEvento,permisosMapa)){
                Toast.makeText(this, "No se podra mostrar su ubicación actual", Toast.LENGTH_SHORT).show();
            }
        }
        ActivityCompat.requestPermissions(crearEvento, new String[]{permisosMapa},MAPAID);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MAPAID){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }
    //---------------------Permisos de GPS--------------------------------------

    private void revisarGPS(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                iniciarActLocalizacion();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int codigostatus = ((ApiException)e).getStatusCode();
                switch (codigostatus){
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(opcionesEvento.this,SOLICITUD_GPS);
                        } catch (IntentSender.SendIntentException sendIntentException) {

                        }break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SOLICITUD_GPS){
            if (resultCode == RESULT_OK) {
                iniciarActLocalizacion();
            }else {
                Toast.makeText(this, "no podre mostrar su ubicación actual", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarActLocalizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        }
    }

    private void detenerActLocalizacion(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}