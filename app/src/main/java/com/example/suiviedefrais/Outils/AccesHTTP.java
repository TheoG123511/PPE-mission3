package com.example.suiviedefrais.Outils;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Classe technique de connexion distante HTTP qui permet d'envoyer des parametres (pas de fichier)
 */
public class AccesHTTP extends AsyncTask<String, Integer, Long> {

    // propriétés
    public String ret; // information retournée par le serveur
    public String url; // url correspondant au information retourner par la serveur
    private String method; // permet de stocker la methode a utiliser dans la requete
    public AsyncResponse delegate=null; // gestion du retour asynchrone
    private String parametres = ""; // paramètres à envoyer en POST au serveur

    /**
     * Constructeur de la classe
     * @param url l'url a utilisé
     * @param method la methode a utilisé (GET, POST, PUT, DELETE)
     */
    public AccesHTTP(String url, String method) {
        super();
        this.url = url;
        this.method = method;
    }

    /**
     * Construction de la chaîne de paramètres à envoyer au serveur
     * @param nom Le nom du parametre
     * @param valeur La valeur a y associé
     */
    public void addParam(String nom, String valeur) {
        try {
            if (parametres.equals("")) {
                // premier paramètre
                parametres = URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }else{
                // paramètres suivants (séparés par &)
                parametres += "&" + URLEncoder.encode(nom, "UTF-8") + "=" + URLEncoder.encode(valeur, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            Log.d("AccesHttp", "Erreur dans la fonction : addParam()");
            e.printStackTrace();
        }
    }

    /**
     * Méthode appelée par la méthode execute
     * permet d'envoyer au serveur une liste de paramètres en GET, POST, PUT, DELETE
     * @return null
     */
    @Override
    protected Long doInBackground(String... urls) {

        // pour éliminer certaines erreurs
        System.setProperty("http.keepAlive", "false");
        try {

            URL url = new URL(this.url);
            // ouverture de la connexion
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            // choix de la méthode
            connexion.setRequestMethod(this.method);
            connexion.setRequestProperty("Accept", "*/*");
            connexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connexion.setFixedLengthStreamingMode(parametres.getBytes().length);
            if (method.equals("GET") || method.equals("DELETE")){
                connexion.setDoOutput(false);
            } else {
                connexion.setDoOutput(true);
                // création de la requete d'envoi sur la connexion, avec les paramètres
                PrintWriter writer = new PrintWriter(connexion.getOutputStream());
                writer.print(parametres);
                writer.flush();
                writer.close();
            }
            // Récupération du retour du serveur
            BufferedReader reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            ret = reader.readLine();
            reader.close();
        } catch (Exception e) {
            Log.d("AccesHttp", "Erreur dans la fonction doInBackground() -> message : " + e.getMessage());
        }
        return null;
    }


    /**
     * Sur le retour du serveur, envoi l'information retournée à processFinish
     * @param result le retour du serveur
     */
    @Override
    protected void onPostExecute(Long result) {
        try
        {
            delegate.processFinish(this.ret, this.url, this.method);
        }catch (Exception e){
            Log.d("AccesHttp", "Erreur dans la fonction onPostExecute() -> Message : " + e.getMessage());
        }
    }

}
