package com.example.test4.firestoreDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.test4.R;

import java.util.ArrayList;

public class PlaceUserAdapter extends RecyclerView.Adapter<PlaceUserAdapter.ViewHolder>
{
    private Context context = null;
    private ArrayList<PUItem> userItem = null;
    private PUViewListener userViewListener = null;

    public PlaceUserAdapter(ArrayList<PUItem> items, Context context, PUViewListener listener)
    {
        this.userItem = items;
        this.context = context;
        this.userViewListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_item_list, viewGroup, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.userView.setText(userItem.get(i).getTime());
        viewHolder.dateView.setText(userItem.get(i).getUser());
    }

    @Override
    public int getItemCount()
    {
        return userItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView userView = null;
        public TextView dateView = null;

        public ViewHolder(View view) {
            super(view);
            userView = (TextView)view.findViewById(R.id.User);
            dateView = (TextView)view.findViewById(R.id.Time);
        }

        @Override
        public void onClick(View view)
        {
            userViewListener.onItemClick(getAdapterPosition(), view);
        }
    }
}