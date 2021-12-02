package org.tensorflow.lite.examples.transfer;
import androidx.recyclerview.widget.*;

import android.graphics.Color;
import android.view.View;
import java.util.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class SampleAdapter extends RecyclerView.Adapter <SampleViewHolder> {
    //private List<String> samples;
    private DataModels sample_data;
    private int rowLayout;
    private Context mContext;
    private ItemClickListener clickListener;
    protected int selected_row = RecyclerView.NO_POSITION;
    private int selection_color;
 
    public SampleAdapter(DataModels samples, int rowLayout, Context context) {
        this.sample_data = samples;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.selected_row = -1;
        selection_color = 0xFF0A0AFF; //Color.valueOf(255, 32, 32, 255);
    }
    
    @Override
    public SampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new SampleViewHolder (view, clickListener);
    }

    @Override
    public void onBindViewHolder(SampleViewHolder holder, int position) {
        String sample = sample_data.getSampleNames ().get(position);
        holder.sampleTextView.setText(sample);
        TextView sampleTextView = (TextView) holder.itemView.findViewById(R.id.sampleNameTextView);
        sampleTextView.setBackgroundColor(selected_row == position ? 0xFFaaaaff : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return sample_data == null ? 0 : sample_data.getSampleNames ().size ();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.rv_sample_row;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public int getSelectedRow()
    {
        return selected_row;
    }

    public void setSelectedRow(int row)
    {
        selected_row = row;
    }
}