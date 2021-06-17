package com.example.blackjack;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class addUser  extends StringRequest {
    private static final String ruta = "http://virtual392.ies-sabadell.cat/virtual392/Moviles/agregarUser.php"; // "http://10.1.131.139/niko/put_usuario.php";
    private Map<String, String> parametros;  // parametros que contentdra los valores del registro
    public addUser (String nombre, String pass1, String pass2, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null); // LA peticion es post
        parametros = new HashMap<>();
        parametros.put("nombre",nombre);
            parametros.put("password",pass1);
            parametros.put("money","1000");
    }
    public addUser (String nombre, String pass1, String pass2, Response.Listener<String> listener,String laRuta,String money){
        super(Request.Method.POST, laRuta, listener, null);
        parametros = new HashMap<>();
        parametros.put("nombre",nombre);
        parametros.put("password",pass1);
        parametros.put("money",money);
    }

    // Sobreescribimos map  para asegurarnos que nos devuelve la información de uestros parametros
    @Override
    protected Map<String, String> getParams() {
        return parametros;
    }
}
