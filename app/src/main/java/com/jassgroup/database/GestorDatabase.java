package com.jassgroup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jassgroup.acceso.clases.Usuario;

/**
 * Created by kenwhiston on 29/05/2016.
 */
public class GestorDatabase extends SQLiteOpenHelper {

    /*
        Instancia singleton
        */
    private static GestorDatabase singleton;

    /*
    Etiqueta de depuración
     */
    private static final String TAG = GestorDatabase.class.getSimpleName();


    /*
    Nombre de la base de datos
     */
    public static final String DATABASE_NAME = "tucarta.db";

    /*
    Versión actual de la base de datos
     */
    public static final int DATABASE_VERSION = 1;


    private GestorDatabase(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);

    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
    public static synchronized GestorDatabase getInstance(Context context) {
        if (singleton == null) {
            singleton = new GestorDatabase(context.getApplicationContext());
        }
        return singleton;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear la tabla 'evento'
        db.execSQL(ScriptDatabaseApp.CREAR);
        db.execSQL(ScriptDatabaseUsuario.CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Añade los cambios que se realizarán en el esquema
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabaseApp.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabaseUsuario.TABLE_NAME);
        onCreate(db);
    }

    ///metodos de base de datos////
    public void registrarApp(String appnom, String estado, String intro){
        ContentValues values = new ContentValues();

        values.put(ScriptDatabaseApp.Column.APPNOM, "TuCarta");
        values.put(ScriptDatabaseApp.Column.ESTADO, "1");
        values.put(ScriptDatabaseApp.Column.INTRO, "1");

        // Insertando el registro en la base de datos
        getWritableDatabase().insert(
                ScriptDatabaseApp.TABLE_NAME,
                null,
                values
        );
    }

    /**
     * Obtiene todos los registros de la tabla entrada
     *
     * @return cursor con los registros
     */
    public String obtenerValorApp(String campo) {
        // Seleccionamos todas las filas de la tabla
        String valor="0";
        String sql = "select "+campo+" from "+
                ScriptDatabaseApp.TABLE_NAME+ " limit 1" ;


        Cursor c = getWritableDatabase().rawQuery(
                sql, null);

        if(c==null){
            return "0";
        }

        while (c.moveToNext()) {
            valor = c.getString(0);
        }
        c.close();
        return valor;
    }

    //area de gestion de usuarios
    ///metodos de base de datos////
    public void EliminarUsuarios() {
        // Modificar usuario
        getWritableDatabase().delete(ScriptDatabaseUsuario.TABLE_NAME,
                null,
                null);
    }

    public void registrarUsuario(String uid,Usuario miUsuario){
        ContentValues values = new ContentValues();

        values.put(ScriptDatabaseUsuario.Column.UID, uid);
        values.put(ScriptDatabaseUsuario.Column.APELLIDOS, miUsuario.apellidos);
        values.put(ScriptDatabaseUsuario.Column.NOMBRES, miUsuario.nombres);
        values.put(ScriptDatabaseUsuario.Column.CORREO, miUsuario.correo);
        values.put(ScriptDatabaseUsuario.Column.CLAVE, miUsuario.clave);
        values.put(ScriptDatabaseUsuario.Column.TELEFONO, miUsuario.telefono);
        values.put(ScriptDatabaseUsuario.Column.IMAGEN, miUsuario.imagen);

        // Insertando el registro en la base de datos
        getWritableDatabase().insert(
                ScriptDatabaseUsuario.TABLE_NAME,
                null,
                values
        );
    }

    public void actualizarUsuario(String uid,Usuario miUsuario){
        ContentValues values = new ContentValues();

        values.put(ScriptDatabaseUsuario.Column.UID, uid);
        values.put(ScriptDatabaseUsuario.Column.APELLIDOS, miUsuario.apellidos);
        values.put(ScriptDatabaseUsuario.Column.NOMBRES, miUsuario.nombres);
        //values.put(ScriptDatabaseUsuario.Column.CORREO, miUsuario.correo);
        //values.put(ScriptDatabaseUsuario.Column.CLAVE, miUsuario.clave);
        values.put(ScriptDatabaseUsuario.Column.TELEFONO, miUsuario.telefono);
        //values.put(ScriptDatabaseUsuario.Column.IMAGEN, miUsuario.imagen);

        // Insertando el registro en la base de datos
        getWritableDatabase().update(
                ScriptDatabaseUsuario.TABLE_NAME,
                values,
                null,
                null
        );
    }

    /**
     * Obtiene todos los registros de la tabla entrada
     *
     * @return cursor con los registros
     */
    public String obtenerValorUsuario(String campo) {
        // Seleccionamos todas las filas de la tabla
        String valor="0";
        String sql = "select "+campo+" from "+
                ScriptDatabaseUsuario.TABLE_NAME+ " limit 1" ;


        Cursor c = getWritableDatabase().rawQuery(
                sql, null);

        if(c==null){
            return "0";
        }

        while (c.moveToNext()) {
            valor = c.getString(0);
        }
        c.close();
        return valor;
    }
}
