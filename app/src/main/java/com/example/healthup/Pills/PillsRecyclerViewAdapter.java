package com.example.healthup.Pills;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;

import java.util.List;

public class PillsRecyclerViewAdapter extends RecyclerView.Adapter<PillsRecyclerViewAdapter.ViewHolder> {

    private List<Pill> pills;
    private PillsDAO pillsDAO;
    private Activity activity;
    public PillsRecyclerViewAdapter(Activity activity, List<Pill> pills, PillsDAO pillsDAO) {
        this.activity = activity;
        this.pills = pills;
        this.pillsDAO = pillsDAO;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pillName;
        ImageView pillListEdit, pillListDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            pillName = itemView.findViewById(R.id.pillListName);
            pillListEdit = itemView.findViewById(R.id.pillListEdit);
            pillListDelete = itemView.findViewById(R.id.pillListDelete);
        }
    }

    @NonNull
    @Override
    public PillsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pill_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PillsRecyclerViewAdapter.ViewHolder holder, int position) {
        Pill pill = pills.get(position);
        holder.pillName.setText(pill.getName());

        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            holder.pillName.setTextColor(ContextCompat.getColor(activity, android.R.color.white));
        } else {
            holder.pillName.setTextColor(ContextCompat.getColor(activity, android.R.color.black));
        }


        holder.pillListEdit.setOnClickListener(v -> {
        });

        holder.pillListDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this pill \"" + pill.getName() + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Pill pillToDelete = pills.get(position);
                        pillsDAO.delete(pillToDelete);
                        pills.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pills.size());
                        Toast.makeText(v.getContext(), "Pill deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.pillListEdit.setOnClickListener(v -> {
            Intent intent = new Intent(activity, EditPillsActivity.class);
            intent.putExtra("pill", pill);
            activity.startActivityForResult(intent, 2001);
        });

    }

    @Override
    public int getItemCount() {
        return pills.size();
    }

    public void updatePills(List<Pill> updatedList) {
        this.pills = updatedList;
        notifyDataSetChanged();
    }
}
