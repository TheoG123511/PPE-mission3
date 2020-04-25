package com.example.suiviedefrais.Controleur;
import com.example.suiviedefrais.Model.AccesDistant;
import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Date;

public final class Control {

    private static Control instance = null;
    // private static Profil profil;
    private static String nomFic = "saveprofil";
   // private static AccesLocal accesLocal;
    private static AccesDistant accesDistant;
    private static Context context;
    // private ArrayList<Profil> lesProfils = new ArrayList<Profil>();

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
            //accesLocal = new AccesLocal(context);
            accesDistant = new AccesDistant();
           // accesDistant.envoi("tous", new JSONArray());
            // profil = accesLocal.recupDernier();
           // recupSerialize(context);

        }
        return Control.instance;
    }
}
