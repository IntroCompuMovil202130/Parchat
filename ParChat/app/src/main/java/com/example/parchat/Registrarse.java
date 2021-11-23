package com.example.parchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registrarse extends AppCompatActivity {

    EditText textEmail;
    EditText textPassword;
    EditText textConfirmPass;

    private String email = "";
    private String password = "";
    private String conPass = "";


    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        mAuth = FirebaseAuth.getInstance();

        textEmail = findViewById(R.id.editText2);
        textPassword = findViewById(R.id.contraseña);
        textConfirmPass = findViewById(R.id.contraseña_conf);

        progressDialog = new ProgressDialog(this);

    }

    public void iniciarSesion(View v){
        onBackPressed();
    }

    public void registrarse(View v){

        email = textEmail.getText().toString().trim();
        password = textPassword.getText().toString().trim();
        conPass = textConfirmPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(Registrarse.this,"Debe ingresar un email",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(Registrarse.this,"Debe ingresar una contraseña",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(conPass)){
            Toast.makeText(Registrarse.this,"Debe confirmar su contraseña",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()>=6){
            if(verificarPass(password,conPass) && isEmailValid(email)){
                registro();
            }
        }else{
            Toast.makeText(Registrarse.this,"La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
        }
    }


    private boolean verificarPass(String pass, String confpass){
        if(pass.equals(confpass)){
            return true;
        }else{
            Toast.makeText(Registrarse.this,"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean isEmailValid(String email) {
        if (!email.contains("@") ||!email.contains(".") || email.length() < 5) {
            Toast.makeText(Registrarse.this,"Email no valido",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registro(){
        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                HashMap<String, String> data = new HashMap<>();
                data.put("perfil", "0");
                database = FirebaseDatabase.getInstance().getReference("Users/" + mAuth.getCurrentUser().getUid());
                database.setValue(data);
                mAuth.signOut();
                Intent intent = new Intent(Registrarse.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(Registrarse.this,"No se pudo registrar este usuario",Toast.LENGTH_SHORT).show();
            }
        });
    }
}