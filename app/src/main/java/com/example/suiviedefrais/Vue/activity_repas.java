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

public class activity_repas extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtRepas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repas);
        init();
    }
    /**
     * Permet d'initialiser l'activity
     */
    private void init(){
        txtRepas = findViewById(R.id.txtRepas);
        txtRepas.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.dateRepas), false);
        ImgRetourMenuPrincipal();
        AddOneBtnRepas();
        LessOneBtnRepas();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgRepasReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(activity_repas.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddOneBtnRepas(){
        (findViewById(R.id.cmdRepasPlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity += 1;
                txtRepas.setText(String.format("%x", quantity));
            }
        });
    }

    private void LessOneBtnRepas(){
        (findViewById(R.id.cmdRepasMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtRepas.setText(String.format("%x", quantity));
            }
        });
    }
}
