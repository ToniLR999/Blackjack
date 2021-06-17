package com.example.blackjack;

import java.util.ArrayList;

public class claseAuxiliar {

    int CalcularPuntos(carta cartas) {
        int puntuacion = 0;
        if (cartas.valor_carta==11) {
            if (puntuacion + 11 <= 21) {
                puntuacion += 11;
            } else {
                puntuacion++;
            }
        } else {
            int numero = cartas.valor_carta;
            puntuacion += numero;
        }
        return puntuacion;
    }

    int CalcularPuntosAs(ArrayList<carta> mano, int puntuacion) {
        if (puntuacion + 11 <= 21) {
            puntuacion += 11;
            return puntuacion;
        } else {
            puntuacion++;
            return puntuacion;
        }
    }

    int ComprobarAs(carta cartas) {
        int puntuacion = 0;
        if (cartas.valor_carta==11) {
            return 99;
        }
        return 0;
    }
    int CalcularPuntosTotales(ArrayList<carta> mano) {
        int puntuacion = 0;
        for (int i=0;i<mano.size();i++){
            if(this.ComprobarAs(mano.get(i))==99){
                puntuacion = this.CalcularPuntosAs(mano,puntuacion);
            }else{
                puntuacion+=this.CalcularPuntos(mano.get(i));
            }
        }
        return puntuacion;
    }

}
