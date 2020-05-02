package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Model.FraisHf;
import com.example.suiviedefrais.Outils.Global;
import com.example.suiviedefrais.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static com.example.suiviedefrais.Outils.MesOutils.generateKey;


public class HfRecapActivity extends AppCompatActivity {

    private DatePicker datHfRecap;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf_recap);
        init();
    }

    private void dateChanged() {
        final DatePicker uneDate = findViewById(R.id.datHfRecap);
        uneDate.init(uneDate.getYear(), uneDate.getMonth(), uneDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.d("DateChanged", "la date a ete changer par l'utilisateur, on mes a jour");
                creerList();
            }
        });
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        datHfRecap = findViewById(R.id.datHfRecap);
        control = Control.getInstance(this);
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datHfRecap), false);
        ImgRetourMenuPrincipal();
        creerList();
        dateChanged();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgHfRecapReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(HfRecapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * creer la liste adapteur qui permet d'afficher la liste de tous les frais hors forfaits
     */
    public void creerList(){
        List<FraisHf> list = new ArrayList<>();
        // on recupere les valeurs
        Integer year = datHfRecap.getYear();
        Integer month = datHfRecap.getMonth();
        // on genere la clee
        Integer key = generateKey(year, month);
        // si la clef existe
        if (control.checkIfKeyExist(key)){
            // si la liste est vide
            if (Objects.requireNonNull(control.getData(key)).getLesFraisHf().size() == 0) {
                displayMessage("Impossible d'afficher les donnees, aucun frais hors forfait trouvée !");
            }
            list = Objects.requireNonNull(control.getData(key)).getLesFraisHf();
        } else {
            displayMessage("Impossible d'afficher les donnees, aucun frais hors forfait trouvée !");
        }
        ListView listView = findViewById(R.id.lstHfRecap);
        HfFraisAdapter adapter = new HfFraisAdapter(HfRecapActivity.this, list, key);
        listView.setAdapter(adapter);
    }

    private void displayMessage(String message) {
        Toast.makeText(HfRecapActivity.this, message, Toast.LENGTH_LONG).show();
    }
}

