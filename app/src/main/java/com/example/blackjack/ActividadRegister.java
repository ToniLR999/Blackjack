package com.example.blackjack;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;


public class ActividadRegister extends Activity {
    EditText nombre;
    EditText password;
    EditText password2;
    Button botonRegistrar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        nombre = (EditText)findViewById(R.id.RegisterNameId);
        password = (EditText)findViewById(R.id.RegisterPassword1);
        password2 = (EditText)findViewById(R.id.RegisterConfirmpassword);
        botonRegistrar = (Button)findViewById(R.id.button6);
        final Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonRepuesta = new JSONObject(response);
                    boolean ok =jsonRepuesta.getBoolean("success");
                    if (ok) {
                        Toast.makeText(getApplicationContext(),"Usuario creado.", LENGTH_LONG).show();
                        irseAlLogin();
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(getApplicationContext());
                        alerta.setMessage("Fallo en el registro")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                        Toast.makeText(getApplicationContext(),"ERROR.Algo ha fallado en la inserción!!!!", LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            String pass1 = password.getText().toString();
            String pass2 = password2.getText().toString();
            @Override
            public void onClick(View v) {
                if(nombre.getText() !=null && password.getText() !=null && password2 !=null){

                    if(pass1.equals(pass2)){
                        addUser r= new addUser(nombre.getText().toString(), password.getText().toString(), password2.getText().toString(), respuesta );
                        RequestQueue cola = Volley.newRequestQueue(ActividadRegister.this);
                        cola.add(r);
                    }else{
                        System.out.println(password.getText().toString());
                        System.out.println(password2.getText().toString());
                        Toast.makeText(getApplicationContext(),"Las contrasenas no coinciden", LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void irseAlLogin() {
        Intent switchActivityIntent = new Intent(this, ActividadLogin.class);
        startActivity(switchActivityIntent);
    }
}
