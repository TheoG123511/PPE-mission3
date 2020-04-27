package com.example.suiviedefrais.Model;
import android.util.Log;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Outils.AccesHTTP;
import com.example.suiviedefrais.Outils.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;


public class AccesDistant implements AsyncResponse {
    // constante
    private static final String SERVEUR = "http://192.168.56.1/GsbApi/Api.php";
    // propriété
    private Control control;

    /***
     * Constructeur de la classe
     */
    public AccesDistant(){
        super();
        control = Control.getInstance(null);
    }
    /**
     * Fonction qui doit traité le retour du serveur distant
     * @param output: le retour du serveur au format String
     * @param url: l'url qui a permit d'avoir le output
     * @param method la method qui a ete utiliser
     */
    @Override
    public void processFinish(String output, String url, String method) {
        Log.d("serveur", "****************" + output);
        Log.d("serveur", "method ->" + method);
        try {
            JSONObject info = new JSONObject(output);
            Log.d("serveur retour JSONObject", info.toString());
            String function = info.getString("Function");
            if (!info.has("Error")){
                if (function.equals("connexion")) {
                    control.setAuth(true);
                    // on lance Main Activity
                    control.runDashBoard(true, "");
                } else if (function.equals("insertNewFrais")) {
                    control.setAuth(true);
                    // on lance Main Activity
                    control.runDashBoard(true, "");
                }
            } else {
                if (function.equals("connexion")) {
                    control.setAuth(false);
                    // on remet username et password a zero
                    control.setUsername("");
                    control.setPassword("");
                    // on affiche un message d'erreur
                    control.runDashBoard(false, String.format("%s", info.getString("Error")));
                }
            }
            Log.d("AccesDistant -> Output", info.toString());
        } catch (Exception e){
            Log.d("AccesDistant", "Erreur dans la fonction processFinish() -> message : " + e.getMessage());
        }
    }

    /***
     * Permet de se connecter a l'api
     * @param username l'username a utilisé
     * @param password le mot de passe a utilisé
     */
    public void connection(String username, String password){
        AccesHTTP accesDonnes = new AccesHTTP(SERVEUR, "POST");
        // lien de delegation
        accesDonnes.delegate = this;
        // ajout parametre
        accesDonnes.addParam("username", username, true);
        accesDonnes.addParam("password", password, true);
        accesDonnes.execute();
    }

    /***
     * Permet d'envoyer les donnees au serveur
     * @param method la methode a utilisé dans la requete
     * @param data les donnees a envoyer au server concernant les frais
     */
    public void sendData(String method, JSONObject data){
        AccesHTTP accesDonnes = new AccesHTTP(SERVEUR, method);
        // lien de delegation
        accesDonnes.delegate = this;
        control.setUsername("lvillachane");
        control.setPassword("jux7g");
        // on insert les donnees de connection
        accesDonnes.addParam("username", control.getUsername(), true);
        accesDonnes.addParam("password", control.getPassword(), true);
        // on insert les donnees concernant les frais
        accesDonnes.addParam("AddFrais", data.toString(), false);
        // on envoye la requete
        accesDonnes.execute();
    }

}