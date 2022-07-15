package com.bigoose.unkshopper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.model.Visita;
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

public class MenuActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLocal;
    private String nombre_local;
    private String idVisita;
    private DBClasses dbClasses;
    private static final String SALIDA = "salida";
    private Button btEvaluar, btFotos, btSalida, btEnviar, btCancelar;
    private Gson g = new Gson();
    private double latitud = 0.0, longitud = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
        nombre_local = getIntent().getStringExtra(Consts.KEY_NOMBRE_LOCAL);
        tvLocal.setText(nombre_local + "");
        idVisita = getIntent().getStringExtra(Consts.KEY_ID_VISITA + "");
    }

    private void init() {
        dbClasses = new DBClasses(this);
        tvLocal = findViewById(R.id.tvLocalMenu);
        btEvaluar = findViewById(R.id.btEvaluarMenu);
        btFotos = findViewById(R.id.btFotosMenu);
        btSalida = findViewById(R.id.btMarcarSalidaMenu);
        btEnviar = findViewById(R.id.btEnviarTodoMenu);
        btCancelar = findViewById(R.id.btCancelarMenu);
        btEvaluar.setOnClickListener(this);
        btFotos.setOnClickListener(this);
        btSalida.setOnClickListener(this);
        btEnviar.setOnClickListener(this);
        btCancelar.setOnClickListener(this);
    }

    private void evaluar() {
        startActivity(new Intent(this, EvaluarActivity.class).
                putExtra(Consts.KEY_ID_VISITA, idVisita).putExtra(Consts.KEY_NOMBRE_LOCAL, nombre_local));
    }

    private void fotos() {
        startActivity(new Intent(this, CamaraActivity.class).putExtra(Consts.KEY_TIPO_FOTO, Consts.FOTO).
                putExtra(Consts.KEY_NOMBRE_LOCAL, nombre_local).putExtra(Consts.KEY_ID_VISITA, idVisita));
    }

    private void marcarSalida() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String hora = dateFormat.format(date);

        List<Double> latitudes = MainActivity.latitudes;
        List<Double> longitudes = MainActivity.longitudes;
        if (latitudes.size() > 0 && longitudes.size() > 0) {
            latitud = latitudes.get(latitudes.size() - 1);
            longitud = longitudes.get(longitudes.size() - 1);
        }
        dbClasses.Actualizar_Visita(idVisita, hora, latitud, longitud, SALIDA);
        //Methods.showToast(this, R.string.marcacion_correcta);
        btSalida.setEnabled(false);

        recuperarVisita();

    }

    private void recuperarVisita(){
            Visita visita = dbClasses.Recuperar_Visita(idVisita);
        String idVisita=visita.getIdVisita();
        if(idVisita!=null&&!idVisita.equals("")){
            saveVisita(visita);
        }
        Log.e("visita rec", g.toJson(visita));
    }

    private void saveVisita(Visita visita) {


        List<Visita> visitaList= new ArrayList<>();
        visitaList.add(visita);

        VisitaList lista= new VisitaList();
        lista.setVisita(visitaList);

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
                        Methods.showToast(MenuActivity.this, R.string.marcacion_correcta);
                        startActivity(new Intent(MenuActivity.this,ListaActivity.class));
                        finish();
                    } else {
                        dbClasses.Cambiar_Estado_Visita(idVisita,1);
                        dialogSavePendientes();
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
                    dialogSavePendientes();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                dbClasses.Cambiar_Estado_Visita(idVisita,1);
                dialogSavePendientes();
            }
        });
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
                        alertDialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void dialogSalida(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.atencion));
        dialog.setMessage(getResources().getString(R.string.finalizar_visita));
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton(R.string.confirmar, null);
        dialog.setNegativeButton(R.string.cancelar, null);

        final AlertDialog alertDialog = dialog.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btConfirmar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btConfirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      marcarSalida();
                      alertDialog.dismiss();

                    }
                });
            }
        });

        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == btCancelar) {
            startActivity(new Intent(MenuActivity.this,ListaActivity.class));
            finish();
        } else if (view == btFotos) {
            fotos();
        } else if (view == btEvaluar) {
            evaluar();
        } else if (view == btSalida) {
            dialogSalida();
        } else if (view == btEnviar) {
            //recuperarVisita();
        }
    }
}
