package com.example.ddb.runningapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ddb.runningapp.pojo.Route;
import com.example.ddb.runningapp.service.RouteService;
import com.example.ddb.runningapp.service.ServiceInstantiator;

import java.util.ArrayList;
import java.util.Comparator;

public class MyArrayAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Route> list = new ArrayList<>();
    private Context context;
    private RouteService service;


    public MyArrayAdapter(ArrayList<Route> list, Context context) {
        this.list = list;
        this.context = context;
        this.service = ServiceInstantiator.getInstance(context);
        list.sort(new Comparator<Route>() {
            @Override
            public int compare(Route route, Route t1) {
                return route.getDate().compareTo(t1.getDate());
            }
        });
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button infoBtn = (Button)view.findViewById(R.id.info_btn);
        if(service.getLocationsOfRoute(list.get(position).getNo()).isEmpty() || list.get(position).getDistance()<0){
            infoBtn.setEnabled(false);
            notifyDataSetChanged();
        }

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                service.removeRoute(list.get(position).getNo());
                 list.remove(list.get(position)); //or some other task

                notifyDataSetChanged();
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                Intent intent = new Intent(context,ViewD.class);
                intent.putExtra("RouteNo", list.get(position).getNo());
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}