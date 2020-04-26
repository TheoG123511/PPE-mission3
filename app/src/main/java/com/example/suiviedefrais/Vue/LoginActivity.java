package com.example.suiviedefrais.Vue;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.R;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Control controle;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    /**
     * Permet d'initialiser l'activity
     */
    private void init() {
        hideKeyboard(this);
        controle = Control.getInstance(this);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        controle = Control.getInstance(this);
        // controle.setMainActivity(this);
        listenOnBtnLogin();
    }

    /**
     * Permet de ne pas ouvrir le clavier virtuel au lancement de l'activity
     * @param activity L'activity concerner
     */
    public void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Permet de desactivé le button de connection
     */
    public void enabledBtnLogin() {
        btnLogin.setEnabled(true);
    }

    /**
     * Permet d'afficher un toast dans l'interface
     * @param message le message a affiché
     */
    public void displayToast(String message){
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Verifie si l'utilisateur est arriver a se connecter, si oui lance le tableaux de bord
     */
    public void isConnected(){
        // controle.getAuth()
        if (true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * Ecoute le click sur l'ImageButton btnLogin, permet de se connecter a l'application
     */
    private void listenOnBtnLogin(){
        btnLogin.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v){
                btnLogin.setEnabled(false);
                try{
                    // controle.connection(txtUsername.getText().toString(), txtPassword.getText().toString());
                }catch (Exception e){
                    Log.d("LoginActivity", "Erreur dans la fonction listenOnBtnLogin() -> message : " + e.getMessage());
                }
            }
        });
    }


}
