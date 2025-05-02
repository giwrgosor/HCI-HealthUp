package com.example.healthup.Contacts;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.R;
import com.example.healthup.domain.Contact;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {

    private int expandedPosition = -1;
    private List<Contact> contacts;

    public ContactsRecyclerViewAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone, fillContactSpace;
        ImageView callIcon;

        ImageView displayIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);
            callIcon = itemView.findViewById(R.id.callIcon);
            displayIcon = itemView.findViewById(R.id.displayIcon);
            fillContactSpace = itemView.findViewById(R.id.contactSpace);
        }
    }

    @Override
    public ContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsRecyclerViewAdapter.ViewHolder holder, int position) {

        Contact contact = contacts.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText("Κινητό: " + contact.formatPhoneNumber(contact.getPhone()));

        int[] colors = {
                Color.parseColor("#BDBDBD"), // Γκρι
                Color.parseColor("#FFCDD2"), // Ροζ
                Color.parseColor("#BBDEFB"), // Μπλε
                Color.parseColor("#FFA726")  // Πορτοκαλί
        };
        int colorIndex = position % colors.length;
        int currentColor = colors[colorIndex];
        holder.contactName.setBackgroundColor(colors[colorIndex]);

        boolean isExpanded = position == expandedPosition;

        if (isExpanded) {
            // ορατότητα
            holder.contactPhone.setVisibility(View.VISIBLE);
            holder.callIcon.setVisibility(View.VISIBLE);
            holder.displayIcon.setVisibility(View.VISIBLE);
            holder.fillContactSpace.setVisibility(View.VISIBLE);

            // χρωματισμός
            holder.contactPhone.setBackgroundColor(currentColor);
            holder.callIcon.setBackgroundColor(currentColor);
            holder.displayIcon.setBackgroundColor(currentColor);
            holder.fillContactSpace.setBackgroundColor(currentColor);

        } else {

            // ορατότητα
            holder.contactPhone.setVisibility(View.GONE);
            holder.callIcon.setVisibility(View.GONE);
            holder.displayIcon.setVisibility(View.GONE);
            holder.fillContactSpace.setVisibility(View.GONE);

            // χρωματισμός
            holder.contactPhone.setBackgroundColor(Color.TRANSPARENT);
            holder.callIcon.setBackgroundColor(Color.TRANSPARENT);
            holder.displayIcon.setBackgroundColor(Color.TRANSPARENT);
            holder.fillContactSpace.setBackgroundColor(Color.TRANSPARENT);

        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            if (adapterPosition == expandedPosition) {
                expandedPosition = -1;
            } else {
                expandedPosition = adapterPosition;
            }
            notifyDataSetChanged();
        });


        holder.callIcon.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_DIAL);  // ACTION_CALL
            intent.setData(Uri.parse("tel:" + contact.getPhone()));
            v.getContext().startActivity(intent);
        });

        holder.displayIcon.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DisplayContactsActivity.class);
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
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
                String name1 = removeAccents(c1.getName());
                String name2 = removeAccents(c2.getName());
                return name1.compareToIgnoreCase(name2);
            }

            private String removeAccents(String text) {
                if (text == null) return "";
                return Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            }
        });
    }


}


