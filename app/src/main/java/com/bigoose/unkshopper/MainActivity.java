package com.bigoose.unkshopper;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.database.DBTables;
import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.ListaValoracion;
import com.bigoose.unkshopper.model.M_SaveVisita;
import com.bigoose.unkshopper.model.Model;
import com.bigoose.unkshopper.model.TipoCriterio;
import com.bigoose.unkshopper.model.TipoIncidencia;
import com.bigoose.unkshopper.model.Usuario;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.model.VisitaAtributo;
import com.bigoose.unkshopper.model.VisitaAtributoList;
import com.bigoose.unkshopper.model.VisitaCriterio;
import com.bigoose.unkshopper.model.VisitaCriterioList;
import com.bigoose.unkshopper.model.VisitaEvaluacion;
import com.bigoose.unkshopper.model.VisitaEvaluacionList;
import com.bigoose.unkshopper.model.VisitaFoto;
import com.bigoose.unkshopper.model.VisitaFotoList;
import com.bigoose.unkshopper.model.VisitaList;
import com.bigoose.unkshopper.model.VisitaResponse;
import com.bigoose.unkshopper.rest.ApiClient;
import com.bigoose.unkshopper.rest.ApiInterface;
import com.bigoose.unkshopper.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.squareup.leakcanary.LeakCanary;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button btSinc, btLocales, btSalir, btEnviarTodo;
    private DBClasses dbClasses;
    private TextView tvUsuario, tvRuta, tvFecha;
    private Gson g = new Gson();
    private LocationManager manager;
    private LocationListener listener;
    private static final int MIN_DISTANCE = 0; //0 METROS
    private static final int MIN_TIME = 1000 * 7; // 10 SEGUNDOS
    public static List<Double> latitudes = new ArrayList<>();
    public static List<Double> longitudes = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mostrarDatos();
        bestLocation();
    }

    private void init() {
        btSinc = findViewById(R.id.btSincronizarMain);
        btLocales = findViewById(R.id.btLocalesMain);
        btSalir = findViewById(R.id.btSalirMain);
        btEnviarTodo = findViewById(R.id.btEnviarTodoMain);
        tvUsuario = findViewById(R.id.tvUsuarioMain);
        tvRuta = findViewById(R.id.tvRutaMain);
        tvFecha = findViewById(R.id.tvFechaMain);
        btSinc.setOnClickListener(this);
        btLocales.setOnClickListener(this);
        btSalir.setOnClickListener(this);
        btEnviarTodo.setOnClickListener(this);
        Methods.isCameraPermissionGranted(this);
        Methods.isStoragePermissionGranted(this);
        dbClasses = new DBClasses(this);
    }


    @Override
    public void onClick(View view) {
        if (view == btSinc) {
            mostrarDialog();
            obtenerVisitas();
        } else if (view == btLocales) {
            startActivity(new Intent(this, ListaActivity.class));
            //finish();
        } else if (view == btSalir) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (view == btEnviarTodo) {
            mostrarDialogEnviando();
            enviarTodo();
        }
    }

    private void mostrarDialogEnviando() {

        progressDialog = ProgressDialog.show(MainActivity.this, getResources().
                        getString(R.string.enviando_info),
                getResources().getString(R.string.msg_dialog_acceso), false, false);
    }

    private void enviarTodo() {
        boolean pendientes = false;

        //ENVIANDO VISITAS PENDIENTES
        List<Visita> visitaList_rec = dbClasses.Recuperar_Todas_Visitas_Pendientes();
        VisitaList listaVisitas = new VisitaList();
        listaVisitas.setVisita(visitaList_rec);
        if (visitaList_rec.size() > 0) {
            saveVisitasPendientes(listaVisitas);
            pendientes = true;
        }

        List<VisitaEvaluacion> visitaEvaluacionList = dbClasses.Recuperar_Todas_Visita_Evaluacion();
        List<VisitaCriterio> visitaCriterioList = dbClasses.Recuperar_Todas_Visita_Criterio();
        List<VisitaAtributo> visitaAtributoList = dbClasses.Recuperar_Todas_Visita_Atributo();


        //CREANDO LISTA CRITERIO
        VisitaCriterioList listaCriterio = new VisitaCriterioList();
        listaCriterio.setData(visitaCriterioList);

        //CREANDO LISTA ATRIBUTO
        VisitaAtributoList listaAtributo = new VisitaAtributoList();
        listaAtributo.setData(visitaAtributoList);

        //CREANDO LISTA EVALUACION
        VisitaEvaluacionList listaEvaluacion = new VisitaEvaluacionList();
        listaEvaluacion.setData(visitaEvaluacionList);
        if (visitaEvaluacionList.size() > 0) {
            saveVisitaEvaluacion(listaEvaluacion, listaCriterio, listaAtributo);
            pendientes = true;
        }


        //ENVIANDO FOTOS PENDIENTES
        List<VisitaFoto> listaFotos = dbClasses.Recuperar_Todas_Visita_Foto();
        for (int i = 0; i < listaFotos.size(); i++) {
            byte[] foto_rec = listaFotos.get(i).getFotoByte();
            String foto = Base64.encodeToString(foto_rec, Base64.DEFAULT);
            listaFotos.get(i).setFoto(foto);
            listaFotos.get(i).setFotoByte(null);
        }
        VisitaFotoList visitaFotoList = new VisitaFotoList();
        visitaFotoList.setVisita_fotos(listaFotos);
        if (listaFotos.size() > 0) {
            saveVisitaFoto(visitaFotoList);
            pendientes = true;
        }


        //ENVIANDO INCIDENCIAS PENDIENTES
        List<VisitaFoto> listaIncid = dbClasses.Recuperar_Todas_Visita_Incidencia();
        for (int i = 0; i < listaIncid.size(); i++) {
            byte[] foto_rec = listaIncid.get(i).getFotoByte();
            String foto = Base64.encodeToString(foto_rec, Base64.DEFAULT);
            listaIncid.get(i).setFoto(foto);
            listaIncid.get(i).setFotoByte(null);
        }
        VisitaFotoList visitaIncidenciaList = new VisitaFotoList();
        visitaIncidenciaList.setVisita_fotos(listaIncid);
        if (listaIncid.size() > 0) {
            saveVisitaIncidencia(visitaIncidenciaList);
            pendientes = true;
        }

        if (pendientes == false) {
            progressDialog.dismiss();
            Methods.dialogSimple(this, getResources().getString(R.string.atencion),
                    getResources().getString(R.string.sin_pendientes));
        }
    }

    private void saveVisitasPendientes(final VisitaList lista) {

        Log.e("visita pend a env", g.toJson(lista));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisita(lista);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita").getAsString();
                    Log.e("estado visita", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {
                        //Methods.showToast(CamaraActivity.this, R.string.marcacion_correcta);
                        for (int i = 0; i < lista.getVisita().size(); i++) {
                            String idVisita = lista.getVisita().get(i).getIdVisita();
                            Log.e("id vis a  camb", idVisita);
                            dbClasses.Cambiar_Estado_Visita(idVisita, 0);
                        }

                    } else {
                        //Methods.showToast(CamaraActivity.this, R.string.no_se_envio);

                    }
                    progressDialog.dismiss();

                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void saveVisitaFoto(VisitaFotoList visitaFotoList) {

        Log.e("visita foto a env", g.toJson(visitaFotoList));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaFoto(visitaFotoList);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita_fotos").getAsString();
                    Log.e("estado visita_foto", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {
                        Methods.showToast(MainActivity.this, R.string.envio_correcto);

                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Foto.TABLE_NAME);
                    } else {

                        //Methods.showToast(MainActivity.this, R.string.no_se_envio);
                    }
                    progressDialog.dismiss();

                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                //Methods.showToast(CamaraActivity.this, R.string.no_conecta);

            }
        });
    }

    private void saveVisitaIncidencia(VisitaFotoList visitaIncidencia) {

        Log.e("visita inc a env", g.toJson(visitaIncidencia));
        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaInc(visitaIncidencia);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita_inc").getAsString();
                    Log.e("estado visita_inc", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {
                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Incidencia.TABLE_NAME);
                        Methods.showToast(MainActivity.this, R.string.envio_correcto);
                    } else {
                    }
                    progressDialog.dismiss();
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();

                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void saveVisitaEvaluacion(final VisitaEvaluacionList listaEvaluacion, final VisitaCriterioList listaCriterio,
                                      final VisitaAtributoList listaAtributo) {

        Log.e("vis eval a env", g.toJson(listaEvaluacion));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaEvaluacion(listaEvaluacion);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita_eval").getAsString();
                    Log.e("estado visita ev", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {

                        try {
                            saveVisitaCriterio(listaCriterio, listaAtributo);
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Methods.showToast(MainActivity.this, R.string.ocurrio_error);
                        }

                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(MainActivity.this, R.string.no_se_envio);
                    }

                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void saveVisitaCriterio(final VisitaCriterioList listaCriterio,
                                    final VisitaAtributoList listaAtributo) {

        Log.e("vis cri a env", g.toJson(listaCriterio));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaCriterio(listaCriterio);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita_cri").getAsString();
                    Log.e("estado visita cri", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {
                        //Methods.showToast(EvaluarActivity.this, R.string.envio_correcto);
                        Log.e("envio crit", "correcto");

                        try {
                            saveVisitaAtributo(listaAtributo);

                        } catch (Exception e) {
                            Methods.showToast(MainActivity.this, R.string.ocurrio_error);
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(MainActivity.this, R.string.no_se_envio);
                    }


                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void saveVisitaAtributo(VisitaAtributoList list) {

        Log.e("vis atrib a env", g.toJson(list));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaAtributo(list);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String estado_visita = jsonObject.get("estado_visita_atri").getAsString();
                    Log.e("estado visita atri", estado_visita);
                    if (estado_visita.equalsIgnoreCase("true")) {
                        progressDialog.dismiss();
                        Methods.showToast(MainActivity.this, R.string.envio_correcto);
                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Evaluacion.TABLE_NAME);
                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Criterio.TABLE_NAME);
                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Atributo.TABLE_NAME);
                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(MainActivity.this, R.string.no_se_envio);
                    }
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void mostrarDatos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);

        Usuario usuario_rec = dbClasses.Recuperar_Usuario();
        idUsuario = usuario_rec.getIdUsuario();
        Log.e("usuario rec", g.toJson(usuario_rec));
        if (usuario_rec.getNombre_usuario() != null) {
            tvUsuario.setText(usuario_rec.getNombre_usuario() + "");
            tvRuta.setText(usuario_rec.getRegion_3());
        }

        tvFecha.setText(fecha);
    }

    private void bestLocation() {

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String providerFine = manager.getBestProvider(criteria, true);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String providerCoarse = manager.getBestProvider(criteria, true);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.w("COORDENADAS -----> ", "LONGITUD: " + location.getLongitude() + " LATITUD: " + location.getLatitude());
                if (location != null) {
                    latitudes.add(location.getLatitude());
                    longitudes.add(location.getLongitude());
                    Log.e("latitudes size", latitudes.size() + "");
                    Log.e("longitudes size", longitudes.size() + "");

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        if (providerCoarse != null) {
            manager.requestLocationUpdates(providerCoarse, MIN_TIME, MIN_DISTANCE, listener);
        }
        if (providerFine != null) {
            manager.requestLocationUpdates(providerFine, MIN_TIME, MIN_DISTANCE, listener);
        }
    }

    private void mostrarDialog() {
        progressDialog = ProgressDialog.show(MainActivity.this, getResources().
                        getString(R.string.sincronizando),
                getResources().getString(R.string.msg_dialog_acceso), false, false);
    }

    private void obtenerVisitas() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Visita>> call = service.obtenerVisitas(idUsuario);
        call.enqueue(new Callback<List<Visita>>() {
            @Override
            public void onResponse(Call<List<Visita>> call, Response<List<Visita>> response) {

                if (response.isSuccessful()) {

                    List<Visita> visitaList = response.body();
                    Log.e("visita", g.toJson(visitaList));
                    Log.e("visita size", visitaList.size() + "");

                    dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita.TABLE_NAME);
                    for (int i = 0; i < visitaList.size(); i++) {
                        dbClasses.Agregar_Visita(visitaList.get(i));
                    }

                    obtenerTipoIncidencias();
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Visita>> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void obtenerTipoIncidencias() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<TipoIncidencia>> call = service.obtenerTipoIncidencias();
        call.enqueue(new Callback<List<TipoIncidencia>>() {
            @Override
            public void onResponse(Call<List<TipoIncidencia>> call, Response<List<TipoIncidencia>> response) {

                if (response.isSuccessful()) {

                    List<TipoIncidencia> tipoIncidenciaList = response.body();
                    Log.e("tipo inc", g.toJson(tipoIncidenciaList));
                    Log.e("tipo inc size", tipoIncidenciaList.size() + "");

                    dbClasses.Borrar_Tabla_Completa(DBTables.T_Tipo_Incidencia.TABLE_NAME);
                    for (int i = 0; i < tipoIncidenciaList.size(); i++) {
                        dbClasses.Agregar_Tipo_Incidencia(tipoIncidenciaList.get(i));
                    }

                    obtenerTipoCriterio();
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<TipoIncidencia>> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void obtenerTipoCriterio() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<TipoCriterio>> call = service.obtenerTipoCriterio();
        call.enqueue(new Callback<List<TipoCriterio>>() {
            @Override
            public void onResponse(Call<List<TipoCriterio>> call, Response<List<TipoCriterio>> response) {

                if (response.isSuccessful()) {

                    List<TipoCriterio> tipoCriterioList = response.body();
                    Log.e("tipo cri", g.toJson(tipoCriterioList));
                    Log.e("tipo cri size", tipoCriterioList.size() + "");

                    dbClasses.Borrar_Tabla_Completa(DBTables.T_Tipo_Criterio.TABLE_NAME);
                    for (int i = 0; i < tipoCriterioList.size(); i++) {
                        dbClasses.Agregar_Tipo_Criterio(tipoCriterioList.get(i));
                    }

                    obtenerCriteriosAtributos();
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<TipoCriterio>> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

    private void obtenerCriteriosAtributos() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<CriterioAtributo>> call = service.obtenerCriteriosAtributos(idUsuario);
        call.enqueue(new Callback<List<CriterioAtributo>>() {
            @Override
            public void onResponse(Call<List<CriterioAtributo>> call, Response<List<CriterioAtributo>> response) {

                if (response.isSuccessful()) {

                    List<CriterioAtributo> criterioAtributoList = response.body();
                    Log.e("crit atrib", g.toJson(criterioAtributoList));
                    Log.e("crit atrib size", criterioAtributoList.size() + "");

                    dbClasses.Borrar_Tabla_Completa(DBTables.T_Criterio_Atributo.TABLE_NAME);
                    for (int i = 0; i < criterioAtributoList.size(); i++) {
                        dbClasses.Agregar_Criterio_Atributo(criterioAtributoList.get(i));
                    }
                    progressDialog.dismiss();
                    Methods.showToast(MainActivity.this, R.string.sincronizacion_correcta);
                } else {
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                    }
                    progressDialog.dismiss();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<CriterioAtributo>> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(MainActivity.this, R.string.no_conecta);
            }
        });
    }

}
