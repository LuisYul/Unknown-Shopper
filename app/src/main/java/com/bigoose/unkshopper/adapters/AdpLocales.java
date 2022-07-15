package com.bigoose.unkshopper.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.bigoose.unkshopper.R;
import com.bigoose.unkshopper.model.Local;
import com.bigoose.unkshopper.model.Visita;

import java.util.ArrayList;
import java.util.List;


public class AdpLocales extends RecyclerView.Adapter<AdpLocales.ViewHolder> {

    private List<Visita> listVisita;
    private List<Visita> mFilteredList;
    private Context context;
    private int ItemLayout;
    private static ItemClickListener itemClickListener;

    public AdpLocales(List<Visita> listVisita, Context context, int ItemLayout) {
        this.listVisita = listVisita;
        this.mFilteredList = listVisita;
        this.context = context;
        this.ItemLayout = ItemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(ItemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvCodigo.setText(mFilteredList.get(position).getIdLocal());
        holder.tvLocal.setText(mFilteredList.get(position).getNombre_local());
        holder.tvDistrito.setText(mFilteredList.get(position).getNombre_region());
        holder.tvDireccion.setText(mFilteredList.get(position).getDireccion());

        String horaInicio,horaFin;
        horaInicio=mFilteredList.get(position).getHoraIni();
        horaFin=mFilteredList.get(position).getHoraFin();
        Log.e("horaInicio",horaInicio+"!");
        Log.e("horaFin",horaFin+"!");

        if(horaFin!=null&&!horaFin.equals("")){
            holder.tvEstado.setBackgroundColor(context.getResources().getColor(R.color.colorSuccessB));
            holder.tvEstado.setText("Finalizada");
            holder.tvEstado.setVisibility(View.VISIBLE);
        }
        else if(horaInicio!=null&&!horaInicio.equals("")){
            holder.tvEstado.setBackgroundColor(context.getResources().getColor(R.color.colorAmarilloClaro));
            holder.tvEstado.setText("Iniciada");
            holder.tvEstado.setVisibility(View.VISIBLE);
        }else{
            holder.tvEstado.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCodigo, tvLocal, tvDistrito, tvDireccion,tvEstado;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCodigo = itemView.findViewById(R.id.tvItemCodLocal);
            tvLocal = itemView.findViewById(R.id.tvItemLocal);
            tvDistrito = itemView.findViewById(R.id.tvItemDistrito);
            tvDireccion = itemView.findViewById(R.id.tvItemDireccion);
            tvEstado= itemView.findViewById(R.id.tvEstadoItemLocal);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.OnClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void OnClick(View view, int position);
    }

    public List<Visita> getList() {
        return this.mFilteredList;
    }


    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = listVisita;
                } else {

                    ArrayList<Visita> filteredList = new ArrayList<>();

                    for (Visita visita : listVisita) {

                        if (visita.getNombre_local().toLowerCase().contains(charString) ||
                                visita.getNombre_local().toUpperCase().contains(charString) ||
                                visita.getIdLocal().toLowerCase().contains(charString) ||
                                visita.getIdLocal().toUpperCase().contains(charString)) {

                            filteredList.add(visita);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<Visita>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
