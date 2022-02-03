package com.example.mistareas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.NoSuchAlgorithmException;

/**
 * Clase controladora de la base de datos de la aplicacion.
 */
public class ControladorDB extends SQLiteOpenHelper {

    public ControladorDB(Context context) {
        super(context, "com.example.mistareas.db", null, 1);
    }

    /**
     * Metodo on create para la cracion de las tablas de la base de datos
     *
     * @param db la base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, user TEXT NOT NULL UNIQUE, pass TEXT not null);");
        db.execSQL("CREATE TABLE tareas(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, userID INTEGER, FOREIGN KEY (userID) REFERENCES usuarios (user));");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Permite añadir tareas a la bbdd
     *
     * @param tarea   a insertar en la bbdd
     * @param usuario quien realiza la tarea
     */
    public void addTarea(String tarea, int usuario) {

        ContentValues registro = new ContentValues();
        registro.put("nombre", tarea);
        registro.put("userID", usuario);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tareas", null, registro);
        db.close();

    }

    /**
     * Permite añadir usuarios a la bbdd
     *
     * @param usuario quien se va a añadir
     * @param pass    contraseña del usuario
     * @throws NoSuchAlgorithmException
     */
    public void addUsuario(String usuario, String pass) throws NoSuchAlgorithmException {
        ConversorHash cvHash = new ConversorHash();
        String passHash = cvHash.convertirAHash(pass);
        ContentValues registro = new ContentValues();
        registro.put("user", usuario);
        registro.put("pass", passHash);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("usuarios", null, registro);
        db.close();
    }


    /**
     * Busca las tareas de un usuario en la tabla de tareas de la bbdd
     *
     * @param usuario es el id del usuario que ha registrado la tarea
     * @return null si no hay tareas y un array con todas las tareas en caso de haber una o mas
     */
    public String[] obtenerTareas(int usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tareas WHERE userID=?", new String[]{String.valueOf(usuario)});
        int registros = cursor.getCount();
        if (registros == 0) {
            cursor.close();
            db.close();
            return null;
        } else {
            String[] tareas = new String[registros];
            cursor.moveToFirst();
            for (int i = 0; i < registros; i++) {
                tareas[i] = cursor.getString(1);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return tareas;
        }
    }

    /**
     * Borra la tarea pasada como parametros de la tabla tareas de la bbdd
     *
     * @param tarea a borrar
     */
    public void borrarTarea(String tarea) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TAREAS", "NOMBRE=?", new String[]{tarea});
        db.close();
    }

    /**
     * Permite modificar una tarea de la tabla tareas por otra nueva
     *
     * @param tareaActual es la tarea existente en la bbdd
     * @param tareaNueva  es la que va a modificar a la existente
     */
    public void modificarTarea(String tareaActual, String tareaNueva) {

        ContentValues registro = new ContentValues();
        registro.put("nombre", tareaNueva);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("tareas", registro, "nombre=?", new String[]{tareaActual});
    }

    /**
     * Permite comprobar la existencia de un usuario en la tabla usuarios de la bbdd
     *
     * @param usuario a buscar
     * @return true si existe y false si no existe
     */
    public boolean comprobarUsuario(String usuario) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user FROM usuarios WHERE user=?", new String[]{usuario});
        int registro = cursor.getCount();
        cursor.close();
        if (registro == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permite saber si se encuentra en la bbdd el usuario pasado como parametro y si es así lo devuelve para poder realizar login
     *
     * @param usuario a buscar
     * @return nombre del usuario si existe en la tabla usuarios y null si no existe
     */
    public String buscarUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT user FROM usuarios WHERE user=?", new String[]{usuario});
        String usuarioQuery;
        int registro = cursor.getCount();
        if (registro > 0) {
            cursor.moveToFirst();
            usuarioQuery = cursor.getString(0);
            cursor.close();
            db.close();
        } else {
            return null;
        }
        return usuarioQuery;
    }

    /**
     * Permite obtener el id del usuario pasado como parametro para poder visualizar las tareas de cada usuario en la activity main
     *
     * @param usuario a buscar
     * @return el id del usuario
     */
    public int buscarIDUsuario(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE user=?", new String[]{usuario});
        int iDUsuario;
        cursor.moveToFirst();
        iDUsuario = cursor.getInt(0);
        cursor.close();
        db.close();
        return iDUsuario;
    }

    /**
     * Permite saber si se encuentra en la bbdd la pass del usuario pasado como parametro y si es así lo devuelve para poder realizar login
     *
     * @param usuario del que se busca la password
     * @return null si no existe el string y la password en caso de existir
     */
    public String buscarPassword(String usuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pass FROM usuarios WHERE user=?", new String[]{usuario});
        String passwordQuery;
        int registro = cursor.getCount();
        if (registro > 0) {
            cursor.moveToFirst();
            passwordQuery = cursor.getString(0);
            cursor.close();
            db.close();
        } else {
            return null;
        }

        return passwordQuery;
    }


}
