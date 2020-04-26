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

public class NuitActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtNuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuit);
        init();
    }
    /**
     * Permet d'initialiser l'activity
     */
    private void init(){
        txtNuit = findViewById(R.id.txtNuit);
        txtNuit.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datNuit), false);
        ImgRetourMenuPrincipal();
        AddOneBtnNuit();
        LessOneBtnNuit();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgNuitReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(NuitActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddOneBtnNuit(){
        (findViewById(R.id.cmdNuitPlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity += 1;
                txtNuit.setText(String.format("%x", quantity));
            }
        });
    }

    private void LessOneBtnNuit(){
        (findViewById(R.id.cmdNuitMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtNuit.setText(String.format("%x", quantity));
            }
        });
    }
}
