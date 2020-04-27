package com.example.suiviedefrais.Outils;
import java.security.Key;
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

    public static Integer generateKey(Integer year, Integer month){
        if (String.valueOf(month).length() == 1){
            // la le moi est de 1 a 9
            String key = year.toString() + "0" + month.toString();
            return Integer.parseInt(key);
        }else {
            String key = year.toString() + month.toString();
            return Integer.parseInt(key);
        }
    }
}