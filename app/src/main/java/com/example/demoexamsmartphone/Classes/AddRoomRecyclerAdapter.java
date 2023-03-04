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

public class AddRoomRecyclerAdapter extends RecyclerView.Adapter<AddRoomRecyclerAdapter.ViewHolder> {
    ItemClickListener itemClickListener;
    LayoutInflater layoutInflater;
    ArrayList<RoomInAdd> rooms;
    public AddRoomRecyclerAdapter(Context context,ArrayList<RoomInAdd> rooms){
        layoutInflater = LayoutInflater.from(context);
        this.rooms = rooms;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.add_room_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(rooms.get(position).getType());
        holder.imageView.setImageDrawable(rooms.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewIconInItemAddRoom);
            textView = itemView.findViewById(R.id.textViewTypeAddRoom);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemClickListener!=null){
                itemClickListener.onClick(view,getAdapterPosition());
            }
        }
    }
    public interface ItemClickListener{
        void onClick(View view,int position);
    }
}
