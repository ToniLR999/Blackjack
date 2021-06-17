package com.example.blackjack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class baraja {
    player Jugador;
    player Dealer;
    ArrayList<carta> arrayListCartas = new ArrayList<>();
    ConstraintLayout constraintLayout;
    MainActivity activity;
    claseAuxiliar ca;

    baraja(ConstraintLayout cl,  MainActivity mainActivity,player j1, player dealer) {
        ca = new claseAuxiliar();
        constraintLayout = cl;
        Jugador = j1;
        Dealer = dealer;
        activity = mainActivity;
        arrayListCartas.add(new carta(11,"trebolas"));
        arrayListCartas.add(new carta(11,"romboas"));
        arrayListCartas.add(new carta(11,"corazonesas"));
        arrayListCartas.add(new carta(11,"picasas"));

        for(int i = 0; i < 9; i++){
            int valorcarta = i+2;
            arrayListCartas.add(new carta(valorcarta,"trebol"+valorcarta));
        }
        for(int i = 0; i < 9; i++){
            int valorcarta = i+2;
            arrayListCartas.add(new carta(valorcarta,"rombo"+valorcarta));
        }
        for(int i = 0; i < 9; i++){
            int valorcarta = i+2;
            arrayListCartas.add(new carta(valorcarta,"corazones"+valorcarta));
        }
        for(int i = 0; i < 9; i++){
            int valorcarta = i+2;
            arrayListCartas.add(new carta(valorcarta,"picas"+valorcarta));
        }
        int valorcarta = 10;
        arrayListCartas.add(new carta(valorcarta,"trebolj"));
        arrayListCartas.add(new carta(valorcarta,"trebolk"));
        arrayListCartas.add(new carta(valorcarta,"trebolq"));
        arrayListCartas.add(new carta(valorcarta,"romboj"));
        arrayListCartas.add(new carta(valorcarta,"rombok"));
        arrayListCartas.add(new carta(valorcarta,"romboq"));
        arrayListCartas.add(new carta(valorcarta,"corazonesj"));
        arrayListCartas.add(new carta(valorcarta,"corazonesk"));
        arrayListCartas.add(new carta(valorcarta,"corazonesq"));
        arrayListCartas.add(new carta(valorcarta,"picasj"));
        arrayListCartas.add(new carta(valorcarta,"picask"));
        arrayListCartas.add(new carta(valorcarta,"picasq"));

    }

    public void repartirCartas(player dealer,player j1)
    {
        robaCarta(dealer);
        for(int i = 0; i < 2; i++)
        {
             robaCarta(j1);
        }
    }

    public int robaCarta(player alQuePaso)
    {
        int m_random = new Random().nextInt(this.arrayListCartas.size()-1);
        carta cartaTocada = this.arrayListCartas.get(m_random);
        System.out.println(m_random);
        alQuePaso.arrayListCartas.add(cartaTocada);
        this.arrayListCartas.remove(m_random);
        final int resID = activity.getResources().getIdentifier(cartaTocada.carta_value,"drawable",activity.getPackageName());
        final ImageView imageViewPlayer = new ImageView(activity);
        imageViewPlayer.setImageResource(R.drawable.cartaperfecta);
        imageViewPlayer.setLayoutParams(alQuePaso.cartaInicial.getLayoutParams());
        imageViewPlayer.getLayoutParams().width = alQuePaso.cartaInicial.getWidth();
        imageViewPlayer.getLayoutParams().height = alQuePaso.cartaInicial.getHeight();
        imageViewPlayer.setY(imageViewPlayer.getY());
        imageViewPlayer.setX(imageViewPlayer.getX()+alQuePaso.pos);
        alQuePaso.pos+=100;
        alQuePaso.cartas.add(imageViewPlayer);
        if(alQuePaso.cartas.size()%2==0)
        {
            for(int i = 0; i <   alQuePaso.cartas.size(); i++)
            {
                alQuePaso.cartas.get(i).setX(  alQuePaso.cartas.get(i).getX()-100);
            }
        }
        alQuePaso.puntuacion = ca.CalcularPuntosTotales(alQuePaso.arrayListCartas);


        constraintLayout.addView(imageViewPlayer);

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageViewPlayer, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageViewPlayer, "scaleX", 0f, 1f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setDuration(1000);
        oa2.setDuration(1000);
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageViewPlayer.setImageResource(resID);
                oa2.start();
            }
        });
        oa1.start();

        return resID;
    }



    public void mePlanto(final player j1, final player dealer, final baraja bj) {
        int puntuacionDelJugador = j1.puntuacion;
        int puntuacionDealer = dealer.puntuacion;
        if (puntuacionDealer < 17) {
            System.out.println("NO ME DUERMO");
            bj.robaCarta(dealer);
           if (dealer.puntuacion < 17) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        bj.mePlanto(j1, dealer, bj);
                    }
                }, 3000);
            }else{

           }
        }else{

        }
    }
}
