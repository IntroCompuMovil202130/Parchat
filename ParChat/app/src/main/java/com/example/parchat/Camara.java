package com.example.parchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parchat.databinding.ActivityCamaraBinding;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Camara extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensorProximity;

    private ActivityCamaraBinding binding;

    ImageView image;
    String permisoCamara = Manifest.permission.CAMERA;
    String permisoGaleria = Manifest.permission.READ_EXTERNAL_STORAGE;

    int idCamara = 3;
    int idGaleria = 4;

    private static final int IMAGEN_R = 0;
    private static final int CAMARA_R = 1;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCamaraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        binding.botonColgar.setOnClickListener(v -> onBackPressed());

        if (sensorProximity == null){
            finish();
        }

        image = findViewById(R.id.imagen);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if( event.values[0] < 2.0 ){
                binding.aceptar.setVisibility(View.INVISIBLE);
                binding.frameLayout.setVisibility(View.VISIBLE);
            }else{
                //binding.aceptar.setVisibility(View.VISIBLE);
                binding.frameLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(sensorEventListener, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    //-------------------------------------------



    private void camaraImage(){
        if(ActivityCompat.checkSelfPermission(this,permisoCamara) == PackageManager.PERMISSION_GRANTED){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try{
                startActivityForResult(takePictureIntent, CAMARA_R);
            }catch (ActivityNotFoundException e){
                Log.e("PERMISSION_APP",e.getMessage());
            }
        }
    }

    private void galleryImage(){
        if(ActivityCompat.checkSelfPermission(this,permisoGaleria)== PackageManager.PERMISSION_GRANTED){
            Intent pickImg = new Intent(Intent.ACTION_PICK);
            pickImg.setType("image/*");
            startActivityForResult(pickImg,IMAGEN_R);
        }
    }

    public void cameraPressed(View v){
        if(ActivityCompat.checkSelfPermission(this,permisoCamara)!= PackageManager.PERMISSION_GRANTED){
            requestPermission(this,permisoCamara,"",idCamara);
        }else{
            camaraImage();
        }
    }


    public void galleryPressed(View v){
        if(ActivityCompat.checkSelfPermission(this,permisoGaleria)!= PackageManager.PERMISSION_GRANTED){
            requestPermission(this,permisoGaleria,"",idGaleria);
        }else{
            galleryImage();
        }
    }

    private void requestPermission(Activity context, String permission, String justification, int id){
        if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
                Toast.makeText(context,justification,Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission},id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==idCamara){
            camaraImage();
        }else if(requestCode==idGaleria){
            galleryImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case IMAGEN_R:
                if(resultCode== RESULT_OK){
                    try {
                        final Uri imageUri= data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                        image.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                };
                break;

            case CAMARA_R:
                if(resultCode== RESULT_OK){
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    image.setImageBitmap(imageBitmap);
                }
        }
    }

    public void volver(View v){
        onBackPressed();
    }


}