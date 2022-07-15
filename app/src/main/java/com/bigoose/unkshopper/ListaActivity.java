package com.bigoose.unkshopper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigoose.unkshopper.adapters.AdpLocales;
import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.model.Local;
import com.bigoose.unkshopper.model.Usuario;
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

public class ListaActivity extends BaseActivity implements AdpLocales.ItemClickListener {
    private RecyclerView rvLocales;
    private AdpLocales adpLocales;
    private EditText etBuscar;
    private DBClasses dbClasses;
    private String nombre_local, idVisita;
    private TextView tvRuta;
    private MainActivity main;
    private static final String KEY_TIPO = "tipo";
    private static final String INGRESO = "ingreso";
    private double latitud = 0.0, longitud = 0.0;

    Gson g = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);


        init();
        mostrarDatos();
        FiltrarLocales();

    }

    private void init() {

        dbClasses = new DBClasses(this);
        rvLocales = findViewById(R.id.rvLocales);
        etBuscar = findViewById(R.id.etBuscarLista);
        tvRuta = findViewById(R.id.tvRutaLista);
        Methods.isCameraPermissionGranted(this);
        Methods.isStoragePermissionGranted(this);
    }

    private void mostrarDatos() {
        Usuario usuario_rec = dbClasses.Recuperar_Usuario();
        tvRuta.setText(usuario_rec.getRegion_3());
        List<Visita> visitaList = dbClasses.Recuperar_Todas_Visitas();
        Log.e("visita lis rec", g.toJson(visitaList));
        adpLocales = new AdpLocales(visitaList, this, R.layout.item_local);
        adpLocales.setClickListener(this);
        rvLocales.setLayoutManager(new NpaGridLayoutManager(this, 1));
        rvLocales.setAdapter(adpLocales);
    }

    private void FiltrarLocales() {

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (adpLocales != null) {
                    try {
                        rvLocales.getRecycledViewPool().clear();
                        adpLocales.getFilter().filter(charSequence.toString());

                    } catch (Exception e) {
                        Log.e("Error", "Recycler!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void OnClick(View view, int position) {
        nombre_local = adpLocales.getList().get(position).getNombre_local();
        idVisita = adpLocales.getList().get(position).getIdVisita();
        String horaSalida=adpLocales.getList().get(position).getHoraFin();
        if(horaSalida==null||horaSalida.equals("")||horaSalida.equals("00:00:00")){
            showDialog("¿Desea iniciar la marca del ingreso al " + nombre_local + "?");
        }
        else{
            Methods.dialogSimple(this,getResources().getString(R.string.atencion),
                    "Ya se marcó la salida de esta visita");
        }
    }

    public void volver(View view) {

        finish();
    }

    private static class NpaGridLayoutManager extends GridLayoutManager {

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public NpaGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public NpaGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public NpaGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }
    }

    public void showDialog(String msj) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
        final TextView textViev = alertLayout.findViewById(R.id.tvMsgDialog);
        final TextView tvReportar = alertLayout.findViewById(R.id.tvReportarDialog);
        final Button btSI = alertLayout.findViewById(R.id.btSiDialog);
        final Button btNO = alertLayout.findViewById(R.id.btNoDialog);
        textViev.setText(msj);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        List<Double> latitudes = MainActivity.latitudes;
        List<Double> longitudes = MainActivity.longitudes;


        if (latitudes.size() > 0 && longitudes.size() > 0) {
            latitud = latitudes.get(latitudes.size() - 1);
            longitud = longitudes.get(longitudes.size() - 1);
        }


        tvReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GUARDANDO INICIO DE VISITA
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                String hora = dateFormat.format(date);

                dbClasses.Actualizar_Visita(idVisita, hora, latitud, longitud, INGRESO);
                startActivity(new Intent(ListaActivity.this, CamaraActivity.class).
                        putExtra(Consts.KEY_TIPO_FOTO, Consts.INCIDENCIA).putExtra(Consts.KEY_NOMBRE_LOCAL, nombre_local)
                        .putExtra(Consts.KEY_ID_VISITA, idVisita));
                dialog.dismiss();
            }
        });

        btSI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GUARDANDO INICIO DE VISITA
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                String hora = dateFormat.format(date);

                dbClasses.Actualizar_Visita(idVisita, hora, latitud, longitud, INGRESO);
                saveVisitaInicio();
                startActivity(new Intent(ListaActivity.this, MenuActivity.class).
                        putExtra(Consts.KEY_NOMBRE_LOCAL, nombre_local).putExtra(Consts.KEY_ID_VISITA, idVisita));
                //Methods.showToast(ListaActivity.this, R.string.marcacion_correcta);
                dialog.dismiss();
            }
        });

        btNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void saveVisitaInicio() {

        Visita visita = dbClasses.Recuperar_Visita(idVisita);

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
}
