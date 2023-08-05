package com.example.mymapsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewListener{

    TextView textView;
    EditText input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input=findViewById(R.id.search_bar);
        textView=findViewById(R.id.emptymsg);

        //Adapter Code
        List<String> items=new LinkedList<>();

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TheAdapter adapter=new TheAdapter(items,this);
        recyclerView.setAdapter(adapter);



        DbHelper dbHelper = new DbHelper(MainActivity.this);
        List<Location1> allPlaces = dbHelper.selectAll();
        for(int i = 0; i < allPlaces.size();i++){
            String temp = allPlaces.get(i).place+"\nLatitude : "+allPlaces.get(i).latitude+"\nLongitude : "+allPlaces.get(i).longitude;
            if(!items.contains(temp)){
                items.add(temp);
            }
        }

        adapter.notifyItemInserted(items.size()-1);
        if (items.size()==0){
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setVisibility(View.INVISIBLE);
        }

    }

    public void gotoMaps(View view) {
        String location=input.getText().toString();
        if(location.isEmpty()){
            Toast.makeText(MainActivity.this,"Enter a Location to search!",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent i= new Intent(getApplicationContext(),MapsActivity.class);
            i.putExtra("location",location);
            startActivity(i);
            this.finish();
        }
    }

    @Override
    public void onItemClick(String place) {
        Intent i= new Intent(getApplicationContext(),MapsActivity.class);
        i.putExtra("location",place.split(",")[0]);
        startActivity(i);
        this.finish();
    }
}