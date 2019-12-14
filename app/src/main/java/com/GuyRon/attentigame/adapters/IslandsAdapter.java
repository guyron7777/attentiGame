package com.GuyRon.attentigame.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.GuyRon.attentigame.R;
import com.GuyRon.attentigame.models.ColorsObj;

public class IslandsAdapter extends RecyclerView.Adapter<IslandsAdapter.ViewHolder> {

    private int[][] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int positionOfColor = -1;
    //we can add here more colors - and change the number of colors in mainactivityviewmodel-
    private int[] colors = {Color.WHITE, Color.BLUE, Color.BLACK, Color.CYAN, Color.GREEN, Color.GRAY, Color.RED};

    // data is passed into the constructor
    public IslandsAdapter(Context context, int[][] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_item, parent, false);
        //change the size of every cell
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        // 5 rows in the screen
        layoutParams.height = (parent.getHeight() / 5);
        //all the columns scratch in the screen
        layoutParams.width = (parent.getWidth() / mData[0].length);
        view.setLayoutParams(layoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(getItem(position) + "");
        //the ocean is white all the islands in other colors
        if (positionOfColor == -1) {
            holder.myTextView.setBackgroundColor(Color.WHITE);
        } else {
            holder.myTextView.setBackgroundColor(colors[((int) getItem(position))]);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length * mData[0].length;
    }

    // stores and recycles views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private Object getItem(int position) {
        return mData[position / mData[0].length][position % mData[0].length];
    }

    public void changeColor(ColorsObj color) {
        // 0 is for white and we dont want to repeat the with color - it is only to the ocean
        // the "4" is for green because MACABI HAIFA :) it could be any of them
        if (color.getColorNumber() == 0) color.setColorNumber(4);
        //pick the right color
        positionOfColor = color.getColorNumber();
        mData[color.getRow()][color.getCol()] = color.getColorNumber();
    }
}