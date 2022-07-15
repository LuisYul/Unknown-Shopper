package com.bigoose.unkshopper.database;

import android.provider.BaseColumns;

public class DBTables {

    public DBTables() {
    }

    public static final class T_Foto implements BaseColumns {
        public T_Foto() {
        }

        public static final String TABLE_NAME = "foto";
        public static final String TIPO_FOTO = "tipo_foto";
        public static final String FOTO = "foto";
        public static final String COMENTARIO = "comentario";

        public static final String CREAR_TABLA_FOTO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                // + ID_USUARIO + " INTEGER PRIMARY KEY, "
                + TIPO_FOTO + " VARCHAR(20), "
                + FOTO + " BLOB, "
                + COMENTARIO + " VARCHAR(50)) ";

        public static final String DROP_TABLA_FOTO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita implements BaseColumns {
        public T_Visita() {
        }

        public static final String TABLE_NAME = "visita";
        public static final String ID_VISITA = "id_visita";
        public static final String ID_LOCAL = "id_local";
        public static final String NOMBRE_LOCAL = "nombre_local";
        public static final String HORA_INI = "hora_ini";
        public static final String HORA_FIN = "hora_fin";
        public static final String LATITUD_INI = "latitud_ini";
        public static final String LONGITUD_INI = "longitud_ini";
        public static final String LATITUD_FIN = "latitud_fin";
        public static final String LONGITUD_FIN = "longitud_fin";
        public static final String ID_REGION = "id_region";
        public static final String NOMBRE_REGION = "nombre_region";
        public static final String ID_CLIENTE = "id_cliente";
        public static final String NOMBRE_CLIENTE = "nombre_cliente";
        public static final String ID_GIRO_NEGOCIO = "id_giro_negocio";
        public static final String NOMBRE_GIRO_NEGOCIO = "nombre_giro_negocio";
        public static final String COMENTARIO = "comentario";
        public static final String DIRECCION = "direccion";
        public static final String CONDICION = "condicion";
        public static final String ESTADO = "estado";

        public static final String CREAR_TABLA_VISITA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + ID_LOCAL + " VARCHAR(10), "
                + NOMBRE_LOCAL + " VARCHAR(100), "
                + HORA_INI + " VARCHAR(20), "
                + HORA_FIN + " VARCHAR(20), "
                + LATITUD_INI + " VARCHAR(30), "
                + LONGITUD_INI + " VARCHAR(30), "
                + LATITUD_FIN + " VARCHAR(30), "
                + LONGITUD_FIN + " VARCHAR(30), "
                + ID_REGION + " VARCHAR(10), "
                + NOMBRE_REGION + " VARCHAR(50), "
                + ID_CLIENTE + " VARCHAR(10), "
                + NOMBRE_CLIENTE + " VARCHAR(100), "
                + ID_GIRO_NEGOCIO + " VARCHAR(10), "
                + NOMBRE_GIRO_NEGOCIO + " VARCHAR(100), "
                + COMENTARIO + " VARCHAR(200), "
                + DIRECCION + " VARCHAR(200), "
                + ESTADO + " CHAR(2), "
                + CONDICION + " VARCHAR(10)) ";

        public static final String DROP_TABLA_VISITA = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Tipo_Incidencia implements BaseColumns {
        public T_Tipo_Incidencia() {
        }

        public static final String TABLE_NAME = "tipo_incidencia";
        public static final String ID_TIPO_INCIDENCIA = "id_tipo_incidencia";
        public static final String NOMBRE = "nombre";
        public static final String ESTADO = "estado";

        public static final String CREAR_TABLA_TIPO_INCIDENCIA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_TIPO_INCIDENCIA + " VARCHAR(10), "
                + NOMBRE + " VARCHAR(100), "
                + ESTADO + " CHAR(2)) ";

        public static final String DROP_TABLA_TIPO_INCIDENCIA = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Tipo_Criterio implements BaseColumns {
        public T_Tipo_Criterio() {
        }

        public static final String TABLE_NAME = "tipo_criterio";
        public static final String ID_CRITERIO = "id_criterio";
        public static final String NOMBRE = "nombre";
        public static final String ESTADO = "estado";
        public static final String ID_TIPO_CRITERIO = "id_tipo_criterio";

        public static final String CREAR_TABLA_TIPO_CRITERIO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_CRITERIO + " VARCHAR(10), "
                + NOMBRE + " VARCHAR(100), "
                + ESTADO + " CHAR(2),"
                + ID_TIPO_CRITERIO + " VARCHAR(10)) ";

        public static final String DROP_TABLA_TIPO_CRITERIO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Usuario implements BaseColumns {
        public T_Usuario() {
        }

        public static final String TABLE_NAME = "usuario";
        public static final String ID_USUARIO = "id_usuario";
        public static final String NOMBRE_USUARIO = "nombre_usuario";
        public static final String NUM_IDENTIDAD = "num_identidad";
        public static final String BREVE = "breve";
        public static final String ID_REGION = "id_region";
        public static final String COD_REGION = "cod_region";
        public static final String REGION_1 = "region_1";
        public static final String TIPO_REGION_1 = "tipo_region_1";
        public static final String REGION_2 = "region_2";
        public static final String TIPO_REGION_2 = "tipo_region_2";
        public static final String REGION_3 = "region_3";
        public static final String TIPO_REGION_3 = "tipo_region_3";
        public static final String PERFIL = "perfil";
        public static final String USUARIO = "usuario";
        public static final String CLAVE = "clave";

        public static final String CREAR_TABLA_USUARIO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_USUARIO + " VARCHAR(10), "
                + NOMBRE_USUARIO + " VARCHAR(100), "
                + NUM_IDENTIDAD + " VARCHAR(20), "
                + BREVE + " VARCHAR(20), "
                + ID_REGION + " VARCHAR(20), "
                + COD_REGION + " VARCHAR(20), "
                + REGION_1 + " VARCHAR(20), "
                + TIPO_REGION_1 + " VARCHAR(30), "
                + REGION_2 + " VARCHAR(20), "
                + TIPO_REGION_2 + " VARCHAR(30), "
                + REGION_3 + " VARCHAR(20), "
                + TIPO_REGION_3 + " VARCHAR(30), "
                + PERFIL + " VARCHAR(30), "
                + USUARIO + " VARCHAR(30), "
                + CLAVE + " VARCHAR(30)) ";

        public static final String DROP_TABLA_USUARIO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Criterio_Atributo implements BaseColumns {
        public T_Criterio_Atributo() {
        }

        public static final String TABLE_NAME = "criterio_atributo";
        public static final String ID_VISITA = "id_visita";
        public static final String ID_CRITERIO = "id_criterio";
        public static final String CRITERIO = "criterio";
        public static final String ID_ATRIBUTO = "id_atributo";
        public static final String ATRIBUTO = "atributo";
        public static final String ICONO = "icono";
        public static final String VALOR = "valor";

        public static final String CREAR_TABLA_CRITERIO_ATRIBUTO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + ID_CRITERIO + " VARCHAR(10), "
                + CRITERIO + " VARCHAR(30), "
                + ID_ATRIBUTO + " VARCHAR(10), "
                + ATRIBUTO + " VARCHAR(100), "
                + ICONO + " VARCHAR(20), "
                + VALOR + " VARCHAR(10)) ";

        public static final String DROP_TABLA_CRITERIO_ATRIBUTO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita_Foto implements BaseColumns {
        public T_Visita_Foto() {
        }

        public static final String TABLE_NAME = "visita_foto";
        public static final String ID_VISITA = "id_visita";
        public static final String HORA = "hora";
        public static final String ID_CRITERIO = "id_criterio";
        public static final String COMENTARIO = "comentario";
        public static final String FOTO = "foto";
        public static final String INDICE = "indice";

        public static final String CREAR_TABLA_VISITA_FOTO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + HORA + " VARCHAR(20), "
                + ID_CRITERIO + " VARCHAR(10), "
                + COMENTARIO + " VARCHAR(200), "
                + FOTO + " BLOB, "
                + INDICE + " VARCHAR(10))";

        public static final String DROP_TABLA_VISITA_FOTO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita_Incidencia implements BaseColumns {
        public T_Visita_Incidencia() {
        }

        public static final String TABLE_NAME = "visita_incidencia";
        public static final String ID_VISITA = "id_visita";
        public static final String HORA = "hora";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String ID_TIPO = "id_tipo";
        public static final String COMENTARIO = "comentario";
        public static final String FOTO = "foto";


        public static final String CREAR_TABLA_VISITA_INCIDENCIA = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + HORA + " VARCHAR(20), "
                + LATITUD + " VARCHAR(20), "
                + LONGITUD + " VARCHAR(20), "
                + ID_TIPO + " VARCHAR(10), "
                + COMENTARIO + " VARCHAR(200), "
                + FOTO + " BLOB) ";


        public static final String DROP_TABLA_VISITA_INCIDENCIA = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita_Evaluacion implements BaseColumns {
        public T_Visita_Evaluacion() {
        }

        public static final String TABLE_NAME = "visita_evaluacion";
        public static final String ID_VISITA = "id_visita";
        public static final String HORA = "hora";
        public static final String SCORE = "score";

        public static final String CREAR_TABLA_VISITA_EVALUACION = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10) PRIMARY KEY, "
                + HORA + " VARCHAR(20), "
                + SCORE + " VARCHAR(10))";

        public static final String DROP_TABLA_VISITA_EVALUACION = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita_Criterio implements BaseColumns {
        public T_Visita_Criterio() {
        }

        public static final String TABLE_NAME = "visita_criterio";
        public static final String ID_VISITA = "id_visita";
        public static final String ID_CRITERIO = "id_criterio";
        public static final String SCORE = "score";
        public static final String VALOR = "valor";

        public static final String CREAR_TABLA_VISITA_CRITERIO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + ID_CRITERIO + " VARCHAR(10), "
                + SCORE + " VARCHAR(10), "
                + VALOR + " VARCHAR(10),"
                + "PRIMARY KEY(" + ID_VISITA + "," + ID_CRITERIO + "))";

        public static final String DROP_TABLA_VISITA_CRITERIO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final class T_Visita_Atributo implements BaseColumns {
        public T_Visita_Atributo() {
        }

        public static final String TABLE_NAME = "visita_atributo";
        public static final String ID_VISITA = "id_visita";
        public static final String ID_CRITERIO = "id_criterio";
        public static final String ID_ATRIBUTO = "id_atributo";
        public static final String SCORE = "score";

        public static final String CREAR_TABLA_VISITA_ATRIBUTO = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                + ID_VISITA + " VARCHAR(10), "
                + ID_CRITERIO + " VARCHAR(10), "
                + ID_ATRIBUTO + " VARCHAR(10), "
                + SCORE + " VARCHAR(10),"
                + "PRIMARY KEY(" + ID_VISITA + "," + ID_CRITERIO + "," + ID_ATRIBUTO + "))";

        public static final String DROP_TABLA_VISITA_ATRIBUTO = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
