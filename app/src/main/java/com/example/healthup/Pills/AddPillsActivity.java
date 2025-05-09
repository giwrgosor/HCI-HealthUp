package com.example.healthup.Pills;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.PillsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;

import java.util.HashMap;
import java.util.Map;

public class AddPillsActivity extends AppCompatActivity {
    private EditText pillNameInput;
    private CheckBox beforeBreakfast, afterBreakfast, noon, afternoon, beforeDinner, beforeSleep;
    private String selectedDay = "ΔΕΥ";
    private final Map<String, Button> dayButtons = new HashMap<>();
    private final Map<String, boolean[]> daySchedules = new HashMap<>();
    private final PillsDAO pillsDAO = new PillsMemoryDAO();
    private Button btn_addPill;
    private ImageButton btn_homePill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pills);

        pillNameInput = findViewById(R.id.pillName);

        beforeBreakfast = findViewById(R.id.checkbox_monday_before_breakfast);
        afterBreakfast = findViewById(R.id.checkbox_monday_after_breakfast);
        noon = findViewById(R.id.checkbox_monday_noon);
        afternoon = findViewById(R.id.checkbox_monday_afternoon);
        beforeDinner = findViewById(R.id.checkbox_monday_before_dinner);
        beforeSleep = findViewById(R.id.checkbox_monday_before_sleep);

        btn_addPill = findViewById(R.id.addPillButton);
        btn_homePill = findViewById(R.id.homePill);

        setupDayButtons();
        updateDayButtonColors();

        btn_addPill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePill();
            }
        });

        btn_homePill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPillsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

    }


    private void setupDayButtons() {
        dayButtons.put("ΔΕΥ", findViewById(R.id.mondayButton));
        dayButtons.put("ΤΡΙ", findViewById(R.id.tuesdayButton));
        dayButtons.put("ΤΕΤ", findViewById(R.id.wednesdayButton));
        dayButtons.put("ΠΕΜ", findViewById(R.id.thursdayButton));
        dayButtons.put("ΠΑΡ", findViewById(R.id.fridayButton));
        dayButtons.put("ΣΑΒ", findViewById(R.id.saturdayButton));
        dayButtons.put("ΚΥΡ", findViewById(R.id.sundayButton));

        for (Map.Entry<String, Button> entry : dayButtons.entrySet()) {
            entry.getValue().setOnClickListener(v -> {
                saveCurrentDaySchedule();
                selectedDay = entry.getKey();
                updateDayButtonColors();
                loadScheduleForSelectedDay();
            });
        }
    }

    private void saveCurrentDaySchedule() {
        boolean[] schedule = new boolean[]{
                beforeBreakfast.isChecked(),
                afterBreakfast.isChecked(),
                noon.isChecked(),
                afternoon.isChecked(),
                beforeDinner.isChecked(),
                beforeSleep.isChecked()
        };
        daySchedules.put(selectedDay, schedule);
    }

    private void loadScheduleForSelectedDay() {

        boolean[] schedule;
        if (daySchedules.containsKey(selectedDay)) {
            schedule = daySchedules.get(selectedDay);
        } else {
            schedule = new boolean[6];
        }

        beforeBreakfast.setChecked(schedule[0]);
        afterBreakfast.setChecked(schedule[1]);
        noon.setChecked(schedule[2]);
        afternoon.setChecked(schedule[3]);
        beforeDinner.setChecked(schedule[4]);
        beforeSleep.setChecked(schedule[5]);
    }

    private void updateDayButtonColors() {
        for (Map.Entry<String, Button> entry : dayButtons.entrySet()) {
            if (entry.getKey().equals(selectedDay)) {
                entry.getValue().setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red));
            } else {
                entry.getValue().setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.blue));
            }
        }
    }

    private void savePill() {
        saveCurrentDaySchedule();

        String name = pillNameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Παρακαλώ εισάγετε όνομα χαπιού", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pillsDAO.existsByName(name)) {
            Toast.makeText(this, "Υπάρχει ήδη χάπι με αυτό το όνομα!", Toast.LENGTH_SHORT).show();
            return;
        }

        Pill pill = new Pill();
        pill.setName(name);
        for (Map.Entry<String, boolean[]> entry : daySchedules.entrySet()) {
            pill.setScheduleForDay(entry.getKey(), entry.getValue());
        }

        pillsDAO.save(pill);
        Toast.makeText(this, "Το χάπι αποθηκεύτηκε!", Toast.LENGTH_SHORT).show();
        finish();
    }


}