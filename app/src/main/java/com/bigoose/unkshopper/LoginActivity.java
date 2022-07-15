package com.bigoose.unkshopper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.database.DBTables;
import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.TipoCriterio;
import com.bigoose.unkshopper.model.TipoIncidencia;
import com.bigoose.unkshopper.model.Usuario;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.rest.ApiClient;
import com.bigoose.unkshopper.rest.ApiInterface;
import com.bigoose.unkshopper.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etClave;
    private static final String APP_VERSION = "0.0.1";
    private Gson g = new Gson();
    private DBClasses dbClasses;
    private ProgressDialog progressDialog;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        init();
        Methods.isLocationPemissionGranted(this);
        dbClasses = new DBClasses(this);
    }

    public void ingresar(View view) {
        if (Methods.isLocationPemissionGranted(this)) {
            mostrarDialog();
            validarVersion();
        } else {
            Methods.showToast(this, R.string.habilitar_permiso);
        }
    }

    private void init() {

        etUsuario = findViewById(R.id.etUsuarioLogin);
        etClave = findViewById(R.id.etClaveLogin);
    }

    private void validarVersion() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);

        Call<JsonObject> call = service.validarVersion(APP_VERSION);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                    JsonObject jsonObject = response.body();
                    String resultado = jsonObject.get("version").getAsString();
                    Log.e("json object", jsonObject + "");
                    Log.e("resultado", resultado + "");

                    if (resultado.equalsIgnoreCase("TRUE")) {
                        validarUsuario();

                    } else {
                        Methods.showToast(getApplicationContext(), R.string.actualizar_version);
                        progressDialog.dismiss();
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
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
            }
        });
    }

    private void validarUsuario() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        String user = etUsuario.getText().toString();
        String pwd = etClave.getText().toString();
        //String user = "0000000006";
        //String pwd = "1234";
        Call<Usuario> call = service.validarUsuario(user, pwd);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                if (response.isSuccessful()) {

                    usuario = response.body();
                    Log.e("usuario", g.toJson(usuario));

                    if (usuario.getEstadoLogin() != null && usuario.getEstadoLogin().equalsIgnoreCase("true")) {
                        dbClasses.Borrar_Tabla_Completa(DBTables.T_Usuario.TABLE_NAME);
                        dbClasses.Agregar_Usuario(usuario);
                        obtenerVisitas();
                    } else {
                        Methods.showToast(getApplicationContext(), R.string.datos_incorrectos);
                        progressDialog.dismiss();
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
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
            }
        });
    }

    private void mostrarDialog() {
        progressDialog = ProgressDialog.show(LoginActivity.this, getResources().
                        getString(R.string.title_dialog_acceso),
                getResources().getString(R.string.msg_dialog_acceso), false, false);
    }

    private void obtenerVisitas() {
        Log.e("id usuario",usuario.getIdUsuario());
        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<Visita>> call = service.obtenerVisitas(usuario.getIdUsuario());
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
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
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
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
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
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
            }
        });
    }

    private void obtenerCriteriosAtributos() {

        ApiInterface service = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<List<CriterioAtributo>> call = service.obtenerCriteriosAtributos(usuario.getIdUsuario());
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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

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
            public void onFailure(Call<List<CriterioAtributo>> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                Methods.showToast(LoginActivity.this, R.string.no_conecta);
            }
        });
    }

    public void limpiar(View view) {
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Tipo_Incidencia.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Tipo_Criterio.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Usuario.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Criterio_Atributo.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Foto.TABLE_NAME);
        dbClasses.Borrar_Tabla_Completa(DBTables.T_Visita_Foto.TABLE_NAME);
        dialogBorrandoDatos();
    }

    private void dialogBorrandoDatos() {
        Methods.dialogSimple(this, getResources().getString(R.string.title_dialog_borrar),
                getResources().getString(R.string.msg_dialog_borrar));
    }
}
