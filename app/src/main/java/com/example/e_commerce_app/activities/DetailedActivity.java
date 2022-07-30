package com.example.e_commerce_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.e_commerce_app.R;
import com.example.e_commerce_app.models.NewProductModel;
import com.example.e_commerce_app.models.PopularProductsModel;
import com.example.e_commerce_app.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;


    Toolbar toolbar;
    int totalQuantity=1;
    int totalPrice =0;

    //new Products
    NewProductModel newProductModel=null;

    //PopularProductsModel
    PopularProductsModel popularProductsModel =null;

    //Show All
    ShowAllModel showAllModel=null;

    FirebaseAuth auth;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        firestore = FirebaseFirestore.getInstance();

        auth= FirebaseAuth.getInstance();

        final Object obj= getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewProductModel){
            newProductModel= (NewProductModel) obj;
        }else if(obj instanceof PopularProductsModel){
            popularProductsModel=(PopularProductsModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel=(ShowAllModel) obj;
        }

        detailedImg=findViewById(R.id.detailed_img);
        quantity=findViewById(R.id.quantity);
        name=findViewById(R.id.detailed_name);
        rating=findViewById(R.id.rating);
        description=findViewById(R.id.detailed_desc);
        price=findViewById(R.id.detailed_price);

        addToCart=findViewById(R.id.add_to_cart );
        buyNow=findViewById(R.id.buy_now);

        addItems=findViewById(R.id.add_item);
        removeItems=findViewById(R.id.remove_item);


        //NEw Products
        if(newProductModel != null){
            Glide.with(getApplicationContext()).load(newProductModel.getImg_url()).into(detailedImg);
            name.setText(newProductModel.getName());
            rating.setText(newProductModel.getRating());
            description.setText(newProductModel.getDescription());
            price.setText(String.valueOf(newProductModel.getPrice()));
            name.setText(newProductModel.getName());

            totalPrice = newProductModel.getPrice() * totalQuantity;
        }


        //Popular Products
        if(popularProductsModel != null){
            Glide.with(getApplicationContext()).load(popularProductsModel.getImg_url()).into(detailedImg);
            name.setText(popularProductsModel.getName());
            rating.setText(popularProductsModel.getRating());
            description.setText(popularProductsModel.getDescription());
            price.setText(String.valueOf(popularProductsModel.getPrice()));
            name.setText(popularProductsModel.getName());

            totalPrice = popularProductsModel.getPrice() * totalQuantity;
        }

        //Show all products
        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }


        //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailedActivity.this,AdressActivity.class);

                if(newProductModel != null){
                    intent.putExtra("item",newProductModel);
                }
                if(popularProductsModel != null){
                    intent.putExtra("item", String.valueOf(popularProductsModel));
                }
                if(showAllModel != null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);

            }
        });
        //Add  to cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addtoCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newProductModel != null){
                        totalPrice = newProductModel.getPrice() * totalQuantity;
                    }
                    if(popularProductsModel != null){
                        totalPrice = popularProductsModel.getPrice() * totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }

            }
        });


    }

    private void addtoCart() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("TotalPrice", totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added To  A Cart", Toast.LENGTH_SHORT);
                finish();
            }
        });


    }



}