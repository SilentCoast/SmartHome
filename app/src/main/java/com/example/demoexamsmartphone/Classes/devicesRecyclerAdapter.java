package com.example.demoexamsmartphone.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoexamsmartphone.R;

import java.util.ArrayList;

public class devicesRecyclerAdapter extends RecyclerView.Adapter<devicesRecyclerAdapter.ViewHolder> {
    ArrayList<Device> devices;
    LayoutInflater layoutInflater;
    ItemClickListener itemClickListener;

    public devicesRecyclerAdapter(Context context,ArrayList<Device> devices){
        layoutInflater = LayoutInflater.from(context);
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.devices_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(devices.get(position).getImage());
        holder.textView.setTextColor(devices.get(position).getTextColor());
        holder.textView.setText(devices.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageDeviceInItem);
            textView = itemView.findViewById(R.id.textViewDeviceNameInItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener!=null){
                itemClickListener.onClick(itemView,getAdapterPosition());
            }
        }
    }
    public  interface ItemClickListener{
         void onClick(View view,int position);
    }
}
