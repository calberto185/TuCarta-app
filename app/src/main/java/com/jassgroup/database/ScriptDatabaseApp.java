package com.jassgroup.database;

import android.provider.BaseColumns;

/**
 * Created by kenwhiston on 29/05/2016.
 */
public class ScriptDatabaseApp {

    /*
    Etiqueta para Depuración
     */
    private static final String TAG = ScriptDatabaseApp.class.getSimpleName();

    //Metainformación de la base de datos
    public static final String TABLE_NAME = "app";
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";

    // Campos de la tabla usuario
    public static class Column {
        public static final String ID = BaseColumns._ID;
        public static final String APPNOM = "appnom";
        public static final String ESTADO = "estado";
        public static final String INTRO = "intro";
    }

    // Comando CREATE para la tabla USUARIO
    public static final String CREAR =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    Column.ID + " " + INT_TYPE + " primary key autoincrement," +
                    Column.APPNOM + " " + STRING_TYPE + " null," +
                    Column.ESTADO + " " + STRING_TYPE + " null," +
                    Column.INTRO + " " + STRING_TYPE + " null )";
}
