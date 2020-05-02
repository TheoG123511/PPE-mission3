package com.example.suiviedefrais.Controleur;
import com.example.suiviedefrais.Model.AccesDistant;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.Outils.Serializer;
import com.example.suiviedefrais.Vue.LoginActivity;
import android.content.Context;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public final class Control {

    // proprieter de la classe
    private static Map<Integer, FraisMois> listFraisMois = new HashMap<>();
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
     */
    public void runDashBoard(Boolean state){
        setAuth(state);
        ((LoginActivity) getMainActivity()).isConnected();
    }

    /**
     * Permet d'afficher un message (de type toast) dans loginActivity et de reactiver le btn d'envoie des donnees
     * @param message String : le message a afficher
     * @param stateBtn Boolean : True pour activer le btn
     */
    public void displayMessageInLogin(String message, Boolean stateBtn){
        ((LoginActivity) getMainActivity()).displayToast(message);
        if (stateBtn) {
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

    /**
     * Getter
     * @return la valeur de username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter
     * @param username la nouvelle valeur a initialiser
     */
    private void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter
     * @return la valeur de password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter
     * @param password la nouvelle valeur a initialiser
     */
    private void setPassword(String password) {
        this.password = password;
    }

    /**
     * Permet de recupere les donnes serialiser
     * @param context le context a utiliser pour la deserialisation
     */
    private static void recupSerialize(Context context){
        listFraisMois = (HashMap) Serializer.deSerialize(context);
        try {
            Log.d("Controleur", "recupSerialize listefraisMois recuperer String Object -> " + listFraisMois.toString());
        } catch (NullPointerException e){
            // le fichier n'existe pas ou les donnees contenu dedans pose un probleme on remes tous a zero
            Log.d("Controleur", "Une erreur c est produite sur la deserialisation");
            listFraisMois = new HashMap<>();
        }

    }

    /**
     * Verifie si une clee existe ou non dans listFraisMois
     * @param key Integer : la clef a verifier
     * @return Boolean : True en cas de succes, False en cas d'echec
     */
    public boolean checkIfKeyExist(Integer key) {
        try {
            return listFraisMois.containsKey(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Permet d'inserer un nouveaux frais forfaitaire dans la liste des frais mais
     * @param key Integer : la clef a utiliser
     * @param fraisF FraisMois : Un object de type FraisMois
     */
    public void insertDataIntoFraisF(Integer key, FraisMois fraisF) {
        listFraisMois.put(key, fraisF);
        // on sauvegarde les donnees en local
        Serializer.serialize(listFraisMois, context);
    }

    /**
     * Getter
     * @return la valeur de listFraisMois
     */
    public Map<Integer, FraisMois> getListFraisMois() {
        return listFraisMois;
    }

    /**
     * Getter
     * @param key Integer : la clef a utiliser pour retrouver les donnes
     * @return la valeur de l'object dans listFraisMois
     */
    public FraisMois getData(Integer key) {
        if (listFraisMois.containsKey(key)) {
            return listFraisMois.get(key);
        }
        Log.d("Controleur", "Erreur dans la fonction getData() -> la clef n'existe pas ! Message : ");
        return null;
    }

    /**
     * Permet d'envoyer les donnees au serveur de control
     */
    public void sendDataToServer() {
        // on verifie si la liste n'est pas vide
        if (getLenListFraisMois() > 0) {
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
        } else {
            displayMessageInLogin("Une erreur c'est produite aucune donnees sauvegarder. Syncronisation avec le serveur impossible !", true);
        }

    }

    /**
     * Permet de reinitialiser les donnes contenu dans listFraisMois (remise a zero) et de serialiser listFraisMois
     * pour sauvegarder
     */
    public void reinitializeData() {
        // on reinitialise l'object
        listFraisMois = new HashMap<>();
        // on le sauvegarde
        Serializer.serialize(listFraisMois, context);
    }

    /**
     * Getter
     * @return Ineteger : la taille de listFraisMois
     */
    public Integer getLenListFraisMois(){
        return listFraisMois.size();
    }
}
