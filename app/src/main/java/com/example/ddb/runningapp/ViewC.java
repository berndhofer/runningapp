package com.example.ddb.runningapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ddb.runningapp.service.RouteService;
import com.example.ddb.runningapp.service.ServiceInstantiator;

public class ViewC extends AppCompatActivity {
RouteService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_c);
        service = ServiceInstantiator.getInstance(getBaseContext());
        ListView listView = (ListView) findViewById(R.id.listView);
        //ArrayAdapter<Route> adapter = new ArrayAdapter<Route>(getBaseContext(),android.R.layout.simple_list_item_1, service.getRoutes());
        MyArrayAdapter adapter = new MyArrayAdapter(service.getRoutes(),this);
         listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("clicked");
            }
        });

        }
    }

