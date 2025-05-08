package com.example.healthup.Sos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.R;
import com.example.healthup.domain.Pill;

import java.util.List;

public class EmergencyPillAdapter extends RecyclerView.Adapter<EmergencyPillAdapter.PillViewHolder> {

    private List<Pill> pillList;

    public EmergencyPillAdapter(List<Pill> pillList) {
        this.pillList = pillList;
    }

    public static class PillViewHolder extends RecyclerView.ViewHolder {
        TextView name, frequency;

        public PillViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pillName);
            frequency = itemView.findViewById(R.id.pillFrequency);
        }
    }

    @Override
    public PillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pill, parent, false);
        return new PillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PillViewHolder holder, int position) {
        Pill pill = pillList.get(position);
        holder.name.setText(pill.getName());
        holder.frequency.setText(String.format("%d φορές την εβδομάδα", pill.getTimesPerWeek()));
    }

    @Override
    public int getItemCount() {
        return pillList.size();
    }
}
