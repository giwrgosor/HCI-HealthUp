package com.example.healthup.Contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class KeypadActivity extends AppCompatActivity {

    private TextView numberDisplay;
    private FloatingActionButton call, backspace;
    private ImageButton btn_homeKeypad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keypad);

        numberDisplay = findViewById(R.id.numberDisplay);
        call = findViewById(R.id.keypadCall);
        backspace = findViewById(R.id.keypadBackspace);
        btn_homeKeypad = findViewById(R.id.homeButtonKeypad);

        if ((getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES) {

            ImageView background = findViewById(R.id.editProfileBackground);
            if (background != null) {
                background.setImageResource(R.drawable.blackbackground);
            }
        }

        btn_homeKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KeypadActivity.this, MainMenuActivity.class);
                startActivity(intent);
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

}