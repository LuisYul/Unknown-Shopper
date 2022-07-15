package com.bigoose.unkshopper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "unk_shopper";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBTables.T_Foto.CREAR_TABLA_FOTO);
        db.execSQL(DBTables.T_Visita.CREAR_TABLA_VISITA);
        db.execSQL(DBTables.T_Tipo_Incidencia.CREAR_TABLA_TIPO_INCIDENCIA);
        db.execSQL(DBTables.T_Usuario.CREAR_TABLA_USUARIO);
        db.execSQL(DBTables.T_Criterio_Atributo.CREAR_TABLA_CRITERIO_ATRIBUTO);
        db.execSQL(DBTables.T_Tipo_Criterio.CREAR_TABLA_TIPO_CRITERIO);
        db.execSQL(DBTables.T_Visita_Foto.CREAR_TABLA_VISITA_FOTO);
        db.execSQL(DBTables.T_Visita_Evaluacion.CREAR_TABLA_VISITA_EVALUACION);
        db.execSQL(DBTables.T_Visita_Criterio.CREAR_TABLA_VISITA_CRITERIO);
        db.execSQL(DBTables.T_Visita_Atributo.CREAR_TABLA_VISITA_ATRIBUTO);
        db.execSQL(DBTables.T_Visita_Incidencia.CREAR_TABLA_VISITA_INCIDENCIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DBTables.T_Foto.DROP_TABLA_FOTO);
        db.execSQL(DBTables.T_Visita.DROP_TABLA_VISITA);
        db.execSQL(DBTables.T_Tipo_Incidencia.DROP_TABLA_TIPO_INCIDENCIA);
        db.execSQL(DBTables.T_Usuario.DROP_TABLA_USUARIO);
        db.execSQL(DBTables.T_Criterio_Atributo.DROP_TABLA_CRITERIO_ATRIBUTO);
        db.execSQL(DBTables.T_Tipo_Criterio.DROP_TABLA_TIPO_CRITERIO);
        db.execSQL(DBTables.T_Visita_Foto.DROP_TABLA_VISITA_FOTO);
        db.execSQL(DBTables.T_Visita_Evaluacion.DROP_TABLA_VISITA_EVALUACION);
        db.execSQL(DBTables.T_Visita_Criterio.DROP_TABLA_VISITA_CRITERIO);
        db.execSQL(DBTables.T_Visita_Atributo.DROP_TABLA_VISITA_ATRIBUTO);
        db.execSQL(DBTables.T_Visita_Incidencia.DROP_TABLA_VISITA_INCIDENCIA);
    }
}
