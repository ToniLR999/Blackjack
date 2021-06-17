package com.example.blackjack;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class addGame  extends StringRequest {
    private static final String ruta = "http://virtual392.ies-sabadell.cat/virtual392/Moviles/agregarPartida.php"; // "http://10.1.131.139/niko/put_usuario.php";
    private Map<String, String> parametros;  // parametros que contentdra los valores del registro
    public addGame (String win, String loser, Integer winner,Integer losser, Response.Listener<String> listener){
        super(Request.Method.POST, ruta, listener, null); // LA peticion es post
        parametros = new HashMap<>();
        parametros.put("win",win);
        String puntosWin = String.valueOf(winner);
        String puntosLosser = String.valueOf(losser);
        parametros.put("winpoints",puntosWin);
        parametros.put("loser",loser);
        System.out.println("PUNTUACION DEL PERDEDOR "+losser);

        parametros.put("loserpoints",puntosLosser);
    }

    // Sobreescribimos map  para asegurarnos que nos devuelve la información de uestros parametros
    @Override
    protected Map<String, String> getParams() {
        return parametros;
    }
}
