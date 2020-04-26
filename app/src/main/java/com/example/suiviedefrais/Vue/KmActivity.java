package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.suiviedefrais.Model.Global;
import com.example.suiviedefrais.R;


public class KmActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        txtKm = findViewById(R.id.txtKm);
        txtKm.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datKm), false);
        ImgRetourMenuPrincipal();
        AddOneBtnKm();
        LessOneBtnKm();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgKmReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(KmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddOneBtnKm(){
        (findViewById(R.id.cmdKmPlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity += 1;
                txtKm.setText(String.format("%x", quantity));
            }
        });
    }

    private void LessOneBtnKm(){
        (findViewById(R.id.cmdKmMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtKm.setText(String.format("%x", quantity));
            }
        });
    }
}
