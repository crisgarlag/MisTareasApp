package com.example.mistareas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mistareas.db.ControladorDB;
import com.example.mistareas.db.ConversorHash;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;

/**
 * Clase que se encarga de la activity de login
 * */
public class LoginActivity extends AppCompatActivity {

    private ControladorDB controladorDB;

    /**
     * Metodo para crear la activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        controladorDB = new ControladorDB(this);
        TextView login = (TextView) findViewById(R.id.textoLogin);
        SpannableString loginSubrayado = new SpannableString("LOGIN");
        loginSubrayado.setSpan(new UnderlineSpan(),0, loginSubrayado.length(),0);
        login.setText(loginSubrayado);

    }

    /**
     * Metodo que permite pasar al activity Registro para crear cuentas
     * @param view
     */
    public void crearCuenta(View view){

        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    /**
     * Comprueba si el usuarrio existe en la bbdd, en caso afirmativo compara que la password coincida con la pasada en el textview.
     * Si todo es correcto, se pasará al activity Main
     *
     * @param view
     * @throws NoSuchAlgorithmException
     */
    public void login(View view) throws NoSuchAlgorithmException {
        TextInputEditText usuario = findViewById(R.id.cajaUser);
        TextInputEditText password = findViewById(R.id.cajaPass);
        try{
            String textoUsuario = usuario.getText().toString();
            String textoPassword = password.getText().toString();
            String busquedaUsuario = controladorDB.buscarUsuario(textoUsuario);
            String busquedaPassword = controladorDB.buscarPassword(textoUsuario);
            ConversorHash cvHash = new ConversorHash();
            String passHash = cvHash.convertirAHash(textoPassword);

            if(!textoUsuario.equals("")){
                if(controladorDB.comprobarUsuario(textoUsuario)){
                    if(textoUsuario.equals(busquedaUsuario)
                            && passHash.equals(busquedaPassword)){
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("idUsuario", controladorDB.buscarIDUsuario(textoUsuario));
                        startActivity(intent);
                    }else{
                        toastPersonalizado("Credenciales inválidas");
                    }
                } else{
                    toastPersonalizado("El usuario no existe");
                }
            }else{

                toastPersonalizado("El campo usuario está vacio");
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Permite personalizar los toast de la vista
     * @param texto el texto que mostrará el toast
     */
    public void toastPersonalizado(String texto) {
        Toast toast = new Toast(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toastLinearLayout));
        TextView textoView = layout.findViewById(R.id.textToast);
        textoView.setText(texto);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}