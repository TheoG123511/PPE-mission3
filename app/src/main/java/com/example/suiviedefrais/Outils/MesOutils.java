package com.example.suiviedefrais.Outils;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class MesOutils {

    /**
     * Convertion d'une date en chaine sous la forme yyyy-MM-dd hh:mm:ss
     * @param uneDate un Object de type Date
     * @return Une date au format String
     */
    public static String convertDateToString(Date uneDate){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return date.format(uneDate);
    }
}