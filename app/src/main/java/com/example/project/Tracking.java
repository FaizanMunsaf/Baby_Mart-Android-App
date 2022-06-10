package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tracking extends AppCompatActivity {
MaterialButton caneclorder,ordernow,submit,addtocart;
MaterialTextView name,price;
ImageView image;
TextView status;
RatingBar rating;
FirebaseAuth auth;
Button inc,dec;
TextView show;
int count=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        caneclorder = findViewById(R.id.cancelorder);
        status = findViewById(R.id.status);
        ordernow = findViewById(R.id.deliver);
        submit = findViewById(R.id.rate_btn);
        addtocart = findViewById(R.id.addtocart_btn);
        rating =findViewById(R.id.ratingbar);
        name=findViewById(R.id.product_name);
        image=findViewById(R.id.product_image);
        price = findViewById(R.id.produt_price);
        inc = findViewById(R.id.increment);
        dec = findViewById(R.id.decrement);
        show = findViewById(R.id.show);


        Intent intent=getIntent();

        String p_id = intent.getStringExtra("id");
        final String[] name_txt = new String[1];
        final String[] price_text = new String[1];
        final String[] getquantity = new String[1];
        final long[] quantity = {0};

        auth = FirebaseAuth.getInstance();

        FirebaseFirestore.getInstance().collection("Products")
                .document(p_id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        name.setText(documentSnapshot.get("Name").toString());
                        name_txt[0] = documentSnapshot.get("Name").toString();
                        ArrayList<String> images_1= (ArrayList<String>) documentSnapshot.get("Images");
                        Picasso.with(getApplicationContext()).load(images_1.get(0)).into(image);
                        getquantity[0] =  documentSnapshot.get("Quantity").toString();
                        quantity[0] = Long.parseLong(getquantity[0]);
                         price.setText(documentSnapshot.get("Price").toString());
                        price_text[0] = documentSnapshot.get("Price").toString();
                    }
                });

        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count< quantity[0]){
                    count++;
                    show.setText(""+count);
                FirebaseFirestore.getInstance().collection("Products")
                        .document(p_id)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                long product_price=Long.parseLong(documentSnapshot.get("Price").toString())*count;
                                price.setText(""+product_price);
                                price_text[0] = String.valueOf(product_price);
                            }
                        });}
                else{
                    Toast.makeText(Tracking.this, "No more quanitities"+ quantity[0], Toast.LENGTH_SHORT).show();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count < 2){
                    Toast.makeText(Tracking.this, "it's less then 0", Toast.LENGTH_SHORT).show();
                } else {
                    count--;
                    show.setText(""+count);
                    FirebaseFirestore.getInstance().collection("Products")
                            .document(p_id)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    long product_price=Long.parseLong(documentSnapshot.get("Price").toString())*count;
                                    price.setText(""+product_price);
                                    price_text[0] = String.valueOf(product_price);
                                }
                            });
                }

            }
        });



        caneclorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("❌ Order Cancel");
            }
        });

        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("✔ Orderd");

                long quan=Long.parseLong(show.getText().toString());

                if(quan< quantity[0]){
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("Name", name_txt[0]);
                    docData.put("Price", price_text[0]);
                    docData.put("Quantity", quan);
                    docData.put("Image", intent.getStringExtra("Image"));
                    CollectionReference _ref = FirebaseFirestore.getInstance().collection("order-items");
                    _ref.document(auth.getUid()).collection("items")
                            .document(p_id)
                            .set(docData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    status.setText("✔ Added into your cart");
                                    Toast.makeText(Tracking.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Tracking.this, "Failed", Toast.LENGTH_SHORT).show();
                                    Log.d("Error", e.getMessage());
                                }
                            });

                    long abc = quantity[0]-quan;

                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("Quantity",abc);
                    CollectionReference h_ref = FirebaseFirestore.getInstance().collection("Products");
                    h_ref.document(p_id)
                            .update(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(Tracking.this, "Products updated", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(Tracking.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    status.setText("❌ Product is Finished");
                }

                //Toast.makeText(Tracking.this,p_id,Toast.LENGTH_SHORT).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float ratingvalue = rating.getRating();
                Toast.makeText(Tracking.this,"Rating Submited : "+ratingvalue,Toast.LENGTH_SHORT).show();

            }
        });
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quan=(int) Integer.parseInt((String) show.getText());

                if(quan< quantity[0]){
                Map<String, Object> docData = new HashMap<>();
                docData.put("Name", name_txt[0]);
                docData.put("Price", price_text[0]);
                docData.put("Quantity", quan);
                docData.put("Image", intent.getStringExtra("Image"));
                CollectionReference _ref = FirebaseFirestore.getInstance().collection("cart-items");
                _ref.document(auth.getUid()).collection("items")
                        .document(p_id)
                        .set(docData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                status.setText("✔ Added into your cart");
                                Toast.makeText(Tracking.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Tracking.this, "Failed", Toast.LENGTH_SHORT).show();
                                Log.d("Error", e.getMessage());
                            }
                        });
            }else{
                status.setText("❌ Product is Finished");
                }
            }
        });
    }
}