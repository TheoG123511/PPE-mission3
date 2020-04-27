package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.R;
import static com.example.suiviedefrais.Outils.MesOutils.generateKey;


public class HfActivity extends AppCompatActivity {

    private EditText txtHf;
    private EditText txtHfMotif;
    private DatePicker datHf;
    private Control control;
    boolean checkData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.datHf);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("DateChanged", "la date a ete changer par l'utilisateur, on mes a jour");
                // updateFrais();
            }
        });
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        datHf = findViewById(R.id.datHf);
        txtHfMotif = findViewById(R.id.txtHfMotif);
        txtHf = findViewById(R.id.txtHf);
        control = Control.getInstance(this);
        imgRetourMenuPrincipal();
        validerFraisHfBtn();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void imgRetourMenuPrincipal(){
        (findViewById(R.id.imgHfReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                if (checkData) {
                    displayMessage("Frais Hors Forfait ajouter avec Succes !");
                }
                Intent intent = new Intent(HfActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void backToMainMenu() {
        displayMessage("Frais Hors Forfait ajouter avec succes !");
        Intent intent = new Intent(HfActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void validerFraisHfBtn(){
        (findViewById(R.id.cmdHfAjouter)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // on recupere les valeurs
                Integer year = datHf.getYear();
                Integer month = datHf.getMonth();
                Integer day = datHf.getDayOfMonth();

                String motif = "";
                float montant = 0;
                try {
                    motif = txtHfMotif.getText().toString();
                    montant = Float.parseFloat(txtHf.getText().toString());
                    checkData = checkDataInToEdit(motif, montant);
                } catch (NumberFormatException e) {
                    displayMessage("Une Erreur c'est produite, Merci de remplir tous les champs !");
                }
                Log.d("HfActivity", "Le jours est = " + day.toString());
                // on genere la clee
                Integer key = generateKey(year, month);
                // on recupere les donnees depuis le controleur
                if (!control.checkIfKeyExist(key) && checkData){
                    // on creer l'object
                    FraisMois frais = new FraisMois(year, (month + 1));
                    // on insert le frais hf
                    frais.addFraisHf(montant, motif, day);
                    control.insertDataIntoFraisF(key, frais);
                } else {
                    // on recupere les donnees
                    FraisMois frais = control.getData(key);
                    if (!(frais == null) && checkData) {
                        frais.addFraisHf(montant, motif, day);
                        control.insertDataIntoFraisF(key, frais);
                    }
                }
                if (checkData) {
                    backToMainMenu();
                }
            }
        });
    }

    private boolean checkDataInToEdit(String motif, float montant) {
        // recuperation des donnees
        if (montant <= 0) {
           displayMessage("Une Erreur c'est produite, le montant doit etre supérieur a zero !");
           return false;
        }
        if (motif.length() <= 3) {
            displayMessage("Une Erreur c'est produite, le motif doit etre remplie d'au moins 4 caractère !");
            return false;
        }
        return true;
    }

    private void displayMessage(String message) {
        Toast.makeText(HfActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}
