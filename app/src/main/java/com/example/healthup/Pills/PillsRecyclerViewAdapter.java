package com.example.healthup.Pills;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;

import java.util.List;

public class PillsRecyclerViewAdapter extends RecyclerView.Adapter<PillsRecyclerViewAdapter.ViewHolder> {

    private List<Pill> pills;
    private PillsDAO pillsDAO;

    public PillsRecyclerViewAdapter(List<Pill> pills, PillsDAO pillsDAO) {
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

        holder.pillListEdit.setOnClickListener(v -> {
        });

        holder.pillListDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Επιβεβαίωση διαγραφής")
                    .setMessage("Είστε σίγουρος ότι θέλετε να διαγράψετε το χάπι;")
                    .setPositiveButton("Ναι", (dialog, which) -> {
                        Pill pillToDelete = pills.get(position);
                        pillsDAO.delete(pillToDelete);
                        pills.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pills.size());
                        Toast.makeText(v.getContext(), "Το χάπι διαγράφηκε", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Όχι", null)
                    .show();
        });

        holder.pillListEdit.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, EditPillsActivity.class);
            intent.putExtra("pill", pill);
            context.startActivity(intent);
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
