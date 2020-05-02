package com.example.suiviedefrais.Vue;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.suiviedefrais.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        // ecoute des click sur les differents ImgButton du menu principal de l'application
        ecouteBtnMenuFraisKm();
        ecouteBtnMenuFraisHotel();
        ecouteBtnMenuFraisRepas();
        ecouteBtnMenuFraisEtape();
        ecouteBtnMenuFraisHf();
        ecouteBtnMenuFraisRecap();
        ecouteBtnMenuEnvoieDonnee();
    }

    /**
     * Permet d'acceder a la vue qui gere les frais KM
     */
    private void ecouteBtnMenuFraisKm(){
        (findViewById(R.id.cmdKm)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, KmActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui gere les frais Nuitée
     */
    private void ecouteBtnMenuFraisHotel(){
        (findViewById(R.id.cmdNuitee)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, NuitActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui gere les frais Etape
     */
    private void ecouteBtnMenuFraisEtape(){
        (findViewById(R.id.cmdEtape)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, EtapeActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui gere les frais Repas
     */
    private void ecouteBtnMenuFraisRepas(){
        (findViewById(R.id.cmdRepas)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, activity_repas.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui gere l'ajout des frais hors forfait
     */
    private void ecouteBtnMenuFraisHf(){
        (findViewById(R.id.cmdHf)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, HfActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui permet d'afficher les frais hors forfait enregister
     */
    private void ecouteBtnMenuFraisRecap(){
        (findViewById(R.id.cmdHfRecap)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, HfRecapActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'acceder a la vue qui gere l'envoie des donnees vers le serveur
     */
    private void ecouteBtnMenuEnvoieDonnee(){
        (findViewById(R.id.cmdTransfert)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
