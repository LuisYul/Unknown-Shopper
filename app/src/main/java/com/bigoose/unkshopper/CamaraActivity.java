package com.bigoose.unkshopper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigoose.unkshopper.adapters.AdpVisitaFoto;
import com.bigoose.unkshopper.database.DBClasses;
import com.bigoose.unkshopper.model.TipoCriterio;
import com.bigoose.unkshopper.model.TipoIncidencia;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.model.VisitaFoto;
import com.bigoose.unkshopper.model.VisitaFotoList;
import com.bigoose.unkshopper.model.VisitaList;
import com.bigoose.unkshopper.rest.ApiClient;
import com.bigoose.unkshopper.rest.ApiInterface;
import com.bigoose.unkshopper.utils.App;
import com.bigoose.unkshopper.utils.Consts;
import com.bigoose.unkshopper.utils.Methods;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CamaraActivity extends BaseActivity implements View.OnClickListener {

    private String tipo_foto, nombre_local, idVisita;
    private TextView tvTipoFoto, tvLocal;
    private ImageView ivAdd;
    private Button btVolver, btTomarFoto, btEnviar;
    private String nombre_foto;
    private Bitmap bitmap, nuevo;
    private RecyclerView rvFotos;
    private List<VisitaFoto> listaFotos;
    private ArrayAdapter<String> adapter;
    private DBClasses dbClasses;
    private Gson g = new Gson();
    private ProgressDialog progressDialog;
    private static final String SALIDA = "salida";
    private double latitud = 0.0, longitud = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        init();
        dbClasses = new DBClasses(this);
        verDatos();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //llenarSpinner();
        //verLista();
        datosSpinner();
        listaFotos = new ArrayList<>();
        actualizarListaFoto();
    }

    private void init() {
        tvTipoFoto = findViewById(R.id.tvTipoFoto);
        tvLocal = findViewById(R.id.tvLocalFoto);
        ivAdd = findViewById(R.id.ivAddFoto);
        rvFotos = findViewById(R.id.rvFotos);
        btVolver = findViewById(R.id.btVolverCamara);
        btVolver.setOnClickListener(this);
        btTomarFoto = findViewById(R.id.btTomarFoto);
        btTomarFoto.setOnClickListener(this);
        btEnviar = findViewById(R.id.btEnviarCamara);
        btEnviar.setOnClickListener(this);
    }

    private void verDatos() {
        tipo_foto = getIntent().getStringExtra(Consts.KEY_TIPO_FOTO);
        nombre_local = getIntent().getStringExtra(Consts.KEY_NOMBRE_LOCAL);
        idVisita = getIntent().getStringExtra(Consts.KEY_ID_VISITA);
        Log.e("idVisita", idVisita);
        tvLocal.setText(nombre_local + "");
        //tipo_foto = "foto";
        if (tipo_foto.equals(Consts.FOTO)) {
            tvTipoFoto.setText("FOTOS");
            btEnviar.setText("ENVIAR FOTOS");
            btEnviar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_send_black, 0, 0, 0);
        } else {
            tvTipoFoto.setText("INCIDENCIA");
            ivAdd.setVisibility(View.INVISIBLE);
            btEnviar.setText("REPORTAR INCIDENCIA");
            btEnviar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert, 0, 0, 0);
        }
    }

    private void datosSpinner() {
        List<String> data = new ArrayList<>();
        if (tipo_foto.equalsIgnoreCase(Consts.INCIDENCIA)) {
            List<TipoIncidencia> tipoIncidenciaList = dbClasses.Recuperar_Todas_Tipo_Incidencias();
            for (int i = 0; i < tipoIncidenciaList.size(); i++) {
                data.add(tipoIncidenciaList.get(i).getNombre());
            }
        } else {
            List<TipoCriterio> tipoCriterioList = dbClasses.Recuperar_Todas_Tipo_Criterio(idVisita);
            for (int i = 0; i < tipoCriterioList.size(); i++) {
                data.add(tipoCriterioList.get(i).getNombre());
            }
            Log.e("criterios de visita", g.toJson(data));
        }
        adapter = new ArrayAdapter<>(this, R.layout.spinner_item, data);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300) {

            byte[] byte_Rec = App.getInstance().getCapturedPhotoData();
            if (byte_Rec != null) {
                Bitmap bmrec = BitmapFactory.decodeByteArray(byte_Rec, 0, byte_Rec.length);
                Bitmap roted = rotateImage(bmrec, 90);
                Bitmap comp = Methods.getResizedBitmap(roted, 640, 480);
                ByteArrayOutputStream byteArrayOutputStreamObject;
                byteArrayOutputStreamObject = new ByteArrayOutputStream();
                comp.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStreamObject);
                byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

                //enviando nueva lista al adp
                listaFotos.add(new VisitaFoto("", "", byteArrayVar));
                actualizarListaFoto();

                //tipo_foto = "foto";
                if (tipo_foto.equals(Consts.INCIDENCIA)) {
                    btTomarFoto.setVisibility(View.GONE);
                }
                App.getInstance().setCapturedPhotoData(null);
            }
        }
    }

    private void actualizarListaFoto() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFotos.setLayoutManager(linearLayoutManager);
        AdpVisitaFoto adp = new AdpVisitaFoto(this, listaFotos, adapter, this);
        rvFotos.setAdapter(adp);
        int tamaño_lista = listaFotos.size();
        rvFotos.scrollToPosition(tamaño_lista - 1);
        if (tamaño_lista > 0) {
            rvFotos.smoothScrollToPosition(tamaño_lista - 1);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void abrirCamara() {
        Intent intent = new Intent(this, CamTestActivity.class);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onClick(View view) {
        if (view == btVolver) {
            //dbClasses.Borrar_Todas_Visita_Foto();
            finish();
        } else if (view == btTomarFoto) {
            if (Methods.isCameraPermissionGranted(this)) {
                abrirCamara();
            }

        } else if (view == btEnviar) {
            mostrarDialog();
            saveVisitaInicio();
            saveFotos();
            //obtenerDB();
        }
    }

    private void guardarFotoPendientes() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String hora = dateFormat.format(date);

        if (tipo_foto.equalsIgnoreCase(Consts.FOTO)) {
            VisitaFoto visitaFoto;

            for (int i = 0; i < listaFotos.size(); i++) {
                String id_criterio = dbClasses.Recuperar_Id_Criterio(listaFotos.get(i).getIdCriterio(), idVisita);
                String comentario = listaFotos.get(i).getComentario();
                byte[] foto = listaFotos.get(i).getFotoByte();
                visitaFoto = new VisitaFoto();
                visitaFoto.setIdVisita(idVisita);
                visitaFoto.setHora(hora);
                visitaFoto.setIdCriterio(id_criterio);
                visitaFoto.setComentario(comentario);
                visitaFoto.setFotoByte(foto);
                visitaFoto.setIndice(i + "");
                visitaFoto.setTipo("F");
                dbClasses.Agregar_Visita_Foto(visitaFoto);
            }
        } else if (tipo_foto.equalsIgnoreCase(Consts.INCIDENCIA)) {

            VisitaFoto visitaInc;

            List<Double> latitudes = MainActivity.latitudes;
            List<Double> longitudes = MainActivity.longitudes;

            if (latitudes.size() > 0 && longitudes.size() > 0) {
                latitud = latitudes.get(latitudes.size() - 1);
                longitud = longitudes.get(longitudes.size() - 1);
            }

            for (int i = 0; i < listaFotos.size(); i++) {
                String id_tipo = dbClasses.Recuperar_Id_Tipo_Inc(listaFotos.get(i).getIdCriterio());
                String comentario = listaFotos.get(i).getComentario();
                byte[] foto = listaFotos.get(i).getFotoByte();
                visitaInc = new VisitaFoto();
                visitaInc.setIdVisita(idVisita);
                visitaInc.setHora(hora);
                visitaInc.setLatitud(latitud+"");
                visitaInc.setLongitud(longitud+"");
                visitaInc.setIdTipo(id_tipo);
                visitaInc.setComentario(comentario);
                visitaInc.setFotoByte(foto);
                visitaInc.setTipo("I");
                dbClasses.Agregar_Visita_Incidencia(visitaInc);
            }
        }
    }

    private void saveFotos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String hora = dateFormat.format(date);

        //SI ES UNA FOTO NORMAL
        if (tipo_foto.equalsIgnoreCase(Consts.FOTO)) {
            VisitaFoto visitaFoto;
            List<VisitaFoto> visitaFotoList = new ArrayList<>();

            for (int i = 0; i < listaFotos.size(); i++) {
                String id_criterio = dbClasses.Recuperar_Id_Criterio(listaFotos.get(i).getIdCriterio(), idVisita);
                String comentario = listaFotos.get(i).getComentario();
                byte[] foto = listaFotos.get(i).getFotoByte();
                visitaFoto = new VisitaFoto();
                visitaFoto.setIdVisita(idVisita);
                visitaFoto.setHora(hora);
                visitaFoto.setIdCriterio(id_criterio);
                visitaFoto.setComentario(comentario);
                visitaFoto.setFoto(Base64.encodeToString(foto, Base64.DEFAULT));
                visitaFoto.setIndice(i + "");
                visitaFotoList.add(visitaFoto);
            }
            VisitaFotoList listaFotos = new VisitaFotoList();
            listaFotos.setVisita_fotos(visitaFotoList);
            saveVisitaFoto(listaFotos);

            //SI ES UNA INCIDENCIA
        } else if (tipo_foto.equalsIgnoreCase(Consts.INCIDENCIA)) {

            VisitaFoto visitaIncidencia;
            List<VisitaFoto> visitaIncidenciaList = new ArrayList<>();

            for (int i = 0; i < listaFotos.size(); i++) {
                String id_tipo = dbClasses.Recuperar_Id_Tipo_Inc(listaFotos.get(i).getIdCriterio());
                String comentario = listaFotos.get(i).getComentario();
                byte[] foto = listaFotos.get(i).getFotoByte();
                visitaIncidencia = new VisitaFoto();
                visitaIncidencia.setIdTipo(id_tipo);
                visitaIncidencia.setIdVisita(idVisita);
                visitaIncidencia.setHora(hora);
                visitaIncidencia.setLatitud(latitud+"");
                visitaIncidencia.setLongitud(longitud+"");
                visitaIncidencia.setComentario(comentario);
                visitaIncidencia.setFoto(Base64.encodeToString(foto, Base64.DEFAULT));
                visitaIncidenciaList.add(visitaIncidencia);
            }
            VisitaFotoList listaIncidencias = new VisitaFotoList();
            listaIncidencias.setVisita_fotos(visitaIncidenciaList);
            saveVisitaIncidencia(listaIncidencias);
        }

        Log.e("lista foto final size", listaFotos.size() + "");
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
                        progressDialog.dismiss();
                        Methods.showToast(CamaraActivity.this, R.string.envio_correcto);
                    } else {
                        progressDialog.dismiss();
                        guardarFotoPendientes();
                        dialogSavePendientes();
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
                    guardarFotoPendientes();
                    dialogSavePendientes();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                guardarFotoPendientes();
                dialogSavePendientes();
            }
        });
    }

    private void saveVisitaIncidencia(VisitaFotoList visitaIncidencia) {


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
                        progressDialog.dismiss();
                        Methods.showToast(CamaraActivity.this, R.string.envio_correcto);
                    } else {
                        Methods.showToast(CamaraActivity.this, R.string.no_se_envio);
                        progressDialog.dismiss();
                        guardarFotoPendientes();
                        dialogSavePendientes();
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
                    guardarFotoPendientes();
                    dialogSavePendientes();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("on", "failure " + t.toString());
                progressDialog.dismiss();
                guardarFotoPendientes();
                dialogSavePendientes();
            }
        });
    }

    private void obtenerDB() {
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String packageName = getPackageName();
            String sourceDBName = "unk_shopper";
            String targetDBName = "unkshoppbkp";
            if (sd.canWrite()) {
                Date now = new Date();
                String currentDBPath = "data/" + packageName + "/databases/" + sourceDBName;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                String backupDBPath = "DCIM/" + targetDBName + dateFormat.format(now) + ".db";

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                Log.i("backup", "backupDB=" + backupDB.getAbsolutePath());
                Log.i("backup", "sourceDB=" + currentDB.getAbsolutePath());

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();

                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(this, "Descarga Correcta", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Log.i("Backup", e.toString());
        }
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

    private void mostrarDialog() {
        progressDialog = ProgressDialog.show(CamaraActivity.this, getResources().
                        getString(R.string.enviando_info),
                getResources().getString(R.string.msg_dialog_acceso), false, false);
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
