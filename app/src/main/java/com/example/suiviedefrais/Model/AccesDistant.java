package com.example.suiviedefrais.Model;
import android.util.Log;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Outils.AccesHTTP;
import com.example.suiviedefrais.Outils.AsyncResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class AccesDistant implements AsyncResponse {
    // constante
    private static final String SERVEURTOKEN = "https://gestion.brocanticke.com/api/auth/token/login";
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
        Log.d("serveur", "->" + url);
        Log.d("serveur", "method ->" + method);
        if (url.contains("/api/auth/token/login")){
            try {
                JSONObject info = new JSONObject(output);

            } catch (NullPointerException e){
                Log.d("AccesDistant", "Erreur dans la fonction processFinish() -> message : " + e.getMessage());

            } catch (Exception e) {

            }
        }
        else if (url.contains("/api/update/order/")){
            try {
                JSONObject info = new JSONObject(output);

            } catch (JSONException e) {
                Log.d("Erreur", "Conversion jSON impossible : "+ e.toString());
            }
        }
    }

    /***
     * Permet de se connecter a l'api
     * @param username l'username a utilisé
     * @param password le mot de passe a utilisé
     */
    public void connection(String username, String password){
        AccesHTTP accesDonnes = new AccesHTTP(SERVEURTOKEN, "POST");
        // lien de delegation
        accesDonnes.delegate = this;
        // ajout parametre
        accesDonnes.addParam("username", username);
        accesDonnes.addParam("password", password);
        accesDonnes.execute();
    }

    /***
     * Permet d'envoyer une requete
     * @param method la methode a utilisé dans la requete
     * @param url l'url a utilisé
     */
    public void sendRequest(String method, String url){
        AccesHTTP accesDonnes = new AccesHTTP(url, method);
        // lien de delegation
        accesDonnes.delegate = this;
        // on recupere le token
        accesDonnes.execute();
    }

    /***
     * Permet de verifié si toutes les valeur d'un Boolean[] sont vrai
     * @param toCheckArray l'array a vérifié
     * @return True si toutes les valeurs sont vrai sinon false
     */
    private boolean checkOutputServer(boolean[] toCheckArray){
        for (int i = 0; i< toCheckArray.length; i++) {
            Log.d("checkOutputServer",  String.format("%b", toCheckArray[i]));
            if (!toCheckArray[i]){
                return false;
            }
        }
        return true;
    }

}