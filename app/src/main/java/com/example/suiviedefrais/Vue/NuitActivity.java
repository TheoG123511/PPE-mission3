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

public class NuitActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtNuit;
    private DatePicker datNuit;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuit);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.datNuit);
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
        datNuit = findViewById(R.id.datNuit);
        txtNuit = findViewById(R.id.txtNuit);
        txtNuit.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datNuit), false);
        imgRetourMenuPrincipal();
        addOneBtnNuit();
        lessOneBtnNuit();
        control = Control.getInstance(this);
        updateFrais();
        validerFraisBtnNuit();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void imgRetourMenuPrincipal(){
        (findViewById(R.id.imgNuitReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(NuitActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOneBtnNuit(){
        (findViewById(R.id.cmdNuitPlus)).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("DefaultLocale")
            public void onClick(View v){
                quantity += 1;
                txtNuit.setText(String.format("%d", quantity));
            }
        });
    }

    private void lessOneBtnNuit(){
        (findViewById(R.id.cmdNuitMoins)).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("DefaultLocale")
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtNuit.setText(String.format("%d", quantity));
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateFrais() {
        // on recupere les valeurs
        Integer year = datNuit.getYear();
        Integer month = datNuit.getMonth();
        // on genere la clee
        Integer key = generateKey(year, month);
        Log.d("NuitActivity", "update annee = " + key.toString());
        // on recupere les donnees depuis le controleur
        if (control.checkIfKeyExist(key)){
            FraisMois frais = control.getData(key);
            Log.d("update", "data = " + frais.getNuitee().toString());
            quantity = frais.getNuitee();
            txtNuit.setText(String.format("%d", frais.getNuitee()));
        } else {
            quantity = 0;
            txtNuit.setText(String.format("%d", quantity));
        }
    }

    private void validerFraisBtnNuit(){
        (findViewById(R.id.cmdNuitValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // on recupere les valeurs
                Integer year = datNuit.getYear();
                Integer month = datNuit.getMonth();
                // on genere la clee
                Integer key = generateKey(year, month);
                Integer nuit = Integer.parseInt(txtNuit.getText().toString());
                // on recupere les donnees depuis le controleur
                if (!control.checkIfKeyExist(key) && nuit != 0){
                    // on creer l'object
                    FraisMois frais = new FraisMois(year, (month + 1));
                    frais.setNuitee(nuit);
                    Log.d("ValiderFraisNuit", "nuit = " + nuit.toString());
                    Log.d("ValiderFraisNuit", "nuit object = " + frais.getNuitee().toString());
                    control.insertDataIntoFraisF(key, frais);
                } else {
                    // un frais existe pour se mois donc on mes a jours
                    // on recupere les donnees
                    FraisMois frais = control.getData(key);
                    if (!(frais == null)) {
                        frais.setNuitee(nuit);
                        control.insertDataIntoFraisF(key, frais);
                    }
                }
                backToMainMenu();
            }
        });
    }

    private void backToMainMenu() {
        if (quantity != 0) {
            Toast.makeText(NuitActivity.this, "Frais Ajouter avec Succes !", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(NuitActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
