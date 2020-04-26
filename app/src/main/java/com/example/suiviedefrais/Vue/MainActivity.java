package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
        ecouteBtnMenuFraisKm();
        ecouteBtnMenuFraisHotel();
        ecouteBtnMenuFraisRepas();
        ecouteBtnMenuFraisEtape();
        ecouteBtnMenuFraisHf();
        ecouteBtnMenuFraisRecap();
        ecouteBtnMenuEnvoieDonnee();
    }

    private void ecouteBtnMenuFraisKm(){
        (findViewById(R.id.cmdKm)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, KmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ecouteBtnMenuFraisHotel(){
        (findViewById(R.id.cmdNuitee)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, NuitActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ecouteBtnMenuFraisEtape(){
        (findViewById(R.id.cmdEtape)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, EtapeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ecouteBtnMenuFraisRepas(){
        (findViewById(R.id.cmdRepas)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, activity_repas.class);
                startActivity(intent);
            }
        });
    }

    private void ecouteBtnMenuFraisHf(){
        (findViewById(R.id.cmdHf)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, HfActivity.class);
                startActivity(intent);
            }
        });
    }


    private void ecouteBtnMenuFraisRecap(){
        (findViewById(R.id.cmdHfRecap)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, HfRecapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ecouteBtnMenuEnvoieDonnee(){
        (findViewById(R.id.cmdTransfert)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Permet d'afficher un toast dans l'interface
     * @param message le message a affiché
     */
    public void displayToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
