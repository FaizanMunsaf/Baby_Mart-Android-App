package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.DRVinterface.LoadMore;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class dashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    private ImageView searchbtn;
    private FirebaseFirestore db;


    List<DynamicRVModel> items =new ArrayList();
    DynamicRVAdapter dynamicRVAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        searchbtn = findViewById(R.id.searchicon);
        db = FirebaseFirestore.getInstance();

        ArrayList<StaticRvModel> item = new ArrayList<>();


        db.collection("Products").orderBy("Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Product Error", error.getMessage() );
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED) {
                        QueryDocumentSnapshot snapshot=dc.getDocument();
                        ArrayList<String> image= (ArrayList<String>) snapshot.get("Images");
                        item.add(new StaticRvModel(image.get(0),snapshot.get("Name").toString(),snapshot.getId()));
                    }
                }
            }
        });

        recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item,dashboard.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);
        staticRvAdapter.notifyDataSetChanged();


        db.collection("Products")
                .orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            items.add(new DynamicRVModel(snapshot.get("Name").toString(),snapshot.getId()));
//                            Toast.makeText(dashboard.this, ""+snapshot.get("Name"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        RecyclerView drv =findViewById(R.id.rv_2);
        drv .setLayoutManager(new LinearLayoutManager(this));
        dynamicRVAdapter = new DynamicRVAdapter(drv,this,items);
        drv.setAdapter(dynamicRVAdapter);
        dynamicRVAdapter.notifyDataSetChanged();

//        dynamicRVAdapter.setLoadMore(new LoadMore() {
//            @Override
//            public void onLoadMore() {
//                if (items.size()<=10){
//                    items.add(null);
//                    dynamicRVAdapter.notifyItemInserted(items.size()-1);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            items.remove(items.size()-1);
//                            dynamicRVAdapter.notifyItemRemoved(items.size());
//
//                            int index = items.size();
//                            int end = index+10;
//                            for (int i=index;i<end;i++){
//
//                                String name = UUID.randomUUID().toString();
//                                DynamicRVModel  item =new DynamicRVModel(name,"1");
//                                items.add(item);
//                            }
//
//
//                            dynamicRVAdapter.notifyDataSetChanged();
//                            dynamicRVAdapter.setLoded();
//
//
//                        }
//                    },4000);
//
//
//
//                }
//                else
//                    Toast.makeText(dashboard.this, "Data Completed", Toast.LENGTH_SHORT).show();
//            }
//
//        });


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in= new Intent(dashboard.this,Tracking.class);
                startActivity(in);
            }
        });


    }
}