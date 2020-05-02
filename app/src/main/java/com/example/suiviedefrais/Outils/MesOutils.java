package com.example.suiviedefrais.Outils;


public abstract class MesOutils {
    /**
     * Permet de gerer la clef qui permet d'acceder au donnees sauvegarder
     * @param year Integer : L'annee voulu (ex : 2020)
     * @param month Integer : Le mois voulu (ex : 1)
     * @return Ineteger : la clef generer
     */
    public static Integer generateKey(Integer year, Integer month){
        // on ajoute 1 au moi car les mois vont de 0 a 11
        month += 1;
        if (String.valueOf(month).length() == 1){
            // la le mois est de 0 a 9
            String key = year.toString() + "0" + month.toString();
            return Integer.parseInt(key);
        }else {
            String key = year.toString() + month.toString();
            return Integer.parseInt(key);
        }
    }

}