package com.example.e_commerce_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e_commerce_app.R;
import com.example.e_commerce_app.adaptres.AdressAdapter;
import com.example.e_commerce_app.models.AdressModel;
import com.example.e_commerce_app.models.NewProductModel;
import com.example.e_commerce_app.models.PopularProductsModel;
import com.example.e_commerce_app.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdressActivity extends AppCompatActivity implements AdressAdapter.SelectedAddress  {

    Button addAdress;
    RecyclerView recyclerView;
    private List<AdressModel> adressModelList;
    private AdressAdapter adressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button AddAddressBtn,paymentBtn;
    Toolbar toolbar;
    String mAdress = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get Data from detailed activity
        Object obj= getIntent().getSerializableExtra("item");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAdress = findViewById(R.id.add_address_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adressModelList = new ArrayList<>();
        adressAdapter = new AdressAdapter(getApplicationContext(),adressModelList,this);
        recyclerView.setAdapter(adressAdapter );

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()) {
                        AdressModel adressModel = doc.toObject(AdressModel.class);
                        adressModelList.add(adressModel);
                        adressAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double amount = 0.0;
                if(obj instanceof NewProductModel){
                    NewProductModel newProductModel=(NewProductModel) obj;
                    amount = newProductModel.getPrice();
                }
                if(obj instanceof PopularProductsModel){
                    PopularProductsModel popularProductsModel=(PopularProductsModel) obj;
                    amount = popularProductsModel.getPrice();
                }
                if(obj instanceof ShowAllModel){
                    ShowAllModel showAllModel=(ShowAllModel) obj;
                    amount = showAllModel.getPrice();
                }
                Intent intent = new Intent(AdressActivity.this,PaymentActivity.class);
                intent.putExtra("amount",amount);
                startActivity(intent);
            }
        });

        addAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdressActivity.this,AddAdressActivity.class));
            }
        });
    }

    @Override
    public void setAdress(String adress) {
        mAdress = adress;

    }
}