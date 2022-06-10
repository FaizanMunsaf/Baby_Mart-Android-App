package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;


public class CartViewAdapter extends RecyclerView.Adapter<CartViewHolder> {

    Context context;
    ArrayList<CartModel> mylist;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public CartViewAdapter(Context context, ArrayList<CartModel> mylist) {
        this.context = context;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlist_0, null);
        CartViewHolder viewHolder = new CartViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartModel currentItem = mylist.get(position);
        Picasso.with(context).load(mylist.get(position).getImage()).into(holder.image);
        holder.name.setText(currentItem.getName());
        holder.rating.setText(currentItem.getRating());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("cart-items").document(auth.getUid()).collection("items")
                        .document(currentItem.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CollectionReference _changeref = FirebaseFirestore.getInstance().collection("cart-items").document(auth.getUid()).collection("items");
                                _changeref.addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        mylist.clear();
                                        if (error != null) {
                                            return;
                                        }

                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                            DocumentSnapshot snapshot=dc.getDocument();
                                            String images=snapshot.get("Image").toString();
                                            mylist.add(new CartModel(snapshot.getString("Name"),"Quantity : "+snapshot.get("Quantity"),snapshot.getId(),snapshot.getString("Image")));
                                        }

                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        });
            }
        });

    }



    @Override
    public int getItemCount() {
        return mylist.size();
    }
}


