package com.example.mymapsapp;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TheAdapter extends RecyclerView.Adapter<Viewholder>{

    List<String> items;
    private RecyclerViewListener recyclerViewListener;
    public TheAdapter(List<String> items, RecyclerViewListener recyclerViewListener) {
        this.items = items;
        this.recyclerViewListener = recyclerViewListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new Viewholder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView.setText(items.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewListener.onItemClick(items.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
class Viewholder extends RecyclerView.ViewHolder{
    TextView textView;
    public LinearLayout layout;
    private TheAdapter adapter;
    public Viewholder(@NonNull View itemView) {
        super(itemView);
        textView= itemView.findViewById(R.id.text);
        layout = itemView.findViewById(R.id.card);
        itemView.findViewById(R.id.delete).setOnClickListener(view -> {
            String remove = adapter.items.remove(getAdapterPosition());
            DbHelper dbHelper=new DbHelper(view.getContext());
            int fcolon=remove.indexOf(":")+1;
            int lcolon=remove.lastIndexOf(":")+1;
            dbHelper.delete(Double.parseDouble(remove.substring(fcolon).split("\n")[0])
                    ,Double.parseDouble(remove.substring(lcolon)));
            adapter.notifyItemRemoved(getAdapterPosition());

        });

    }

    public Viewholder linkAdapter(TheAdapter adapter){
        this.adapter = adapter;
        return this;
    }

}
