package com.example.suiviedefrais.Model;
import android.util.Log;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Outils.AccesHTTP;
import com.example.suiviedefrais.Outils.AsyncResponse;
import org.json.JSONObject;


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
            String function = info.getString("Function");
            if (!info.has("Error")){
                if (function.equals("connexion")) {
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