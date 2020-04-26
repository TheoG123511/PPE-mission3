package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.suiviedefrais.R;


public class HfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hf);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        ImgRetourMenuPrincipal();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgHfReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(HfActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
