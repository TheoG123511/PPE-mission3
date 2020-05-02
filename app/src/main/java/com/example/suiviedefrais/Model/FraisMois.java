package com.example.suiviedefrais.Model;
import android.util.Log;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import static com.example.suiviedefrais.Outils.MesOutils.generateKey;

/**
 * Classe métier contenant les informations des frais d'un mois
 */
public class FraisMois implements Serializable {

    private Integer mois; // mois concerné
    private Integer annee; // année concernée
    private Integer etape; // nombre d'étapes du mois
    private Integer km; // nombre de km du mois
    private Integer nuitee; // nombre de nuitées du mois
    private Integer repas; // nombre de repas du mois
    private final ArrayList<FraisHf> lesFraisHf; // liste des frais hors forfait du mois

    public FraisMois(Integer annee, Integer mois) {
        this.annee = annee;
        this.mois = mois;
        this.etape = 0;
        this.km = 0;
        this.nuitee = 0;
        this.repas = 0;
        lesFraisHf = new ArrayList<>();
        /* Retrait du type de l'ArrayList (Optimisation Android Studio)
		 * Original : Typage explicit =
		 * lesFraisHf = new ArrayList<FraisHf>() ;
		*/
    }

    /**
     * Ajout d'un frais hors forfait
     *
     * @param montant Montant en euros du frais hors forfait
     * @param motif Justification du frais hors forfait
     */
    public void addFraisHf(Float montant, String motif, Integer jour) {
        lesFraisHf.add(new FraisHf(montant, motif, jour));
    }

    /**
     * Suppression d'un frais hors forfait
     *
     * @param index Indice du frais hors forfait à supprimer
     */
    public void supprFraisHf(Integer index) {
        lesFraisHf.remove(index);
    }

    public Integer getMois() {
        return mois;
    }

    public void setMois(Integer mois) {
        this.mois = mois;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getEtape() {
        return etape;
    }

    public void setEtape(Integer etape) {
        this.etape = etape;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getNuitee() {
        return nuitee;
    }

    public void setNuitee(Integer nuitee) {
        this.nuitee = nuitee;
    }

    public Integer getRepas() {
        return repas;
    }

    public void setRepas(Integer repas) {
        this.repas = repas;
    }

    public ArrayList<FraisHf> getLesFraisHf() {
        return lesFraisHf;
    }

    public JSONObject getJsonDataForSend() {
        JSONObject data = new JSONObject();
        try {
            // insertion des frais forfaitaire
            data.put("Date", generateKey(getAnnee(), (getMois() - 1)));
            data.put("Year", getAnnee());
            data.put("Month", getMois());
            data.put("KM", this.getKm());
            data.put("NUI", this.getNuitee());
            data.put("REP", this.getRepas());
            data.put("ETP", this.getEtape());
            // ajout des frais hors forfait
            for (int i = 0; i < this.getLesFraisHf().size(); i++) {
                JSONObject HfFrais = new JSONObject();
                HfFrais.put("Jour", this.getLesFraisHf().get(i).getJour().toString());
                HfFrais.put("Motif", this.getLesFraisHf().get(i).getMotif());
                HfFrais.put("Montant", this.getLesFraisHf().get(i).getMontant().toString());
                data.put("Hf-" + i, HfFrais);
            }
            Log.d("FraisMois", "Fonction getJsonDataForSend() return : " + data.toString());
            return data;
        } catch (Exception e) {
            Log.d("FraisMois", "Erreur dans la fonction -> getJsonDataForSend() : Message : " + e.getMessage());
            return null;
        }
    }

}
