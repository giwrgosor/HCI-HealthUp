package com.example.healthup.Contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.healthup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class KeypadActivity extends AppCompatActivity {

    private TextView numberDisplay;
    private FloatingActionButton call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_keypad);

        numberDisplay = findViewById(R.id.numberDisplay);
        call = findViewById(R.id.keypadCall);

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
                String currentText = numberDisplay.getText().toString();
                numberDisplay.setText(String.format("%s%s", currentText, digit));
            }
        };
    }
}