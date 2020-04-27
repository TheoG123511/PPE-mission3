package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.Model.Global;
import com.example.suiviedefrais.R;

import static com.example.suiviedefrais.Outils.MesOutils.generateKey;

public class activity_repas extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtRepas;
    private DatePicker dateRepas;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.dateRepas);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("DateChanged", "la date a ete changer par l'utilisateur, on mes a jour");
                updateFrais();
            }
        });
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init(){
        dateRepas = findViewById(R.id.dateRepas);
        txtRepas = findViewById(R.id.txtRepas);
        txtRepas.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.dateRepas), false);
        imgRetourMenuPrincipal();
        addOneBtnRepas();
        lessOneBtnRepas();
        control = Control.getInstance(this);
        updateFrais();
        validerFraisBtnRepas();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void imgRetourMenuPrincipal(){
        (findViewById(R.id.imgRepasReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(activity_repas.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOneBtnRepas(){
        (findViewById(R.id.cmdRepasPlus)).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("DefaultLocale")
            public void onClick(View v){
                quantity += 1;
                txtRepas.setText(String.format("%d", quantity));
            }
        });
    }

    private void lessOneBtnRepas(){
        (findViewById(R.id.cmdRepasMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtRepas.setText(String.format("%d", quantity));
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateFrais() {
        // on recupere les valeurs
        Integer year = dateRepas.getYear();
        Integer month = dateRepas.getMonth();
        // on genere la clee
        Integer key = generateKey(year, month);
        Log.d("RepasActivity", "update annee = " + key.toString());
        // on recupere les donnees depuis le controleur
        if (control.checkIfKeyExist(key)){
            FraisMois frais = control.getData(key);
            Log.d("update", "data = " + frais.getRepas().toString());
            quantity = frais.getRepas();
            txtRepas.setText(String.format("%d", frais.getRepas()));
        } else {
            quantity = 0;
            txtRepas.setText(String.format("%d", quantity));
        }
    }

    private void validerFraisBtnRepas(){
        (findViewById(R.id.cmdRepasValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // on recupere les valeurs
                Integer year = dateRepas.getYear();
                Integer month = dateRepas.getMonth();
                // on genere la clee
                Integer key = generateKey(year, month);
                Integer repas = Integer.parseInt(txtRepas.getText().toString());
                // on recupere les donnees depuis le controleur
                if (!control.checkIfKeyExist(key) && repas != 0){
                    // on creer l'object
                    FraisMois frais = new FraisMois(year, (month + 1));
                    frais.setEtape(repas);
                    Log.d("ValiderFraisRepas", "repas = " + repas.toString());
                    Log.d("ValiderFraisRepas", "repas object = " + frais.getRepas().toString());
                    control.insertDataIntoFraisF(key, frais);
                } else {
                    // un frais existe pour se mois donc on mes a jours
                    // on recupere les donnees
                    FraisMois frais = control.getData(key);
                    if (!(frais == null)) {
                        frais.setRepas(repas);
                        control.insertDataIntoFraisF(key, frais);
                    }
                }
                backToMainMenu();
            }
        });
    }

    private void backToMainMenu() {
        if (quantity != 0) {
            Toast.makeText(activity_repas.this, "Frais Ajouter avec Succes !", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(activity_repas.this, MainActivity.class);
        startActivity(intent);
    }
}
