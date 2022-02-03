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
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
/**
 * Clase que se encarga de la activity de registro
 * */
public class RegistroActivity extends AppCompatActivity {

    ControladorDB controladorDB;

    /**
     * Metodo para crear la activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();
        controladorDB = new ControladorDB(this);
        TextView registrar = (TextView) findViewById(R.id.textoRegistrarRegistro);
        SpannableString registrarSubrayado = new SpannableString("REGISTRO");
        registrarSubrayado.setSpan(new UnderlineSpan(), 0, registrarSubrayado.length(), 0);
        registrar.setText(registrarSubrayado);

    }

    /**
     * Permite crear un usuario nuevo. Primero comprueba si este existe en la bbdd, y si no existe y los dos textViews
     * se encuentran cumplimentados se creará el usuario y pasara a la activity main
     *
     * @param view
     * @throws NoSuchAlgorithmException
     */
    public void usuarioNuevo(View view) throws NoSuchAlgorithmException {

        TextInputEditText usuario = findViewById(R.id.usuarioRegistro);
        TextInputEditText password = findViewById(R.id.passwordRegistro);

        String textoUsuario = usuario.getText().toString();
        String textoPassword = password.getText().toString();

        if (!controladorDB.comprobarUsuario(textoUsuario)) {
            if (!textoPassword.equals("") && !textoUsuario.equals("")) {
                controladorDB.addUsuario(textoUsuario, textoPassword);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("idUsuario", controladorDB.buscarIDUsuario(textoUsuario));
                startActivity(intent);
            } else {
                toastPersonalizado("Debe cumplimentar los dos campos");
            }

        } else {
            toastPersonalizado("El usuario ya existe");
        }
    }

    /**
     * Permite volver a la activity login
     *
     * @param view
     */
    public void volverLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    /**
     * Permite personalizar los toast de la vista
     *
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