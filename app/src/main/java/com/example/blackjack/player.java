package com.example.blackjack;

import android.widget.ImageView;

import java.util.ArrayList;

public class player {
    ArrayList<ImageView> cartas;
    ArrayList<carta> arrayListCartas;
    int puntuacion;
    ImageView cartaInicial;
    int pos = 0;
    public player(ImageView primeraCarta)
    {
        cartas = new ArrayList<ImageView>();
        arrayListCartas = new ArrayList<carta>();
        puntuacion = 0;
        cartaInicial = primeraCarta;
    }
}
