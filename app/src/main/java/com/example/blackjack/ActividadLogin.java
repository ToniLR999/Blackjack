package com.example.blackjack;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActividadLogin extends Activity  implements Response.Listener<JSONArray>, Response.ErrorListener {
    RequestQueue rq;
    JsonArrayRequest jsonArrayRequest;
    EditText name;
    EditText passwd;
    Button login;
    Button botonRegister;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MediaPlayer altoriesgo = MediaPlayer.create(ActividadLogin.this,R.raw.cancion);
        altoriesgo.start();
        setContentView(R.layout.login);
        login = (Button)findViewById(R.id.button4);
       name = (EditText) findViewById(R.id.nameID);
        passwd = (EditText)findViewById(R.id.passwordID);
        botonRegister = (Button)findViewById(R.id.button5);
        rq = Volley.newRequestQueue(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckIfExists();
                altoriesgo.stop();
            }
        });
        botonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irseaRegistrar();

            }
        });
    }

    private void irseaRegistrar() {
        Intent switchActivityIntent = new Intent(this, ActividadRegister.class);
        startActivity(switchActivityIntent);
    }

    private void CheckIfExists() {
        String url = "http://virtual392.ies-sabadell.cat/virtual392/Moviles/sesion.php?user="+name.getText().toString()+"&pwd="+passwd.getText().toString();
        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,this,this);
        rq.add(jsonArrayRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"No se ha encontrado el usuario o la contrasena esta mal",Toast.LENGTH_SHORT).show();
        System.out.println(error);
    }

    @Override
    public void onResponse(JSONArray response) {
        User usuario = new User();
        try {
            JSONObject jsonObject = response.getJSONObject(0);
            usuario.setName(jsonObject.optString("nombre"));
            usuario.setPwd(jsonObject.optString("password"));
            usuario.setMoney(jsonObject.optString("money"));
            System.out.println(usuario.toString());
            irseAlPlay(usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void irseAlPlay(User usuario)
    {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("user", usuario);
        startActivity(switchActivityIntent);
    }
}