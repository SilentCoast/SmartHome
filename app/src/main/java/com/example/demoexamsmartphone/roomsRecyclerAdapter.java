package com.example.demoexamsmartphone;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoexamsmartphone.Classes.Room;

import java.util.ArrayList;

public class roomsRecyclerAdapter extends RecyclerView.Adapter<roomsRecyclerAdapter.ViewHolder> {
    ArrayList<Room> rooms;
    LayoutInflater layoutInflater;
    ItemClickListener mClickListener;
    public roomsRecyclerAdapter(Context context,ArrayList<Room> rooms){
        layoutInflater = LayoutInflater.from(context);
        this.rooms = rooms;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rooms_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(rooms.get(position).getImage());
        holder.textViewName.setText(rooms.get(position).getName());
        //
    }

    public Object getItem(int id){
        return rooms.get(id);
    }
    public Object getRoomName(int id){return rooms.get(id).getName();}
    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setClicklistener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textViewName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewInItem);
            textViewName = itemView.findViewById(R.id.textViewRoomName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener!=null) mClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public interface ItemClickListener{
        void onItemClick(View view,int position);
    }
}
