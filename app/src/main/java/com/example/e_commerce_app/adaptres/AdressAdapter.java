package com.example.e_commerce_app.adaptres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerce_app.R;
import com.example.e_commerce_app.models.AdressModel;

import java.util.List;

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.ViewHolder> {
    Context context;
    List<AdressModel> adressModelList;
    SelectedAddress selectedAddress;

    private RadioButton selectedRadioBtn;

    public AdressAdapter(Context context, List<AdressModel> adressModelList, SelectedAddress selectedAddress) {
        this.context = context;
        this.adressModelList = adressModelList;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.adress.setText(adressModelList.get(position).getUserAdress());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return adressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView adress;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adress = itemView.findViewById(R.id.address_add);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }
    public  interface SelectedAddress{
        void setAdress(String adress);
    }
}
