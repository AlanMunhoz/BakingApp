package com.devandroid.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ListViewHolder> {

    private ArrayList<String> mListItems;
    private ListItemClickListener mOnClickListener;
    private Context mContext;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    MainAdapter(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setListAdapter(ArrayList<String> listItems) {
        mListItems = listItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_item, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(mListItems==null)
            return 0;
        return mListItems.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvRecipe;

        ListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvRecipe = itemView.findViewById(R.id.tv_recipe);
        }

        void bind(int listIndex) {
            tvRecipe.setText(mListItems.get(listIndex));
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
