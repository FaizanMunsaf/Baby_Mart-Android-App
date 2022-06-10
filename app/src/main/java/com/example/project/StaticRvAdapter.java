package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StaticRvAdapter  extends RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder>{

    private ArrayList<StaticRvModel> items;
    Context context;
    int row_index= -1;

    public StaticRvAdapter(ArrayList<StaticRvModel> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public StaticRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.static_rv_item,parent,false);
        StaticRVViewHolder staticRVViewHolder = new StaticRVViewHolder(view);
        return staticRVViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull StaticRVViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        StaticRvModel currentItem = items.get(position);
        Picasso.with(context).load(items.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(currentItem.getText());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              row_index = position;
                Intent intent=new Intent(context,Tracking.class);
                intent.putExtra("id",items.get(position).getId());
                intent.putExtra("Image",items.get(position).getImage());
                context.startActivity(intent);
              notifyDataSetChanged();
            }
        });
        if (row_index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_selected_bg);
        }
        else
        {
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class StaticRVViewHolder extends RecyclerView.ViewHolder{

       TextView textView;
       ImageView imageView;
       LinearLayout linearLayout;

       public StaticRVViewHolder(@NonNull View itemView) {
           super(itemView);
           imageView = itemView.findViewById(R.id. image);
           textView = itemView.findViewById(R.id . text);
           linearLayout = itemView.findViewById(R.id . linearlayout);
       }
   }
}
