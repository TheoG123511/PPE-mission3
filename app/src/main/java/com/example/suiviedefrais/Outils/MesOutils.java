package com.example.suiviedefrais.Outils;


public abstract class MesOutils {

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