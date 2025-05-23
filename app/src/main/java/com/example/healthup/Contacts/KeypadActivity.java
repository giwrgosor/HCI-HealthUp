package com.example.healthup.Contacts;

import android.content.Intent;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.healthup.MainActivity;
import com.example.healthup.MainMenuActivity;
import com.example.healthup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class KeypadActivity extends AppCompatActivity {

    private TextView numberDisplay;
    private FloatingActionButton call, backspace;
    private ImageButton btn_homeKeypad;
    private ImageButton voiceKeypad_btn;
    private SoundPool soundPool;
    private Map<String, Integer> soundMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keypad);

        numberDisplay = findViewById(R.id.numberDisplay);
        call = findViewById(R.id.keypadCall);
        backspace = findViewById(R.id.keypadBackspace);
        btn_homeKeypad = findViewById(R.id.homeButtonKeypad);

        voiceKeypad_btn = findViewById(R.id.voiceRecKeypad);

        soundPool = new SoundPool.Builder().setMaxStreams(12).build();
        soundMap.put("0", soundPool.load(this, R.raw.number0, 1));
        soundMap.put("1", soundPool.load(this, R.raw.number1, 1));
        soundMap.put("2", soundPool.load(this, R.raw.number2, 1));
        soundMap.put("3", soundPool.load(this, R.raw.number3, 1));
        soundMap.put("4", soundPool.load(this, R.raw.number4, 1));
        soundMap.put("5", soundPool.load(this, R.raw.number5, 1));
        soundMap.put("6", soundPool.load(this, R.raw.number6, 1));
        soundMap.put("7", soundPool.load(this, R.raw.number7, 1));
        soundMap.put("8", soundPool.load(this, R.raw.number8, 1));
        soundMap.put("9", soundPool.load(this, R.raw.number9, 1));
        soundMap.put("*", soundPool.load(this, R.raw.number_star, 1));
        soundMap.put("#", soundPool.load(this, R.raw.number_pound, 1));

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            ImageView background = findViewById(R.id.editProfileBackground);
            if (background != null) {
                background.setImageResource(R.drawable.contacts_dark_screen);
            }
        }

        btn_homeKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeypadActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });

        voiceKeypad_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int REQUEST_SPEECH_RECOGNIZER = 3000;
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el-GR");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Πείτε τι θα θέλατε");
                startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentRaw = numberDisplay.getText().toString().replaceAll("\\s+", "");
                if (!currentRaw.isEmpty()) {
                    currentRaw = currentRaw.substring(0, currentRaw.length() - 1);
                    numberDisplay.setText(formatPhoneNumber(currentRaw));
                }
            }
        });

        setupKeyListeners();
    }

    private void setupKeyListeners() {
        findViewById(R.id.btn1).setOnClickListener(onKeyClick("1"));
        findViewById(R.id.btn2).setOnClickListener(onKeyClick("2"));
        findViewById(R.id.btn3).setOnClickListener(onKeyClick("3"));
        findViewById(R.id.btn4).setOnClickListener(onKeyClick("4"));
        findViewById(R.id.btn5).setOnClickListener(onKeyClick("5"));
        findViewById(R.id.btn6).setOnClickListener(onKeyClick("6"));
        findViewById(R.id.btn7).setOnClickListener(onKeyClick("7"));
        findViewById(R.id.btn8).setOnClickListener(onKeyClick("8"));
        findViewById(R.id.btn9).setOnClickListener(onKeyClick("9"));
        findViewById(R.id.btn0).setOnClickListener(onKeyClick("0"));
        findViewById(R.id.btnHash).setOnClickListener(onKeyClick("#"));
        findViewById(R.id.btnStar).setOnClickListener(onKeyClick("*"));

        findViewById(R.id.btnStar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberDisplay.setText("");
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = numberDisplay.getText().toString();
                if (!phone.isEmpty()) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } else {
                    Toast.makeText(KeypadActivity.this, "Παρακαλώ συμπληρώστε το τηλέφωνο που θέλετε να καλέσετε.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private View.OnClickListener onKeyClick(final String digit) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentRaw = numberDisplay.getText().toString().replaceAll("\\s+", "");
                if (currentRaw.length() >= 10) return;

                currentRaw += digit;
                numberDisplay.setText(formatPhoneNumber(currentRaw));

                Integer sound = soundMap.get(digit);
                if (sound != null) {
                    soundPool.play(sound, 1, 1, 0, 0, 1);
                }
            }
        };
    }

    private String formatPhoneNumber(String digits) {
        if (digits.length() <= 3) {
            return digits;
        } else if (digits.length() <= 6) {
            return digits.substring(0, 3) + " " + digits.substring(3);
        } else {
            return digits.substring(0, 3) + " " + digits.substring(3, 6) + " " + digits.substring(6);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

}