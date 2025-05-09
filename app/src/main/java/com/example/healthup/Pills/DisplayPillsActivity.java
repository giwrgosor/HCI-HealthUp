package com.example.healthup.Pills;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.PillsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DisplayPillsActivity extends AppCompatActivity {

    private FloatingActionButton btn_addPill;
    private ImageButton btn_homePill;

    private RecyclerView recyclerView;
    private List<Pill> pillList;
    private PillsRecyclerViewAdapter adapter;

    private PillsDAO pillsDAO;
    private Pill pill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pills);

        btn_addPill = findViewById(R.id.addPillIcon);
        btn_homePill = findViewById(R.id.homeViewPill);

        recyclerView = findViewById(R.id.pillsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pillsDAO = new PillsMemoryDAO();
        pillList = pillsDAO.findAll();

        adapter = new PillsRecyclerViewAdapter(pillList, pillsDAO);
        recyclerView.setAdapter(adapter);

        int spacingPx = 32;
        int grayColor = getResources().getColor(android.R.color.darker_gray);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, grayColor, spacingPx));


        btn_addPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPillsActivity.this, AddPillsActivity.class);
                startActivity(intent);
            }
        });

        btn_homePill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayPillsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        pillList.clear();
        pillList.addAll(pillsDAO.findAll());
        adapter.notifyDataSetChanged();
    }

}
