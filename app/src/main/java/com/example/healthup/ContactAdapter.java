package com.example.healthup;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private int expandedPosition = -1;
    private List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone, fillContactSpace;
        ImageView callIcon;

        ImageView editIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            callIcon = itemView.findViewById(R.id.callIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
            fillContactSpace = itemView.findViewById(R.id.contactSpace);
        }
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int position) {

        Contact contact = contacts.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhone());

        int[] colors = {
                Color.parseColor("#BDBDBD"), // Γκρι
                Color.parseColor("#FFCDD2"), // Ροζ
                Color.parseColor("#BBDEFB"), // Μπλε
                Color.parseColor("#FFA726")  // Πορτοκαλί
        };
        int colorIndex = position % colors.length;
        int currentColor = colors[colorIndex];
        holder.contactName.setBackgroundColor(colors[colorIndex]);

        boolean isExpanded;
        if (position == expandedPosition) {
            isExpanded = true;
        } else {
            isExpanded = false;
        }

        if (isExpanded) {
            holder.contactPhone.setVisibility(View.VISIBLE);
            holder.callIcon.setVisibility(View.VISIBLE);
            holder.editIcon.setVisibility(View.VISIBLE);
            holder.fillContactSpace.setVisibility(View.VISIBLE);
        } else {
            holder.contactPhone.setVisibility(View.GONE);
            holder.callIcon.setVisibility(View.GONE);
            holder.editIcon.setVisibility(View.GONE);
            holder.fillContactSpace.setVisibility(View.GONE);
        }

        if (isExpanded) {
            holder.contactPhone.setBackgroundColor(currentColor);
            holder.callIcon.setBackgroundColor(currentColor);
            holder.editIcon.setBackgroundColor(currentColor);
            holder.fillContactSpace.setBackgroundColor(currentColor);
        } else {
            holder.contactPhone.setBackgroundColor(Color.TRANSPARENT);
            holder.callIcon.setBackgroundColor(Color.TRANSPARENT);
            holder.editIcon.setBackgroundColor(Color.TRANSPARENT);
            holder.fillContactSpace.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            if (isExpanded) {
                expandedPosition = -1;
            } else {
                expandedPosition = position;
            }
            notifyDataSetChanged();
        });

        holder.callIcon.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);  // ACTION_CALL
            intent.setData(Uri.parse("tel:" + contact.getPhone()));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void sortContacts(List<Contact> contactList) {
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
    }
}


