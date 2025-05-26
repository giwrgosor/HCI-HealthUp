package com.example.healthup.Pills;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthup.MainMenuActivity;
import com.example.healthup.MemoryDAO.PillsMemoryDAO;
import com.example.healthup.R;
import com.example.healthup.dao.PillsDAO;
import com.example.healthup.domain.Pill;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class PillScheduleActivity extends AppCompatActivity {

    private FloatingActionButton btn_displayPill;
    private ImageButton btn_homePill;
    private LinearLayout scheduleLayout;
    private PillsDAO pillDAO;
    private TextView dayTextView;
    private ImageButton voicePillSchedule_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_schedule);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_displayPill = findViewById(R.id.displayPillIcon);
        btn_homePill = findViewById(R.id.homePillSchedule);
        scheduleLayout = findViewById(R.id.schedule_layout);

        dayTextView = findViewById(R.id.dayPill);
        pillDAO = new PillsMemoryDAO();

        voicePillSchedule_btn = findViewById(R.id.voiceRecPillSchedule);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            int whiteColor = getResources().getColor(android.R.color.white);
            ImageView background = findViewById(R.id.imageView2);

            if (background != null) {
                background.setImageResource(R.drawable.pills_dark_screen);
            }

            int[] textViewIds = {
                    R.id.dayPill
            };

            for (int id : textViewIds) {
                ((android.widget.TextView) findViewById(id)).setTextColor(whiteColor);
            }
        }

        voicePillSchedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"What do you want?");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        btn_displayPill.setOnClickListener(view -> {
            Intent intent = new Intent(PillScheduleActivity.this, DisplayPillsActivity.class);
            startActivityForResult(intent, 3001);
        });

        btn_homePill.setOnClickListener(view -> {
            Intent intent = new Intent(PillScheduleActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        String fullDay = getDays();

        if (fullDay.contains("SUN")){
            dayTextView.setText("~   SUNDAY   ~");
        } else if (fullDay.contains("MON")){
            dayTextView.setText("~   MONDAY   ~");
        } else if (fullDay.contains("TUE")) {
            dayTextView.setText("~   TUESDAY   ~");
        } else if (fullDay.contains("WED")){
            dayTextView.setText("~   WEDNESDAY   ~");
        } else if (fullDay.contains("THU")){
            dayTextView.setText("~   THURSDAY   ~");
        } else if (fullDay.contains("FRI")){
            dayTextView.setText("~   FRIDAY   ~");
        } else {
            dayTextView.setText("~   SATURDAY   ~");
        }
        //dayTextView.setText(fullDay);

        displayScheduleForToday(getDays());
    }

    private String getDays() {
        String[] greekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        int dayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        return greekDays[dayIndex];
    }

    private void displayScheduleForToday(String day) {
        String[] timeSlots = {
                "Before Breakfast", "After Breakfast", "Moon",
                "Afternoon", "Before Dinner", "Before Sleep"
        };

        ArrayList<Pill> allPills = pillDAO.findAll();

        for (int i = 0; i < timeSlots.length; i++) {
            boolean hasPillsForSlot = false;

            LinearLayout titleRow = new LinearLayout(this);
            titleRow.setOrientation(LinearLayout.HORIZONTAL);
            titleRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            titleRow.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView slotTitle = new TextView(this);
            slotTitle.setText(timeSlots[i]);
            slotTitle.setTextSize(22);

            int[] slotColors = {
                    getResources().getColor(R.color.colorSlot3),
            };

            int[] darkSlotColors = {
                    getResources().getColor(R.color.slot2),
            };

//            slotTitle.setTextColor(slotColors[i]);

            int currentNightMode = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;

            if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                slotTitle.setTextColor(darkSlotColors[0]);
            } else {
                slotTitle.setTextColor(slotColors[0]);
            }


            slotTitle.setPadding(0, 24, 0, 8);
            slotTitle.setTypeface(null, Typeface.BOLD);

            for (Pill pill : allPills) {
                boolean[] schedule = pill.getScheduleForDay(day);
                if (schedule != null && schedule.length > i && schedule[i]) {
                    hasPillsForSlot = true;
                    break;
                }
            }

            if (hasPillsForSlot) {
                titleRow.addView(slotTitle);
                scheduleLayout.addView(titleRow);

                View separatorLine = new View(this);
                separatorLine.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2
                ));
                separatorLine.setBackgroundColor(getResources().getColor(R.color.gray));
                scheduleLayout.addView(separatorLine);

                boolean hasPills = false;

                for (Pill pill : allPills) {
                    boolean[] schedule = pill.getScheduleForDay(day);
                    if (schedule != null && schedule.length > i && schedule[i]) {
                        LinearLayout pillRow = new LinearLayout(this);
                        pillRow.setOrientation(LinearLayout.HORIZONTAL);
                        pillRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        pillRow.setPadding(0, 4, 0, 4);

                        TextView pillName = new TextView(this);
                        pillName.setText(pill.getName());
//                        pillName.setTextColor(getResources().getColor(R.color.black));

                        int currentNightMode1 = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
                        if (currentNightMode1 == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
                            pillName.setTextColor(getResources().getColor(android.R.color.white));
                        } else {
                            pillName.setTextColor(getResources().getColor(R.color.black));
                        }


                        pillName.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1
                        ));
                        pillName.setTextSize(20);

                        CheckBox checkBox = new CheckBox(this);

//                        String key = pill.getName() + "_" + timeSlots[i];
                        String today = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                        String key = pill.getName() + "_" + timeSlots[i] + "_" + today;

                        SharedPreferences sharedPreferences = getSharedPreferences("PillPreferences", MODE_PRIVATE);
                        boolean isTaken = sharedPreferences.getBoolean(key, pill.isTaken());
                        checkBox.setChecked(isTaken);

                        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(key, isChecked);
                            editor.apply();
                        });

                        pillRow.addView(pillName);
                        pillRow.addView(checkBox);

                        scheduleLayout.addView(pillRow);
                        hasPills = true;
                    }
                }

                if (!hasPills) {
                    TextView noPills = new TextView(this);
                    noPills.setText("No pills available");
                    noPills.setTextSize(14);
                    noPills.setTextColor(getResources().getColor(R.color.gray));
                    scheduleLayout.addView(noPills);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3001) {
            scheduleLayout.removeAllViews();
            displayScheduleForToday(getDays());
        }
    }



}
