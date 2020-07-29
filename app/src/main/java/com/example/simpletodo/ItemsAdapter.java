package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//responsible for displaying data onto the rows of recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items,OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener=longClickListener;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use a layout inflator to inflate a view

        View todoview = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //wrap it inside a viewholder and return
        return new ViewHolder(todoview);
    }

    //resposible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab the item at position
        String item = items.get(position);
        //bind the item into specified view holder
        holder.bind(item);
    }

    //tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tdItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tdItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view of the data inside of the view holder with this data
        public void bind(String item) {
            tdItem.setText(item);
            tdItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tdItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });

        }
    }
}
