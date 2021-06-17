package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private ListView listview;

    Button botoncarta;
    Button botonplay;

    Button botondoblar;
    Button botonplantar;
    ImageView imagenCartaDealer;
    ImageView imagenCartaPlayer;
    TextView dinero;
    EditText apuesta;
    baraja Baraja;
    private boolean firstTime = true;
    public  static  boolean heAcabado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MediaPlayer vueltaCarta= MediaPlayer.create(MainActivity.this,R.raw.vuelacarta);
        //ImageView imageview = new ImageView(MainActivity.this);

        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.Juego);
        botoncarta = findViewById(R.id.button);
        botonplantar = (Button)findViewById(R.id.button2);
        botonplay = (Button)findViewById(R.id.button7);
        botondoblar = (Button)findViewById(R.id.button3);
        imagenCartaDealer = (ImageView)findViewById(R.id.cartaDealer);
        imagenCartaPlayer = (ImageView)findViewById(R.id.cartaMano);
        apuesta = (EditText)findViewById(R.id.apuesta);
        final User usario = (User) getIntent().getSerializableExtra("user");
        dinero = (TextView)findViewById(R.id.textView4);
        dinero.setText(usario.getMoney());
        OcultarBotones();
        final player j1 = new player(imagenCartaPlayer);
       final player dealer = new player(imagenCartaDealer);
       Baraja = new baraja(constraintLayout,MainActivity.this,j1,dealer);
        final Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRepuesta = new JSONObject(response);
                    boolean ok =jsonRepuesta.getBoolean("success");
                    if (ok) {
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(getApplicationContext());
                        alerta.setMessage("Fallo en el editar")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        botonplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dineros = Integer.parseInt(usario.getMoney());

                if (dineros > 0) {
                MostrarBotones();



                    if (firstTime) {
                        imagenCartaDealer.setVisibility(View.INVISIBLE);
                        imagenCartaPlayer.setVisibility(View.INVISIBLE);
                        Baraja.repartirCartas(dealer, j1);
                        firstTime = false;
                    }
                    updateUser(usario, respuesta, -Integer.parseInt(apuesta.getText().toString()));

                }
            }

        });
        botoncarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pedir carta
                    Baraja.robaCarta(j1);
                    vueltaCarta.start();

                    if(j1.puntuacion>21){
                    Baraja.mePlanto(j1,dealer,Baraja);
                    String a = "";
                        a = ("WIN DEALER");
                        newGameBdd("Dealer",dealer.puntuacion,usario.getName(),j1.puntuacion);
                        Toast.makeText(getBaseContext(),(String)a, Toast.LENGTH_SHORT).show();
                        botoncarta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });   AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                        alerta.setMessage("¿Quieres jugar de nuevo?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //aqui se empieza el juego de nuevo
                                reiniciar();
                            }
                        })


                                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //ir al menu activity
                                        irseAlLogin();
                                    }
                                })
                        ;
                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Has perdido");
                        titulo.show();

                    }
            //updateUser(usario,respuesta, -10);
                dinero.setText(usario.getMoney());
            }
        });

        botondoblar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doblar
            }
        });
        botonplantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    j1.puntuacion = new claseAuxiliar().CalcularPuntosTotales(j1.arrayListCartas);

                    if(dealer.puntuacion<17){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                Baraja.mePlanto(j1,dealer,Baraja);
                            }
                        }, 3000);
                }
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String a = "";

                        System.out.println("HA ACABADO");
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);

                        if(j1.puntuacion==dealer.puntuacion){
                            alerta.setMessage("¿Quieres jugar de nuevo?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reiniciar();
                                }
                            })
                                    .setNeutralButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //ir al menu activity
                                            irseAlLogin();
                                        }
                                    })
                            ;
                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Has empatado");
                            titulo.show();
                            a="Empate";
                        }else if((j1.puntuacion>dealer.puntuacion ||dealer.puntuacion>21) && j1.puntuacion<=21){
                            a = "Jugador";
                            newGameBdd(usario.getName(),j1.puntuacion,"Dealer",dealer.puntuacion);
                            alerta.setMessage("¿Quieres jugar de nuevo?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //aqui se empieza el juego de nuevo
                                    reiniciar();
                                }
                            }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //ir al menu activity
                                    irseAlLogin();
                                }
                            })
                            ;
                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Has ganado");
                            updateUser(usario,respuesta,Integer.parseInt(apuesta.getText().toString())*2);
                            titulo.show();
                        }else if((j1.puntuacion<dealer.puntuacion ||j1.puntuacion>21) && dealer.puntuacion<=21){
                            a ="Dealer";
                            System.out.println("PUNTUACION DEL DEEALER"+dealer.puntuacion);
                            newGameBdd("Dealer",dealer.puntuacion,usario.getName(),j1.puntuacion);
                            alerta.setMessage("¿Quieres jugar de nuevo?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //aqui se empieza el juego de nuevo
                                    reiniciar();
                                }
                            }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //ir al menu activity
                                    irseAlLogin();
                                }
                            })
                            ;
                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("Has perdido");
                            titulo.show();
                        }
                        botoncarta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        Toast.makeText(getBaseContext(),(String)a, Toast.LENGTH_SHORT).show();

                    }
                }, 10000);
               // recreate();
            }
        });

    }

    private void OcultarBotones() {
        botoncarta.setVisibility(View.INVISIBLE);
        botondoblar.setVisibility(View.INVISIBLE);
        botonplantar.setVisibility(View.INVISIBLE);
        dinero.setVisibility(View.INVISIBLE);
        botonplay.setVisibility(View.VISIBLE);
        apuesta.setVisibility(View.VISIBLE);
    }
    private void MostrarBotones(){
        botoncarta.setVisibility(View.VISIBLE);
        botondoblar.setVisibility(View.VISIBLE);
        botonplantar.setVisibility(View.VISIBLE);
        dinero.setVisibility(View.VISIBLE);
        botonplay.setVisibility(View.INVISIBLE);
        apuesta.setVisibility(View.INVISIBLE);
    }


    public static void seHaAcabado(){

    }
    private void updateUser(User usario, Response.Listener<String> respuesta, int i)
    {

        int dinero = Integer.valueOf(usario.getMoney());
        usario.setMoney(String.valueOf(dinero+=i));
       String ruta = "http://virtual392.ies-sabadell.cat/virtual392/Moviles/editarUser.php"; // "http://10.1.131.139/niko/put_usuario.php";
        addUser editarUser = new addUser(usario.getName(),usario.getPwd(),usario.getPwd(), respuesta, ruta,usario.getMoney());
        RequestQueue cola = Volley.newRequestQueue(MainActivity.this);
        cola.add(editarUser);
    }

    private void reiniciar(){
        this.recreate();
       //Intent switchActivityIntent = new Intent(this, MainActivity.class);
        //startActivity(switchActivityIntent);
    }

    private void newGameBdd(String a, int puntuacion, String name, int i){
        Response.Listener<String> respuestaPartida = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRepuesta = new JSONObject(response);
                    boolean ok =jsonRepuesta.getBoolean("success");
                    if (ok) {
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(getApplicationContext());
                        alerta.setMessage("Fallo en el editar")
                                .setNegativeButton("Reintentar", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };


        addGame addGame = new addGame(a,name,puntuacion,i,respuestaPartida);
        RequestQueue cola = Volley.newRequestQueue(MainActivity.this);
        cola.add(addGame);
    }
    private void irseAlLogin(){
        Intent switchActivityIntent = new Intent(this, ActividadLogin.class);
        startActivity(switchActivityIntent);
    }

}
