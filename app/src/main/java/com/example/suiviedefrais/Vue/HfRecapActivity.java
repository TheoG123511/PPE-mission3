package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.example.suiviedefrais.Model.Global;
import com.example.suiviedefrais.R;


public class HfRecapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf_recap);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datHfRecap), false);
        ImgRetourMenuPrincipal();
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

}

