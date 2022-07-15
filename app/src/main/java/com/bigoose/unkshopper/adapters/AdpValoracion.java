package com.bigoose.unkshopper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bigoose.unkshopper.R;
import com.bigoose.unkshopper.model.CriterioAtributo;
import com.bigoose.unkshopper.model.ListaValoracion;
import com.bigoose.unkshopper.model.Valoracion;
import com.google.gson.Gson;

import java.util.List;

public class AdpValoracion extends RecyclerView.Adapter<AdpValoracion.ViewHolder> {

    Context context;
    List<ListaValoracion> listaTotal;
    List<CriterioAtributo> list;
    Gson g = new Gson();
    RatingBar rbTotal;
    double promedio,promedioPunt;
    double total=0,totalPunt=0;
    int indice;


    public AdpValoracion(Context context, List<ListaValoracion> listaTotal,RatingBar rbTotal,int indice) {
        this.context = context;
        this.listaTotal = listaTotal;
        this.rbTotal=rbTotal;
        this.indice=indice;
        list=listaTotal.get(indice).getListaVal();
        for(int i=0;i<list.size();i++){
            double valor=Double.parseDouble(list.get(i).getValor());
            double punt=list.get(i).getPuntaje();
            total+=valor;
            totalPunt+=punt;
        }
        promedio=total/list.size();
        Log.w("promedio",promedio+"");
        rbTotal.setRating((float) promedio);
    }

    @NonNull
    @Override
    public AdpValoracion.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_valoracion,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {
        holder.tvItem.setText(list.get(position).getAtributo());
        holder.rbPuntaje.setRating((float) Double.parseDouble(list.get(position).getValor()));


        holder.rbPuntaje.setOnRatingBarChangeListener(  new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                if(b==true){
                    list.get(position).setValor(v+"");
                    list.get(position).setPuntaje(v*4);
                    Log.e("posicion: ",position+" nuevo puntaje: "+v);
                    Log.e("nueva lista: ",g.toJson(list));
                    total=0;
                    totalPunt=0;
                    for(int i=0;i<list.size();i++){
                        total+=Double.parseDouble(list.get(i).getValor());
                        totalPunt+=list.get(i).getPuntaje();
                    }
                    promedio=total/list.size();
                    Log.w("promedio",promedio+"");
                    promedioPunt=totalPunt/list.size();
                    Log.w("promedio punt",promedioPunt+"");
                    rbTotal.setRating((float) promedio);
                    listaTotal.get(indice).setPromedio(promedio);
                    listaTotal.get(indice).setPromedioPunt(promedioPunt);
                    Log.e("lista total en adp",g.toJson(listaTotal));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        RatingBar rbPuntaje;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tvItemItem);
            rbPuntaje=itemView.findViewById(R.id.rbPuntaje);
            itemView.setTag(itemView);
        }
    }

}