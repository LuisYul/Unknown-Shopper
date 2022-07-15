package com.bigoose.unkshopper.rest;


import android.util.Base64;
import android.util.Log;

import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.M_SaveVisita;
import com.bigoose.unkshopper.model.Model;
import com.bigoose.unkshopper.model.Tabla;
import com.bigoose.unkshopper.model.TipoCriterio;
import com.bigoose.unkshopper.model.TipoIncidencia;
import com.bigoose.unkshopper.model.Usuario;
import com.bigoose.unkshopper.model.Visita;
import com.bigoose.unkshopper.model.VisitaAtributoList;
import com.bigoose.unkshopper.model.VisitaCriterio;
import com.bigoose.unkshopper.model.VisitaCriterioList;
import com.bigoose.unkshopper.model.VisitaEvaluacionList;
import com.bigoose.unkshopper.model.VisitaFotoList;
import com.bigoose.unkshopper.model.VisitaList;
import com.bigoose.unkshopper.model.VisitaResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiInterface {

    Gson g = new Gson();

    @POST("version/")
    @FormUrlEncoded
    Call<JsonObject> validarVersion(@Field("version") String version);

    @POST("login/")
    @FormUrlEncoded
    Call<Usuario> validarUsuario(@Field("user") String user, @Field("pwd") String pwd);

    @POST("getVisitas/")
    @FormUrlEncoded
    Call<List<Visita>> obtenerVisitas(@Field("idUsuario") String idUsuario);

    @POST("getTipoIncidencias/")
    Call<List<TipoIncidencia>> obtenerTipoIncidencias();

    @POST("getTipoCriterio/")
    Call<List<TipoCriterio>> obtenerTipoCriterio();

    @POST("getCriteriosAtributos/")
    @FormUrlEncoded
    Call<List<CriterioAtributo>> obtenerCriteriosAtributos(@Field("idUsuario") String idUsuario);

    @POST("saveVisita/")
    Call<JsonObject> saveVisita(@Body VisitaList list);

    @POST("saveVisitaFotos/")
    Call<JsonObject> saveVisitaFoto(@Body VisitaFotoList list);

    @POST("saveVisitaIncidencias/")
    Call<JsonObject> saveVisitaInc(@Body VisitaFotoList list);

    @POST("saveVisitaEvaluacion/")
    Call<JsonObject> saveVisitaEvaluacion(@Body VisitaEvaluacionList list);

    @POST("saveVisitaCriterio/")
    Call<JsonObject> saveVisitaCriterio(@Body VisitaCriterioList list);

    @POST("saveVisitaAtributo/")
    Call<JsonObject> saveVisitaAtributo(@Body VisitaAtributoList list);
}
