package com.jassgroup.database;

import android.provider.BaseColumns;

/**
 * Created by kenwhiston on 03/07/2016.
 */
public class ScriptDatabaseUsuario {
    /*
    Etiqueta para Depuración
     */
    private static final String TAG = ScriptDatabaseUsuario.class.getSimpleName();

    //Metainformación de la base de datos
    public static final String TABLE_NAME = "usuario";
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";

    // Campos de la tabla usuario
    public static class Column {
        public static final String ID = BaseColumns._ID;
        public static final String UID = "uid";
        public static final String APELLIDOS = "apellidos";
        public static final String NOMBRES = "nombres";
        public static final String CORREO = "correo";
        public static final String CLAVE = "clave";
        public static final String TELEFONO = "telefono";
        public static final String IMAGEN = "imagen";
    }

    // Comando CREATE para la tabla USUARIO
    public static final String CREAR =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    Column.ID + " " + INT_TYPE + " primary key autoincrement," +
                    Column.UID + " " + STRING_TYPE + " null," +
                    Column.APELLIDOS + " " + STRING_TYPE + " null," +
                    Column.NOMBRES + " " + STRING_TYPE + " null," +
                    Column.CORREO + " " + STRING_TYPE + " null," +
                    Column.CLAVE + " " + STRING_TYPE + " null," +
                    Column.TELEFONO + " " + STRING_TYPE + " null," +
                    Column.IMAGEN + " " + STRING_TYPE + " null )";
}
