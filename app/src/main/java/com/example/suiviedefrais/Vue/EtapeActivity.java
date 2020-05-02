package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.Outils.Global;
import com.example.suiviedefrais.R;

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

import static com.example.suiviedefrais.Outils.MesOutils.generateKey;


public class EtapeActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtEtape;
    private DatePicker datEtape;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.datEtape);
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
        datEtape = findViewById(R.id.datEtape);
        txtEtape = findViewById(R.id.txtEtape);
        txtEtape.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datEtape), false);
        imgRetourMenuPrincipal();
        addOneBtnEtape();
        lessOneBtnEtape();
        control = Control.getInstance(this);
        updateFrais();
        validerFraisBtnEtape();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void imgRetourMenuPrincipal(){
        (findViewById(R.id.imgEtapeReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(EtapeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addOneBtnEtape(){
        (findViewById(R.id.cmdEtapePlus)).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("DefaultLocale")
            public void onClick(View v){
                quantity += 1;
                txtEtape.setText(String.format("%d", quantity));
            }
        });
    }

    private void lessOneBtnEtape(){
        (findViewById(R.id.cmdEtapeMoins)).setOnClickListener(new Button.OnClickListener() {
            @SuppressLint("DefaultLocale")
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtEtape.setText(String.format("%d", quantity));
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void updateFrais() {
        // on recupere les valeurs
        Integer year = datEtape.getYear();
        Integer month = datEtape.getMonth();
        // on genere la clee
        Integer key = generateKey(year, month);
        Log.d("EtapeActivity", "update annee = " + key.toString());
        // on recupere les donnees depuis le controleur
        if (control.checkIfKeyExist(key)){
            FraisMois frais = control.getData(key);
            Log.d("update", "data = " + frais.getEtape().toString());
            quantity = frais.getEtape();
            txtEtape.setText(String.format("%d", frais.getEtape()));
        } else {
            quantity = 0;
            txtEtape.setText(String.format("%d", quantity));
        }
    }

    private void validerFraisBtnEtape(){
        (findViewById(R.id.cmdEtapeValider)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // on recupere les valeurs
                Integer year = datEtape.getYear();
                Integer month = datEtape.getMonth();
                // on genere la clee
                Integer key = generateKey(year, month);
                Integer etape = Integer.parseInt(txtEtape.getText().toString());
                // on recupere les donnees depuis le controleur
                if (!control.checkIfKeyExist(key) && etape != 0){
                    // on creer l'object
                    FraisMois frais = new FraisMois(year, (month + 1));
                    frais.setEtape(etape);
                    Log.d("ValiderFraisEtape", "etape = " + etape.toString());
                    Log.d("ValiderFraisEtape", "etape object = " + frais.getEtape().toString());
                    control.insertDataIntoFraisF(key, frais);
                } else {
                    // un frais existe pour se mois donc on mes a jours
                    // on recupere les donnees
                    FraisMois frais = control.getData(key);
                    if (!(frais == null)) {
                        frais.setEtape(etape);
                        control.insertDataIntoFraisF(key, frais);
                    }
                }
                backToMainMenu();
            }
        });
    }

    private void backToMainMenu() {
        if (quantity != 0) {
            Toast.makeText(EtapeActivity.this, "Frais Ajouter avec Succes !", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(EtapeActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
