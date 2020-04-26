package com.example.suiviedefrais.Vue;

import androidx.appcompat.app.AppCompatActivity;
import com.example.suiviedefrais.Model.Global;
import com.example.suiviedefrais.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class EtapeActivity extends AppCompatActivity {

    private Integer quantity = 0;
    private TextView txtEtape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etape);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init(){
        txtEtape = findViewById(R.id.txtEtape);
        txtEtape.setText(String.format("%x", quantity));
        Global.changeAfficheDate((DatePicker) findViewById(R.id.datEtape), false);
        ImgRetourMenuPrincipal();
        AddOneBtnEtape();
        LessOneBtnEtape();
    }

    /**
     * Permet de revenir sur le menu principal
     */
    private void ImgRetourMenuPrincipal(){
        (findViewById(R.id.imgEtapeReturn)).setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(EtapeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddOneBtnEtape(){
        (findViewById(R.id.cmdEtapePlus)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity += 1;
                txtEtape.setText(String.format("%x", quantity));
            }
        });
    }

    private void LessOneBtnEtape(){
        (findViewById(R.id.cmdEtapeMoins)).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                quantity -= 1;
                if (quantity < 0) {
                    quantity = 0;
                }
                txtEtape.setText(String.format("%x", quantity));
            }
        });
    }
}
