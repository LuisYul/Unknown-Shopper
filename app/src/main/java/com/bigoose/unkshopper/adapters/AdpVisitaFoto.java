package com.bigoose.unkshopper.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bigoose.unkshopper.R;
import com.bigoose.unkshopper.model.VisitaFoto;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.List;

public class AdpVisitaFoto extends RecyclerView.Adapter<AdpVisitaFoto.ViewHolder> {

    Context context;
    List<VisitaFoto> listFoto;
    ArrayAdapter<String> adp;
    String nombre_foto;
    Activity activity;
    Bitmap bitmap, nuevo;
    Gson g = new Gson();

    public AdpVisitaFoto(Context context, List<VisitaFoto> listFoto, ArrayAdapter<String> adp, Activity activity) {
        this.context = context;
        this.listFoto = listFoto;
        this.adp = adp;
        this.activity = activity;
    }

    @Override
    public AdpVisitaFoto.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpVisitaFoto.ViewHolder holder, final int position) {

        if (listFoto.size() > 0) {
            holder.spTipo.setVisibility(View.VISIBLE);
            holder.etComentario.setVisibility(View.VISIBLE);
            holder.etComentario.setText(listFoto.get(position).getComentario());
            holder.spTipo.setAdapter(adp);
            String tipo = listFoto.get(position).getIdCriterio();
            Log.e("tipo", "en pos" + position + "=" + tipo);
            if (tipo != null) {
                int spinnerPosition = adp.getPosition(tipo);
                Log.e("spinner pos", spinnerPosition + "");
                holder.spTipo.setSelection(spinnerPosition);
            }
//        listFoto.get(position).setIdCriterio(holder.spTipo.getSelectedItem().toString());
            byte[] imagen = listFoto.get(position).getFotoByte();
            if (imagen.length > 0) {
                Bitmap bm = BitmapFactory.decodeByteArray(imagen, 0, imagen.length);
                holder.ivFoto.setImageBitmap(bm);
            } else {
                holder.ivFoto.setImageDrawable(context.getResources().getDrawable(R.drawable.sinimagen));
            }
        } else {
            holder.ivFoto.setImageDrawable(context.getResources().getDrawable(R.drawable.sinimagen));
            holder.spTipo.setVisibility(View.INVISIBLE);
            holder.etComentario.setVisibility(View.INVISIBLE);
        }

        holder.etComentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listFoto.get(position).setComentario(charSequence.toString());
                Log.e("lista text changed", listFoto.get(position).getComentario() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listFoto.get(position).setIdCriterio(adapterView.getItemAtPosition(i).toString());
                Log.e("tipo", "en pos" + position + "=" + listFoto.get(position).getIdCriterio());
                Log.e("lista size", listFoto.size() + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {

        int size;
        size = (listFoto.size() == 0) ? 1 : listFoto.size();
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spTipo;
        ImageView ivFoto;
        EditText etComentario;


        public ViewHolder(View itemView) {
            super(itemView);
            spTipo = itemView.findViewById(R.id.spTipoFotoItem);
            ivFoto = itemView.findViewById(R.id.ivFotoItem);
            etComentario = itemView.findViewById(R.id.etComentarioItem);

        }

    }

}
