package com.bigoose.unkshopper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewStructure;

import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.Foto;
import com.bigoose.unkshopper.model.TipoCriterio;
import com.bigoose.unkshopper.model.TipoIncidencia;
import com.bigoose.unkshopper.model.Usuario;
import com.bigoose.unkshopper.model.Valoracion;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.model.VisitaAtributo;
import com.bigoose.unkshopper.model.VisitaCriterio;
import com.bigoose.unkshopper.model.VisitaEvaluacion;
import com.bigoose.unkshopper.model.VisitaFoto;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DBClasses {
    Context context;
    DBHelper dbHelper;
    private String TAG;
    Gson g = new Gson();

    public DBClasses(Context context) {
        this.context = context;
    }

    /*=========================================== FOTOS ============================================*/

    public void Agregar_Visita_Foto(VisitaFoto visitaFoto) {
        TAG = "add visita foto";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita_Foto.ID_VISITA, visitaFoto.getIdVisita());
        cv.put(DBTables.T_Visita_Foto.HORA, visitaFoto.getHora());
        cv.put(DBTables.T_Visita_Foto.ID_CRITERIO, visitaFoto.getIdCriterio());
        cv.put(DBTables.T_Visita_Foto.COMENTARIO, visitaFoto.getComentario());
        cv.put(DBTables.T_Visita_Foto.FOTO, visitaFoto.getFotoByte());
        cv.put(DBTables.T_Visita_Foto.INDICE, visitaFoto.getIndice());

        long res = db.insert(DBTables.T_Visita_Foto.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");

    }

    public void Agregar_Visita_Incidencia(VisitaFoto visitaFoto) {
        TAG = "add visita inc";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita_Incidencia.ID_VISITA, visitaFoto.getIdVisita());
        cv.put(DBTables.T_Visita_Incidencia.HORA, visitaFoto.getHora());
        cv.put(DBTables.T_Visita_Incidencia.LATITUD, visitaFoto.getLatitud());
        cv.put(DBTables.T_Visita_Incidencia.LONGITUD, visitaFoto.getLongitud());
        cv.put(DBTables.T_Visita_Incidencia.ID_TIPO, visitaFoto.getIdTipo());
        cv.put(DBTables.T_Visita_Incidencia.COMENTARIO, visitaFoto.getComentario());
        cv.put(DBTables.T_Visita_Incidencia.FOTO, visitaFoto.getFotoByte());

        long res = db.insert(DBTables.T_Visita_Incidencia.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");

    }

    public List<VisitaFoto> Recuperar_Todas_Visita_Foto() {
        TAG = "get visita foto";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita_Foto.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        VisitaFoto visitaFoto = null;
        List<VisitaFoto> visitaFotoList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visitaFoto = new VisitaFoto();
                    visitaFoto.setIdVisita(c.getString(0));
                    visitaFoto.setHora(c.getString(1));
                    visitaFoto.setIdCriterio(c.getString(2));
                    visitaFoto.setComentario(c.getString(3));
                    byte[] foto = c.getBlob(4);
                    visitaFoto.setFotoByte(foto);
                    visitaFoto.setIndice(c.getString(5));
                    visitaFotoList.add(visitaFoto);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return visitaFotoList;
    }

    public List<VisitaFoto> Recuperar_Todas_Visita_Incidencia() {
        TAG = "get visita inci";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita_Incidencia.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        VisitaFoto visitaFoto = null;
        List<VisitaFoto> visitaFotoList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visitaFoto = new VisitaFoto();
                    visitaFoto.setIdVisita(c.getString(0));
                    visitaFoto.setHora(c.getString(1));
                    visitaFoto.setLatitud(c.getString(2));
                    visitaFoto.setLongitud(c.getString(3));
                    visitaFoto.setIdTipo(c.getString(4));
                    visitaFoto.setComentario(c.getString(5));
                    byte[] foto = c.getBlob(6);
                    visitaFoto.setFotoByte(foto);
                    visitaFotoList.add(visitaFoto);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return visitaFotoList;
    }


    /*=========================================== VISITA ============================================*/

    public void Agregar_Visita(Visita visita) {
        TAG = "add visita";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita.ID_VISITA, visita.getIdVisita());
        cv.put(DBTables.T_Visita.ID_LOCAL, visita.getIdLocal());
        cv.put(DBTables.T_Visita.NOMBRE_LOCAL, visita.getNombre_local());
        cv.put(DBTables.T_Visita.HORA_INI, visita.getHoraIni());
        cv.put(DBTables.T_Visita.HORA_FIN, visita.getHoraFin());
        cv.put(DBTables.T_Visita.LATITUD_INI, visita.getLatitudIni());
        cv.put(DBTables.T_Visita.LONGITUD_INI, visita.getLongitudIni());
        cv.put(DBTables.T_Visita.LATITUD_FIN, visita.getLatitudFin());
        cv.put(DBTables.T_Visita.LONGITUD_FIN, visita.getLongitudFin());
        cv.put(DBTables.T_Visita.ID_REGION, visita.getIdRegion());
        cv.put(DBTables.T_Visita.NOMBRE_REGION, visita.getNombre_region());
        cv.put(DBTables.T_Visita.ID_CLIENTE, visita.getIdCliente());
        cv.put(DBTables.T_Visita.NOMBRE_CLIENTE, visita.getNombre_cliente());
        cv.put(DBTables.T_Visita.ID_GIRO_NEGOCIO, visita.getIdGiroNegocio());
        cv.put(DBTables.T_Visita.NOMBRE_GIRO_NEGOCIO, visita.getNombre_giroNegocio());
        cv.put(DBTables.T_Visita.COMENTARIO, visita.getComentario());
        cv.put(DBTables.T_Visita.DIRECCION, visita.getDireccion());
        cv.put(DBTables.T_Visita.CONDICION, visita.getCondicion());
        cv.put(DBTables.T_Visita.ESTADO, 0);

        long res = db.replace(DBTables.T_Visita.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<Visita> Recuperar_Todas_Visitas() {
        TAG = "get visitas";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        Visita visita = null;
        List<Visita> visitaList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visita = new Visita();
                    visita.setIdVisita(c.getString(0));
                    visita.setIdLocal(c.getString(1));
                    visita.setNombre_local(c.getString(2));
                    visita.setHoraIni(c.getString(3));
                    visita.setHoraFin(c.getString(4));
                    visita.setLatitudIni(c.getString(5));
                    visita.setLongitudIni(c.getString(6));
                    visita.setLatitudFin(c.getString(7));
                    visita.setLongitudFin(c.getString(8));
                    visita.setIdRegion(c.getString(9));
                    visita.setNombre_region(c.getString(10));
                    visita.setIdCliente(c.getString(11));
                    visita.setNombre_cliente(c.getString(12));
                    visita.setIdGiroNegocio(c.getString(13));
                    visita.setNombre_giroNegocio(c.getString(14));
                    visita.setComentario(c.getString(15));
                    visita.setDireccion(c.getString(16));
                    visita.setCondicion(c.getString(18));
                    visitaList.add(visita);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        c.close();
        db.close();
        return visitaList;
    }

    public List<Visita> Recuperar_Todas_Visitas_Pendientes() {
        TAG = "get visitas";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita.TABLE_NAME+ " WHERE "
                +DBTables.T_Visita.ESTADO+" = 1";
        Log.w("sql vis pend",query);
        Cursor c = db.rawQuery(query, null);
        Visita visita = null;
        List<Visita> visitaList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visita = new Visita();
                    visita.setIdVisita(c.getString(0));
                    visita.setHoraIni(c.getString(3));
                    visita.setHoraFin(c.getString(4));
                    visita.setLatitudIni(c.getString(5));
                    visita.setLongitudIni(c.getString(6));
                    visita.setLatitudFin(c.getString(7));
                    visita.setLongitudFin(c.getString(8));
                    visita.setComentario(c.getString(15));
                    visita.setCondicion(c.getString(18));
                    Log.w("visita pend",g.toJson(visita));
                    visitaList.add(visita);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        c.close();
        db.close();
        return visitaList;
    }

    public Visita Recuperar_Visita(String idVisita) {
        TAG = "get visita";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita.TABLE_NAME + " WHERE " +
                DBTables.T_Visita.ID_VISITA + " = '" + idVisita + "'";
        Cursor c = db.rawQuery(query, null);
        Visita visita = new Visita();

        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                visita.setIdVisita(c.getString(0));
                visita.setHoraIni(c.getString(3));
                visita.setHoraFin(c.getString(4));
                visita.setLatitudIni(c.getString(5));
                visita.setLongitudIni(c.getString(6));
                visita.setLatitudFin(c.getString(7));
                visita.setLongitudFin(c.getString(8));
                visita.setCondicion(c.getString(18));
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return visita;
    }

    public void Actualizar_Visita(String idVisita, String hora, double latitud,
                                  double longitud, String tipo) {
        TAG = "update visitas";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long res = -1;
        ContentValues cv = new ContentValues();

        if (tipo.equals("ingreso")) {
            cv.put(DBTables.T_Visita.HORA_INI, hora);
            cv.put(DBTables.T_Visita.LATITUD_INI, latitud);
            cv.put(DBTables.T_Visita.LONGITUD_INI, longitud);
            cv.put(DBTables.T_Visita.HORA_FIN, "");
            cv.put(DBTables.T_Visita.LATITUD_FIN, "");
            cv.put(DBTables.T_Visita.LONGITUD_FIN, "");
        } else {
            cv.put(DBTables.T_Visita.HORA_FIN, hora);
            cv.put(DBTables.T_Visita.LATITUD_FIN, latitud);
            cv.put(DBTables.T_Visita.LONGITUD_FIN, longitud);
        }
        String where = DBTables.T_Visita.ID_VISITA + " = '" + idVisita + "'";
        res = db.update(DBTables.T_Visita.TABLE_NAME, cv, where, null);
        db.close();
        Log.e(TAG, res + "");
    }

    public void Cambiar_Estado_Visita(String idVisita, int nuevo_estado) {
        TAG = "update estado visita";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long res;
        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita.ESTADO, nuevo_estado);
        String where = DBTables.T_Visita.ID_VISITA + " = '" + idVisita + "'";
        res = db.update(DBTables.T_Visita.TABLE_NAME, cv, where, null);
        db.close();
        Log.e(TAG, res + "");
    }

    public void Borrar_Tabla_Completa(String nombre_tabla) {
        TAG = "delete visitas";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "DELETE FROM " + nombre_tabla;
        db.execSQL(query);
        db.close();
    }

    /*=========================================== TIPO INCIDENCIAS ============================================*/

    public void Agregar_Tipo_Incidencia(TipoIncidencia tipoIncidencia) {
        TAG = "add tipo inc";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Tipo_Incidencia.ID_TIPO_INCIDENCIA, tipoIncidencia.getIdTipoIncidencia());
        cv.put(DBTables.T_Tipo_Incidencia.NOMBRE, tipoIncidencia.getNombre());
        cv.put(DBTables.T_Tipo_Incidencia.ESTADO, tipoIncidencia.getEstado());

        long res = db.replace(DBTables.T_Tipo_Incidencia.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<TipoIncidencia> Recuperar_Todas_Tipo_Incidencias() {
        TAG = "get tipo inc";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Tipo_Incidencia.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        TipoIncidencia tipoIncidencia = null;
        List<TipoIncidencia> tipoIncidenciaList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    tipoIncidencia = new TipoIncidencia();
                    tipoIncidencia.setIdTipoIncidencia(c.getString(0));
                    tipoIncidencia.setNombre(c.getString(1));
                    tipoIncidencia.setEstado(c.getString(2));
                    tipoIncidenciaList.add(tipoIncidencia);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return tipoIncidenciaList;
    }

    public String Recuperar_Id_Tipo_Inc(String tipoInc) {
        TAG = "get id tipo inc";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBTables.T_Tipo_Incidencia.ID_TIPO_INCIDENCIA + " FROM " + DBTables.T_Tipo_Incidencia.TABLE_NAME +
                " WHERE " + DBTables.T_Tipo_Incidencia.NOMBRE + " = '" + tipoInc + "'";
        Cursor c = db.rawQuery(query, null);
        String idTipo = "";
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                idTipo = c.getString(0);
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return idTipo;
    }

    /*=========================================== TIPO CRITERIOS ============================================*/

    public void Agregar_Tipo_Criterio(TipoCriterio tipoCriterio) {
        TAG = "add tipo cri";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Tipo_Criterio.ID_CRITERIO, tipoCriterio.getIdCriterio());
        cv.put(DBTables.T_Tipo_Criterio.NOMBRE, tipoCriterio.getNombre());
        cv.put(DBTables.T_Tipo_Criterio.ESTADO, tipoCriterio.getEstado());
        cv.put(DBTables.T_Tipo_Criterio.ID_TIPO_CRITERIO, tipoCriterio.getIdTipoCriterio());

        long res = db.replace(DBTables.T_Tipo_Criterio.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<TipoCriterio> Recuperar_Todas_Tipo_Criterio(String idVisita) {
        TAG = "get tipo cri";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT criterio FROM " + DBTables.T_Criterio_Atributo.TABLE_NAME + " WHERE " +
                DBTables.T_Criterio_Atributo.ID_VISITA + " = '" + idVisita + "' GROUP BY criterio";
        Cursor c = db.rawQuery(query, null);
        TipoCriterio tipoCriterio = null;
        List<TipoCriterio> tipoCriterioList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    tipoCriterio = new TipoCriterio();
                    tipoCriterio.setNombre(c.getString(0));
                    tipoCriterioList.add(tipoCriterio);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return tipoCriterioList;
    }


    public String Recuperar_Id_Criterio(String criterio, String idVisita) {
        TAG = "get id cri";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + DBTables.T_Criterio_Atributo.ID_CRITERIO + " FROM " + DBTables.T_Criterio_Atributo.TABLE_NAME +
                " WHERE " + DBTables.T_Criterio_Atributo.CRITERIO + " = '" + criterio + "' AND id_visita = '" + idVisita + "'";
        Cursor c = db.rawQuery(query, null);
        String idCriterio = "";
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                idCriterio = c.getString(0);
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return idCriterio;
    }

    /*=========================================== USUARIO ============================================*/

    public void Agregar_Usuario(Usuario usuario) {
        TAG = "add user";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Usuario.ID_USUARIO, usuario.getIdUsuario());
        cv.put(DBTables.T_Usuario.NOMBRE_USUARIO, usuario.getNombre_usuario());
        cv.put(DBTables.T_Usuario.NUM_IDENTIDAD, usuario.getNumIdentidad());
        cv.put(DBTables.T_Usuario.BREVE, usuario.getBreve());
        cv.put(DBTables.T_Usuario.ID_REGION, usuario.getIdRegion());
        cv.put(DBTables.T_Usuario.COD_REGION, usuario.getCodRegion());
        cv.put(DBTables.T_Usuario.REGION_1, usuario.getRegion_1());
        cv.put(DBTables.T_Usuario.TIPO_REGION_1, usuario.getTipo_region_1());
        cv.put(DBTables.T_Usuario.REGION_2, usuario.getTipo_region_2());
        cv.put(DBTables.T_Usuario.TIPO_REGION_2, usuario.getTipo_region_2());
        cv.put(DBTables.T_Usuario.REGION_3, usuario.getRegion_3());
        cv.put(DBTables.T_Usuario.TIPO_REGION_3, usuario.getTipo_region_3());
        cv.put(DBTables.T_Usuario.PERFIL, usuario.getPerfil());
        cv.put(DBTables.T_Usuario.USUARIO, usuario.getUsuario());
        cv.put(DBTables.T_Usuario.CLAVE, usuario.getClave());

        long res = db.replace(DBTables.T_Usuario.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public Usuario Recuperar_Usuario() {
        TAG = "get tipo inc";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Usuario.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        Usuario usuario = null;
        try {
            if (c.getCount() > 0) {
                c.moveToFirst();
                usuario = new Usuario();
                usuario.setIdUsuario(c.getString(0));
                usuario.setNombre_usuario(c.getString(1));
                usuario.setNumIdentidad(c.getString(2));
                usuario.setBreve(c.getString(3));
                usuario.setIdRegion(c.getString(4));
                usuario.setCodRegion(c.getString(5));
                usuario.setRegion_1(c.getString(6));
                usuario.setTipo_region_1(c.getString(7));
                usuario.setRegion_2(c.getString(8));
                usuario.setTipo_region_2(c.getString(9));
                usuario.setRegion_3(c.getString(10));
                usuario.setTipo_region_3(c.getString(11));
                usuario.setPerfil(c.getString(12));
                usuario.setUsuario(c.getString(13));
                usuario.setClave(c.getString(14));
                c.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return usuario;
    }

    /*=========================================== CRITERIO ATRIBUTO ============================================*/

    public void Agregar_Criterio_Atributo(CriterioAtributo criterioAtributo) {
        TAG = "add crit atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Criterio_Atributo.ID_VISITA, criterioAtributo.getIdVisita());
        cv.put(DBTables.T_Criterio_Atributo.ID_CRITERIO, criterioAtributo.getIdCriterio());
        cv.put(DBTables.T_Criterio_Atributo.CRITERIO, criterioAtributo.getCriterio());
        cv.put(DBTables.T_Criterio_Atributo.ID_ATRIBUTO, criterioAtributo.getIdAtributo());
        cv.put(DBTables.T_Criterio_Atributo.ATRIBUTO, criterioAtributo.getAtributo());
        cv.put(DBTables.T_Criterio_Atributo.ICONO, criterioAtributo.getIcono());
        cv.put(DBTables.T_Criterio_Atributo.VALOR, criterioAtributo.getValor());

        long res = db.replace(DBTables.T_Criterio_Atributo.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<CriterioAtributo> Recuperar_Todos_Criterio_Atributo() {
        TAG = "get crit atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Criterio_Atributo.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        CriterioAtributo criterioAtributo = null;
        List<CriterioAtributo> criterioAtributos = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    criterioAtributo = new CriterioAtributo();
                    criterioAtributo.setIdVisita(c.getString(0));
                    criterioAtributo.setIdCriterio(c.getString(1));
                    criterioAtributo.setCriterio(c.getString(2));
                    criterioAtributo.setIdAtributo(c.getString(3));
                    criterioAtributo.setAtributo(c.getString(4));
                    criterioAtributo.setIcono(c.getString(5));
                    criterioAtributo.setValor(c.getString(6));
                    criterioAtributos.add(criterioAtributo);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return criterioAtributos;
    }

    public List<CriterioAtributo> Recuperar_Lista_Valoracion(String criterio, String idVisita) {
        TAG = "get crit atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Criterio_Atributo.TABLE_NAME + " WHERE " +
                DBTables.T_Criterio_Atributo.CRITERIO + " = '" + criterio + "' AND " + DBTables.T_Criterio_Atributo.ID_VISITA +
                " = '" + idVisita + "'";
        Cursor c = db.rawQuery(query, null);
        CriterioAtributo criterioAtributo = null;
        List<CriterioAtributo> criterioAtributos = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    criterioAtributo = new CriterioAtributo();
                    criterioAtributo.setIdVisita(c.getString(0));
                    criterioAtributo.setIdCriterio(c.getString(1));
                    criterioAtributo.setCriterio(c.getString(2));
                    criterioAtributo.setIdAtributo(c.getString(3));
                    criterioAtributo.setAtributo(c.getString(4));
                    criterioAtributo.setIcono(c.getString(5));
                    criterioAtributo.setValor(5 + "");
                    criterioAtributo.setPuntaje(20);
                    criterioAtributos.add(criterioAtributo);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return criterioAtributos;
    }

    public List<CriterioAtributo> Obtener_Tipos_Criterio_Atributo(String idVisita) {
        TAG = "get tipo crit atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT DISTINCT criterio, id_criterio FROM " + DBTables.T_Criterio_Atributo.TABLE_NAME +
                " WHERE " + DBTables.T_Criterio_Atributo.ID_VISITA + " = '" + idVisita + "'";
        Cursor c = db.rawQuery(query, null);
        List<CriterioAtributo> list = new ArrayList<>();
        CriterioAtributo criterioAtributo;
        List<String> listTipoCriterio = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    criterioAtributo = new CriterioAtributo();
                    criterioAtributo.setCriterio(c.getString(0));
                    criterioAtributo.setIdCriterio(c.getString(1));
                    list.add(criterioAtributo);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return list;
    }

    /*=========================================== VISITA EVALUACION ============================================*/

    public void Agregar_Visita_Evaluacion(VisitaEvaluacion visitaEvaluacion) {
        TAG = "add visita eval";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita_Evaluacion.ID_VISITA, visitaEvaluacion.getIdVisita());
        cv.put(DBTables.T_Visita_Evaluacion.HORA, visitaEvaluacion.getHora());
        cv.put(DBTables.T_Visita_Evaluacion.SCORE, visitaEvaluacion.getScore());

        long res = db.replace(DBTables.T_Visita_Evaluacion.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<VisitaEvaluacion> Recuperar_Todas_Visita_Evaluacion() {
        TAG = "get visita eval";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita_Evaluacion.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        VisitaEvaluacion visitaEvaluacion = null;
        List<VisitaEvaluacion> visitaEvaluacionList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visitaEvaluacion = new VisitaEvaluacion();
                    visitaEvaluacion.setIdVisita(c.getString(0));
                    visitaEvaluacion.setHora(c.getString(1));
                    visitaEvaluacion.setScore(c.getString(2));
                    visitaEvaluacionList.add(visitaEvaluacion);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return visitaEvaluacionList;
    }

    /*=========================================== VISITA CRITERIO ============================================*/

    public void Agregar_Visita_Criterio(VisitaCriterio visitaCriterio) {
        TAG = "add visita crit";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita_Criterio.ID_VISITA, visitaCriterio.getIdVisita());
        cv.put(DBTables.T_Visita_Criterio.ID_CRITERIO, visitaCriterio.getIdCriterio());
        cv.put(DBTables.T_Visita_Criterio.SCORE, visitaCriterio.getScore());
        cv.put(DBTables.T_Visita_Criterio.VALOR, visitaCriterio.getValor());

        long res = db.replace(DBTables.T_Visita_Criterio.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<VisitaCriterio> Recuperar_Todas_Visita_Criterio() {
        TAG = "get visita crit";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita_Criterio.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        VisitaCriterio visitaCriterio = null;
        List<VisitaCriterio> visitaCriterioList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visitaCriterio = new VisitaCriterio();
                    visitaCriterio.setIdVisita(c.getString(0));
                    visitaCriterio.setIdCriterio(c.getString(1));
                    visitaCriterio.setScore(c.getString(2));
                    visitaCriterio.setValor(c.getString(3));
                    visitaCriterioList.add(visitaCriterio);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return visitaCriterioList;
    }

    /*=========================================== VISITA ATRIBUTO ============================================*/

    public void Agregar_Visita_Atributo(VisitaAtributo visitaAtributo) {
        TAG = "add visita atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DBTables.T_Visita_Atributo.ID_VISITA, visitaAtributo.getIdVisita());
        cv.put(DBTables.T_Visita_Atributo.ID_CRITERIO, visitaAtributo.getIdCriterio());
        cv.put(DBTables.T_Visita_Atributo.ID_ATRIBUTO, visitaAtributo.getIdAtributo());
        cv.put(DBTables.T_Visita_Atributo.SCORE, visitaAtributo.getScore());

        long res = db.replace(DBTables.T_Visita_Atributo.TABLE_NAME, null, cv);
        db.close();
        Log.w(TAG, res + "");
    }

    public List<VisitaAtributo> Recuperar_Todas_Visita_Atributo() {
        TAG = "get visita atrib";
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DBTables.T_Visita_Atributo.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        VisitaAtributo visitaAtributo = null;
        List<VisitaAtributo> visitaAtributoList = new ArrayList<>();
        try {
            if (c.moveToFirst()) {
                do {
                    visitaAtributo = new VisitaAtributo();
                    visitaAtributo.setIdVisita(c.getString(0));
                    visitaAtributo.setIdCriterio(c.getString(1));
                    visitaAtributo.setIdAtributo(c.getString(2));
                    visitaAtributo.setScore(c.getString(3));
                    visitaAtributoList.add(visitaAtributo);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.close();
        db.close();
        return visitaAtributoList;
    }

}
