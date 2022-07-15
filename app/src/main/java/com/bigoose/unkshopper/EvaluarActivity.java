package com.bigoose.unkshopper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bigoose.unkshopper.adapters.AdpValoracion;
import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.ListaValoracion;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.model.VisitaAtributo;
import com.bigoose.unkshopper.model.VisitaAtributoList;
import com.bigoose.unkshopper.model.VisitaCriterio;
import com.bigoose.unkshopper.model.VisitaCriterioList;
import com.bigoose.unkshopper.model.VisitaEvaluacion;
import com.bigoose.unkshopper.model.VisitaEvaluacionList;
import com.bigoose.unkshopper.model.VisitaList;
import com.bigoose.unkshopper.rest.ApiClient;
import com.bigoose.unkshopper.rest.ApiInterface;
import com.bigoose.unkshopper.utils.Consts;
import com.bigoose.unkshopper.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluarActivity extends BaseActivity implements View.OnClickListener {

    private RatingBar rbTotal;
    private RecyclerView rvPuntaje;
    private AdpValoracion adpValoracion;
    private TextView tvCriterio, tvLocal;
    private Button btSgte, btEnviar;
    private EditText etComentario;
    private Gson g = new Gson();
    private int indice = 0;
    private List<ListaValoracion> listaTotal;
    private DBClasses dbClasses;
    private List<CriterioAtributo> listTipoCriterio;
    private String idVisita;
    private double promedioFinal, promedioFinalPunt;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluar);

        init();
        listaTotal = new ArrayList<>();
        llenarArray();
        mostrarDatos();
    }

    private void init() {
        rbTotal = findViewById(R.id.rbTotal);
        rbTotal.setIsIndicator(true);
        tvCriterio = findViewById(R.id.tvCriterioEvaluar);
        tvLocal = findViewById(R.id.tvLocalEvaluar);
        rvPuntaje = findViewById(R.id.rvPuntaje);
        btSgte = findViewById(R.id.btSgteEvaluar);
        btSgte.setOnClickListener(this);
        btEnviar = findViewById(R.id.bteEnviarEvaluar);
        btEnviar.setOnClickListener(this);
        etComentario = findViewById(R.id.etComentarioEvaluar);
        dbClasses = new DBClasses(this);
    }

    private void llenarArray() {
        String idVisita = getIntent().getStringExtra(Consts.KEY_ID_VISITA);
        Log.e("idVisita en Eval", idVisita);
        listTipoCriterio = dbClasses.Obtener_Tipos_Criterio_Atributo(idVisita);
        Log.e("list tipo crit", g.toJson(listTipoCriterio));
        for (int i = 0; i < listTipoCriterio.size(); i++) {
            double total = 0, promedio = 0;
            List<CriterioAtributo> listaCriterio = dbClasses.Recuperar_Lista_Valoracion(listTipoCriterio.get(i).getCriterio(), idVisita + "");
            for (int j = 0; j < listaCriterio.size(); j++) {
                total += Double.parseDouble(listaCriterio.get(j).getValor());
            }

            promedio = total / listaCriterio.size();
            Log.e("total:", total + " promedio:" + promedio);
            listaTotal.add(new ListaValoracion(listTipoCriterio.get(i).getCriterio(), listaCriterio, promedio,
                    listTipoCriterio.get(i).getIdCriterio(), 20));
        }
        Log.e("lista total", g.toJson(listaTotal));
    }

    public void mostrarDatos() {
        idVisita = getIntent().getStringExtra(Consts.KEY_ID_VISITA);
        String nombre_local = getIntent().getStringExtra(Consts.KEY_NOMBRE_LOCAL);
        tvLocal.setText(nombre_local + "");
        if (listaTotal.size() > 0) {
            Log.e("indice: ", indice + "");
            tvCriterio.setText(listaTotal.get(indice).getTitulo() + "");
            adpValoracion = new AdpValoracion(this, listaTotal, rbTotal, indice);
            rvPuntaje.setLayoutManager(new LinearLayoutManager(this));
            rvPuntaje.setAdapter(adpValoracion);
            indice++;
            Log.e("nueva lista total", g.toJson(listaTotal));
        } else {
            dialogSinCriterios();
        }
    }

    public void volver(View view) {
        finish();
    }

    private void mostrarDialog() {
        progressDialog = ProgressDialog.show(EvaluarActivity.this, getResources().
                        getString(R.string.enviando_encuesta),
                getResources().getString(R.string.msg_dialog_acceso), false, false);
    }

    @Override
    public void onClick(View view) {
        if (view == btSgte) {
            if (indice < listaTotal.size()) {
                mostrarDatos();
            } else verResultadoFinal();
        } else if (view == btEnviar) {
            mostrarDialog();
            saveVisitaInicio();
            saveVisitaEvaluacion();
        }
    }


    private void verResultadoFinal() {
        etComentario.setVisibility(View.VISIBLE);
        btEnviar.setVisibility(View.VISIBLE);
        double totalFinal = 0, totalFinalPunt = 0;
        for (int i = 0; i < listaTotal.size(); i++) {
            double promedio = listaTotal.get(i).getPromedio();
            double promedioPunt = listaTotal.get(i).getPromedioPunt();
            totalFinal += promedio;
            totalFinalPunt += promedioPunt;
            Log.e("promedio", i + " = " + listaTotal.get(i).getPromedio() * 4);
            Log.e("promedio punt", i + " = " + listaTotal.get(i).getPromedioPunt());

        }
        promedioFinal = totalFinal / listaTotal.size();
        promedioFinalPunt = totalFinalPunt / listaTotal.size();
        Log.e("promedio final", promedioFinal + "");
        Log.e("promedio final puny", promedioFinalPunt + "");
        rbTotal.setRating((float) promedioFinal);
        btSgte.setEnabled(false);
        tvCriterio.setVisibility(View.GONE);
        rvPuntaje.setVisibility(View.GONE);
    }

    private void saveVisitaInicio() {

        Visita visita = dbClasses.Recuperar_Visita(idVisita);
        visita.setComentario(etComentario.getText().toString());
        List<Visita> visitaList = new ArrayList<>();
        visitaList.add(visita);
        VisitaList lista = new VisitaList();
        lista.setVisita(visitaList);

        Log.e("visita inicio", g.toJson(lista));

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
                    } else {
                        //Methods.showToast(CamaraActivity.this, R.string.no_se_envio);
                        dbClasses.Cambiar_Estado_Visita(idVisita,1);
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
                    dbClasses.Cambiar_Estado_Visita(idVisita,1);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                //progressDialog.dismiss();
                //Methods.showToast(CamaraActivity.this, R.string.no_conecta);
                dbClasses.Cambiar_Estado_Visita(idVisita,1);
            }
        });
    }

    private void saveVisitaEvaluacion() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String hora = dateFormat.format(date);
        List<VisitaEvaluacion> visitaEvaluacionList= new ArrayList<>();
        visitaEvaluacionList.add(new VisitaEvaluacion(idVisita,hora,promedioFinalPunt+""));
        VisitaEvaluacionList listaEvaluacion= new VisitaEvaluacionList();
        listaEvaluacion.setData(visitaEvaluacionList);

        Log.e("vis eval a env", g.toJson(listaEvaluacion));

        //GUARDANDO EN PENDIENTE
        final VisitaEvaluacion visitaEvalPend=new VisitaEvaluacion();
        visitaEvalPend.setIdVisita(idVisita);
        visitaEvalPend.setHora(hora);
        visitaEvalPend.setScore(promedioFinalPunt+"");

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
                        //Methods.showToast(EvaluarActivity.this, R.string.envio_correcto);
                        List<VisitaCriterio> visitaCriterioList = new ArrayList<>();
                        try {
                            for (int i = 0; i < listaTotal.size(); i++) {
                                double promedioPunt = listaTotal.get(i).getPromedioPunt();
                                String idCriterio = listaTotal.get(i).getIdCriterio();
                                VisitaCriterio visitaCriterio = new VisitaCriterio(idVisita, idCriterio, promedioPunt + "", 4 + "");
                                visitaCriterioList.add(visitaCriterio);
                            }
                            VisitaCriterioList listaCriterio= new VisitaCriterioList();
                            listaCriterio.setData(visitaCriterioList);
                            saveVisitaCriterio(listaCriterio);
                        } catch (Exception e) {
                            progressDialog.dismiss();
                            Methods.showToast(EvaluarActivity.this, R.string.ocurrio_error);
                            dbClasses.Agregar_Visita_Evaluacion(visitaEvalPend);
                            saveEvaluacionesPendiente();
                        }

                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(EvaluarActivity.this, R.string.no_se_envio);
                        dbClasses.Agregar_Visita_Evaluacion(visitaEvalPend);
                        saveEvaluacionesPendiente();
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
                    dbClasses.Agregar_Visita_Evaluacion(visitaEvalPend);
                    saveEvaluacionesPendiente();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(EvaluarActivity.this, R.string.no_conecta);
                dbClasses.Agregar_Visita_Evaluacion(visitaEvalPend);
                saveEvaluacionesPendiente();
            }
        });
    }

    private void saveVisitaCriterio(final VisitaCriterioList list) {

        Log.e("vis cri a env", g.toJson(list));

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<JsonObject> call = service.saveVisitaCriterio(list);
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
                            List<VisitaAtributo> visitaAtributoList = new ArrayList<>();
                            for (int i = 0; i < listaTotal.size(); i++) {
                                String idCriterio = listaTotal.get(i).getIdCriterio();
                                List<CriterioAtributo> listCri = listaTotal.get(i).getListaVal();

                                for (int j = 0; j < listCri.size(); j++) {
                                    String idAtributo = listCri.get(j).getIdAtributo();
                                    double score = listCri.get(j).getPuntaje();
                                    VisitaAtributo visitaAtributo= new VisitaAtributo(idVisita,idCriterio,idAtributo,score+"");
                                    visitaAtributoList.add(visitaAtributo);
                                }
                            }
                            VisitaAtributoList lista= new VisitaAtributoList();
                            lista.setData(visitaAtributoList);
                            saveVisitaAtributo(lista);

                        } catch (Exception e) {
                            Methods.showToast(EvaluarActivity.this, R.string.ocurrio_error);
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(EvaluarActivity.this, R.string.no_se_envio);
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
                Methods.showToast(EvaluarActivity.this, R.string.no_conecta);
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
                        Methods.showToast(EvaluarActivity.this, R.string.envio_correcto);
                    } else {
                        progressDialog.dismiss();
                        Methods.showToast(EvaluarActivity.this, R.string.no_se_envio);
                    }
                    finish();
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
                Methods.showToast(EvaluarActivity.this, R.string.no_conecta);
            }
        });
    }

    private void dialogSinCriterios() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.atencion));
        dialog.setMessage(getResources().getString(R.string.sin_criterios));
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton(R.string.confirmar, null);
        dialog.setCancelable(false);
        final AlertDialog alertDialog = dialog.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btConfirmar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

   private void saveEvaluacionesPendiente(){

       for (int i = 0; i < listaTotal.size(); i++) {
           String idCriterio = listaTotal.get(i).getIdCriterio();
           List<CriterioAtributo> listCri = listaTotal.get(i).getListaVal();
           double promedioPunt = listaTotal.get(i).getPromedioPunt();
           VisitaCriterio visitaCriterio = new VisitaCriterio(idVisita, idCriterio, promedioPunt + "", 4 + "");
           dbClasses.Agregar_Visita_Criterio(visitaCriterio);

           for (int j = 0; j < listCri.size(); j++) {
               String idAtributo = listCri.get(j).getIdAtributo();
               double score = listCri.get(j).getPuntaje();
               VisitaAtributo visitaAtributo= new VisitaAtributo(idVisita,idCriterio,idAtributo,score+"");
               dbClasses.Agregar_Visita_Atributo(visitaAtributo);
           }
       }
       dialogSavePendientes();
   }

    private void dialogSavePendientes() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.atencion));
        dialog.setMessage(getResources().getString(R.string.save_pendiente));
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton(R.string.confirmar, null);
        dialog.setCancelable(false);
        final AlertDialog alertDialog = dialog.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btConfirmar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }
}
