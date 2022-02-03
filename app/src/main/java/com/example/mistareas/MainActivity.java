package com.example.mistareas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mistareas.db.ControladorDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Clase que se encarga de la activity main
 */
public class MainActivity extends AppCompatActivity {

    private ControladorDB controladorDB;
    private ListView listViewTareas;
    private ActionBar actionBar;
    private ColorDrawable colorDrawable;

    /**
     * Metodo para crear la activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarVariables();
        actionBar.setBackgroundDrawable(colorDrawable);
        actualizarUI();
    }

    /**
     * Permite iniciailizar las variables utilizadas para crear la activity
     */
    public void inicializarVariables() {
        controladorDB = new ControladorDB(this);
        listViewTareas = (ListView) findViewById(R.id.listaTareas);
        actionBar = getSupportActionBar();
        colorDrawable = new ColorDrawable(Color.parseColor("#009688"));
        FloatingActionButton fab = findViewById(R.id.botonFlotante);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                añadirTarea();
            }
        });
    }

    /**
     * Crea la barra de menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Permite volver a la activity login
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Crea una ventana emergente tras pulsar el boton flotante. El texto introducido
     * en el textView que aparece será insertado en la tabla tareas de la bbdd
     */
    public void añadirTarea() {
        EditText cajaTexto = new EditText(this);
        int dato = this.getIntent().getExtras().getInt("idUsuario");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nueva Tarea")
                .setMessage("¿Qué quieres hacer a continuacion?")
                .setView(cajaTexto)
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controladorDB.addTarea(cajaTexto.getText().toString(), dato);
                        toastPersonalizado("Tarea añadida correctamente");
                        actualizarUI();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();

    }

    /**
     * Crea una ventana emergente tras pulsar el boton flotante. El texto introducido
     * en el textView que aparece modificará la tarea que ya existe en la tabla tareas de la bbdd
     */
    public void modificarTarea(View view) {

        View parent = (View) view.getParent();
        TextView textoTarea = parent.findViewById(R.id.taskTitle);
        EditText cajaTexto = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Modificar Tarea")
                .setMessage("¿Qué quieres hacer a continuacion?")
                .setView(cajaTexto)
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controladorDB.modificarTarea(textoTarea.getText().toString(), cajaTexto.getText().toString());
                        toastPersonalizado("Tarea modificada correctamente");
                        actualizarUI();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.show();
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
     * Actualiza la interfaz para mostrar las tareas del usuario que existen en la bbdd
     */
    public void actualizarUI() {

        ArrayAdapter<String> adapter;
        int idUsuario = this.getIntent().getExtras().getInt("idUsuario");
        if (controladorDB.obtenerTareas(idUsuario) == null) {
            listViewTareas.setAdapter(null);
        } else {

            adapter = new ArrayAdapter<String>(this, R.layout.tarea, R.id.taskTitle, controladorDB.obtenerTareas(idUsuario));
            listViewTareas.setAdapter(adapter);
        }
    }

    /**
     * Pulsando sobre checkbox elimina la tarea de la bbdd
     * @param view
     */
    public void borrarTarea(View view) {
        View parent = (View) view.getParent();
        TextView tareaTextView = (TextView) parent.findViewById(R.id.taskTitle);
        controladorDB.borrarTarea(tareaTextView.getText().toString());
        actualizarUI();
        toastPersonalizado("Tarea eliminada correctamente");

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