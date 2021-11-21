package com.example.parchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email,password;
    public static final String TAG = "FirebaseAuth";
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            updateUI();
        }
    }

    private void updateUI(){
        Intent intent = new Intent(MainActivity.this, Match.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void registrarse(View v){
        startActivity(new Intent(v.getContext(),Registrarse.class));
    }

    public void recContrasena(View v){
        startActivity( new Intent(v.getContext(), RecuperarContrasena.class) );
    }

    public void iniciarSesion(View v){

        String emails = email.getText().toString();
        String pass = password.getText().toString();

        if(validateForm(emails, pass)){
            mAuth.signInWithEmailAndPassword(emails,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i(TAG,"Log in sucessfull for the user: " + emails);
                        updateUI();
                    }else{
                        Log.e(TAG,"Login failed: "+ task.getException().toString());
                        Toast.makeText(MainActivity.this,"Error de inicio de sesion",Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                    }
                }
            });
        }
    }
    private boolean validateForm(String em,String passw) {
        boolean valid = true;

        if (TextUtils.isEmpty(em)) {
            Toast.makeText(MainActivity.this,"Debe ingresar un email",Toast.LENGTH_SHORT).show();

            valid = false;

        }
        if (TextUtils.isEmpty(passw)) {
            Toast.makeText(MainActivity.this,"Debe ingresar una contraseña",Toast.LENGTH_SHORT).show();

            valid = false;
        }
        valid = isEmailValid(em);
        return valid;
    }

    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||!email.contains(".") || email.length() < 5) {
            Toast.makeText(MainActivity.this,"Email invalido",Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }
}
