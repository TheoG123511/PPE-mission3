package com.example.suiviedefrais.Controleur;
import com.example.suiviedefrais.Model.AccesDistant;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.Model.Global;
import com.example.suiviedefrais.Model.Serializer;
import com.example.suiviedefrais.Vue.LoginActivity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.suiviedefrais.Outils.MesOutils.generateKey;


public final class Control {

    // proprieter de la classe
    private static Map<Integer, FraisMois> listFraisMois = new HashMap<>();
    private static String nomFic = "save.fic";
    private static Control instance = null;
    private static AccesDistant accesDistant;
    private static Context context;
    private Context mainActivity = null;
    private Boolean isAuth = null;
    // on stocke les valeur qui permette de se connecter a l'api
    private String username = null;
    private String password = null;

    private Control() {
        super();
    }

    /**
     *
     * @return une instance de la classe control (singleton)
     */
    public static final Control getInstance(Context context){
        if (context != null){
            Control.context = context;
        }
        if (instance == null){
            Control.instance = new Control();
            accesDistant = new AccesDistant();
            // on recupere les donnees serialiser si elle existe
           recupSerialize(context);
        }
        return Control.instance;
    }

    /***
     * Getter
     * @return la valeur de mainActivity
     */
    private Context getMainActivity() {
        return mainActivity;
    }

    /***
     * Setter
     * @param mainActivity la nouvelle valeur a initialiser
     */
    public void setMainActivity(Context mainActivity) {
        this.mainActivity = mainActivity;
    }


    /***
     * Permet de mettre a jour l'activity MainActivity apres le retour du serveur
     * @param state l'etat du retour (true succes, false erreur)
     * @param message le message a affiché ( a laisse vide en cas de succes)
     */
    public void runDashBoard(Boolean state, String message){
        if (state){
            setAuth(true);
            // on deserialise les donnees
            //recupSerialize(context);
            ((LoginActivity) getMainActivity()).isConnected();
        }else {
            setAuth(false);
            ((LoginActivity) getMainActivity()).isConnected();
            ((LoginActivity) getMainActivity()).displayToast(message);
            ((LoginActivity) getMainActivity()).enabledBtnLogin();
        }
    }

    /***
     * Getter
     * @return la valeur de isAuth
     */
    public Boolean getAuth() {
        return isAuth;
    }

    /***
     * Setter
     * @param auth la nouvelle valeur a initialiser
     */
    public void setAuth(Boolean auth) {
        isAuth = auth;
    }

    /***
     * Permet de se connecter a l'api
     * @param username l'username a utilisé
     * @param password le mot de passe a utilisé
     */
    public void connection(String username, String password){
        setUsername(username);
        setPassword(password);
        accesDistant.connection(username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static void recupSerialize(Context context){

        listFraisMois = (HashMap) Serializer.deSerialize(context);
        //listFraisMois = new HashMap<>();
        //Serializer.serialize(listFraisMois, context);
        Log.d("Controleur", "recupSerialize listefraisMois recuperer");
            // teste
        Calendar c = Calendar.getInstance();
        Integer key = generateKey(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        try {
            Log.d("Controleur", "recupSerialize listefraisMois recuperer String Object -> " + listFraisMois.toString());
        } catch (NullPointerException e){
            // le fichier n'existe pas ou les donnees contenu dedans pose un probleme on remes tous a zero
            Log.d("Controleur", "Une erreur c est produite sur la deserialisation");
            listFraisMois = new HashMap<>();

            //listFraisMois.put(key, new FraisMois(c.get(Calendar.YEAR), c.get(Calendar.MONTH)));
        }

    }


    public boolean checkIfKeyExist(Integer key) {
        try {
            return listFraisMois.containsKey(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void insertDataIntoFraisF(Integer key, FraisMois fraisF) {
        listFraisMois.put(key, fraisF);
        // on sauvegarde les donnees en local
        Serializer.serialize(listFraisMois, context);
    }

    public Map<Integer, FraisMois> getListFraisMois() {
        return listFraisMois;
    }

    public FraisMois getData(Integer key) {
        if (listFraisMois.containsKey(key)) {
            return listFraisMois.get(key);
        }
        Log.d("Controleur", "Erreur dans la fonction getData() -> la clef n'existe pas ! Message : ");
        return null;
    }

    public void sendDataToServer() {
        Map<Integer, FraisMois> map = this.getListFraisMois();
        for (Map.Entry<Integer, FraisMois> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Log.d("Controleur", "Fonction sendDataToServer() -> Key = " + key.toString());
            JSONObject frais = entry.getValue().getJsonDataForSend();
            // si aucune erreur ne c'est produite
            if (frais != null) {
                accesDistant.sendData("POST", frais);
            }


        }

    }
}
