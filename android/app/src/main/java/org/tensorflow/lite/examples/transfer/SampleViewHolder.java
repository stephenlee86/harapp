package org.tensorflow.lite.examples.transfer;
import androidx.recyclerview.widget.*;
import android.view.View;
import java.util.*;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



public class SampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected TextView sampleTextView;
    private ItemClickListener clickListener;

    public SampleViewHolder(View itemView, ItemClickListener relayListener) {
        super(itemView);
        sampleTextView = (TextView) itemView.findViewById(R.id.sampleNameTextView);
        itemView.setTag(itemView);
        itemView.setOnClickListener(this); // bind the listener
        clickListener = relayListener;
    }

    @Override
    public void onClick(View view) {
        if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
        if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
    }

    public void bindData(SampleViewModel viewModel) {
        sampleTextView.setText(viewModel.getSampleText());
    }
}