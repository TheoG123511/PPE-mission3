package com.example.suiviedefrais.Model;
import android.util.Log;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Outils.AccesHTTP;
import com.example.suiviedefrais.Outils.AsyncResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AccesDistant implements AsyncResponse {
    // constante
    private static final String SERVEUR = "http://192.168.56.1/GsbApi/Api.php";
    // propriété
    private Control control;
    private List<String> list = new ArrayList<String>();
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
            if (function.equals("connexion")) {
                if (info.has("Error")) {
                    control.displayMessageInLogin(String.format("%s", info.getString("Error")), true);
                }else {
                    // on envoie les donnees on commence la syncronisation avec le serveur distant
                    control.displayMessageInLogin("Démarrage de la syncronisation avec le serveur distant.", false);
                    control.sendDataToServer();
                }
                } else if (function.equals("insertNewFrais")) {

                    Log.d("AccesDistant", "Fonction insertNewFrais detected -> " + output.toString());
                    int keyFrais = info.getInt("KeyObject");
                    String year = info.getString("Year");
                    String month = info.getString("Month");
                    Log.d("AccesDistant", "keyObject = " + Integer.toString(keyFrais));
                    JSONObject KM = info.getJSONObject("KM");
                    if (KM.has("Error")) {
                        String error = KM.getString("Error");
                        list.add(String.format("[%s-%s] Frais Km -> %s", month,  year, error));
                    }
                    JSONObject REP = info.getJSONObject("REP");
                    if (REP.has("Error")) {
                        String error = REP.getString("Error");
                        list.add(String.format("[%s-%s] Frais de Repas -> %s", month,  year, error));
                    }
                    JSONObject ETP = info.getJSONObject("ETP");
                    if (ETP.has("Error")) {
                        String error = ETP.getString("Error");
                        list.add(String.format("[%s-%s] Frais d'etape -> %s", month,  year, error));
                    }
                    JSONObject NUI = info.getJSONObject("NUI");
                    if (NUI.has("Error")) {
                        String error = NUI.getString("Error");
                        list.add(String.format("[%s-%s] Frais de Nuitée -> %s", month,  year, error));
                    }
                    Log.d("AccesDistant", "KM = " + KM.toString() + "\nREP = " + REP.toString() + "\nETP = " + ETP.toString() + "\nNUI = " + NUI.toString());
                    Log.d("AccesDistant", "List data len = " + control.getLenListFraisMois().toString());
                    // on verifie si l'object a des frais hf
                    try {
                        JSONObject jsonObject = info.getJSONObject("fraisHorsForfait");
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            if (jsonObject.get(key) instanceof JSONObject) {
                                if (((JSONObject) jsonObject.get(key)).has("Error")) {
                                    String date = ((JSONObject) jsonObject.get(key)).getString("date");
                                    String error = ((JSONObject) jsonObject.get(key)).getString("Error");
                                    list.add(String.format("[%s] Frais Hors Forfait -> %s", date, error));
                                }

                                Log.d("AccesDistant While", "Data = " + jsonObject.get(key).toString());

                            }
                        }
                    } catch (JSONException e) {
                        Log.d("AccesDistant", "Cette object n'a pas de frais hors forfait");
                    }
                    // on supprime l'object
                    control.getListFraisMois().remove(keyFrais);
                    control.displayMessageInLogin(listToString(list), false);
                    // on a fini la syncronisation
                    if (control.getLenListFraisMois() == 0){
                        // on reinitialise les donnees serializer
                        Log.d("AccesDistant", "Fin des donnees a syncro");
                        control.reinitializeData();
                        control.setAuth(true);
                        // on lance Main Activity
                        control.displayMessageInLogin("Syncronisation effectuer avec Succes !", false);
                        control.runDashBoard(true);
                    }
                }
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
        setList(new ArrayList<String>());
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
        //control.setUsername("lvillachane");
        //control.setPassword("jux7g");
        // on insert les donnees de connection
        accesDonnes.addParam("username", control.getUsername(), true);
        accesDonnes.addParam("password", control.getPassword(), true);
        // on insert les donnees concernant les frais
        accesDonnes.addParam("AddFrais", data.toString(), false);
        // on envoye la requete
        accesDonnes.execute();
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    private String listToString(List<String> listError){
        String listString = "";
        for (String s : list)
        {
            listString += String.format("%s\n", s);
        }
        setList(new ArrayList<String>());
        return listString;
    }
}