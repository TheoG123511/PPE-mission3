package com.example.suiviedefrais.Vue;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.R;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        controle = Control.getInstance(this);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        controle.setMainActivity(this);
        listenOnBtnLogin();
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
        txtUsername.setText("");
        txtPassword.setText("");
        if (controle.getAuth()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    /**
     * Ecoute le click sur l'ImageButton btnLogin, permet de se connecter au serveur
     */
    private void listenOnBtnLogin(){
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                btnLogin.setEnabled(false);
                try{
                    controle.connection(txtUsername.getText().toString(), txtPassword.getText().toString());
                    //controle.connection("lvillachane", "jux7g");
                }catch (Exception e){
                    Log.d("LoginActivity", "Erreur dans la fonction listenOnBtnLogin() -> message : " + e.getMessage());
                    displayToast("Une Erreur c'est produite, Merci de remplir tous les champs !");
                }
            }
        });
    }


}
