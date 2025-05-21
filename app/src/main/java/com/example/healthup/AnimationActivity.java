package com.example.healthup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 6000;
    private static final String TEXT = "Help Up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.contactsBackground);

        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 1f, 1.5f);
        scaleUp.setDuration(1000);
        scaleUp.setRepeatCount(ObjectAnimator.INFINITE);
        scaleUp.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 1f, 1.5f);
        scaleDown.setDuration(1000);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleUp.start();
        scaleDown.start();

        TextView textView = findViewById(R.id.textView3);
        textView.setY(600);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        new Handler().postDelayed(() -> {
            for (int i = 0; i < TEXT.length(); i++) {
                final int index = i;
                new Handler().postDelayed(() -> {
                    builder.append(TEXT.charAt(index));

                    builder.setSpan(
                            new ForegroundColorSpan(0xFFFFFFFF),
                            builder.length() - 1,
                            builder.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                    builder.setSpan(
                            new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                            builder.length() - 1,
                            builder.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                    textView.setText(builder);
                }, 500 * index);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
