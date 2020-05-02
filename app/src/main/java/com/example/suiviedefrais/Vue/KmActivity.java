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
import com.example.suiviedefrais.Outils.Global;
import com.example.suiviedefrais.R;
import static com.example.suiviedefrais.Outils.MesOutils.generateKey;


public class KmActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtKm;
    private DatePicker datKm;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.datKm);
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
    private void init() {
        datKm = findViewById(R.id.datKm);
        txtKm = findViewById(R.id.txtKm);
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datKm), false);
        imgRetourMenuPrincipal();
        addOneBtnKm();
        lessOneBtnKm();
        validerFraisBtnKm();
        control = Control.getInstance(this);
        updateFrais();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void imgRetourMenuPrincipal(){
        (findViewById(R.id.imgKmReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(KmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOneBtnKm(){
        (findViewById(R.id.cmdKmPlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity += 1;
                txtKm.setText(String.format("%d", quantity));
            }
        });
    }

    private void lessOneBtnKm(){
        (findViewById(R.id.cmdKmMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtKm.setText(String.format("%d", quantity));
            }
        });
    }

    private void validerFraisBtnKm(){
        (findViewById(R.id.cmdKmValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // on recupere les valeurs
                Integer year = datKm.getYear();
                Integer month = datKm.getMonth();
                // on genere la clee
                Integer key = generateKey(year, month);
                Integer km = Integer.parseInt(txtKm.getText().toString());
                // on recupere les donnees depuis le controleur
                if (!control.checkIfKeyExist(key) && km != 0){
                    // on creer l'object
                    FraisMois fraisKm = new FraisMois(year, (month + 1));
                    fraisKm.setKm(km);
                    Log.d("ValiderFraisKm", "km = " + km.toString());
                    Log.d("ValiderFraisKm", "km object = " + fraisKm.getKm().toString());
                    control.insertDataIntoFraisF(key, fraisKm);
                } else {
                    // un frais existe pour se mois donc on mes a jours
                    // on recupere les donnees
                    FraisMois fraisKm = control.getData(key);
                    if (!(fraisKm == null)) {
                        fraisKm.setKm(km);
                        control.insertDataIntoFraisF(key, fraisKm);
                    }
                }
                backToMainMenu();
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateFrais() {
        // on recupere les valeurs
        Integer year = datKm.getYear();
        Integer month = datKm.getMonth();
        // on genere la clee
        Integer key = generateKey(year, month);
        // on recupere les donnees depuis le controleur
        if (control.checkIfKeyExist(key)){
            FraisMois fraisKm = control.getData(key);
            Log.d("update", "data = " + fraisKm.getKm());
            quantity = fraisKm.getKm();
            txtKm.setText(String.format("%d", fraisKm.getKm()));
        } else {
            quantity = 0;
            txtKm.setText(String.format("%d", quantity));
        }
    }

    private void backToMainMenu() {
        if (quantity != 0) {
            Toast.makeText(KmActivity.this, "Frais Ajouter avec Succes !", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(KmActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
