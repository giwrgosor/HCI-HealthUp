package com.example.healthup;

import androidx.appcompat.app.AppCompatActivity;

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

    private static final int[] COLORS = {
            0xFFE91E63, // Ροζ
            0xFF2196F3, // Μπλε
            0xFFFFC107, // Κίτρινο
            0xFF4CAF50, // Πράσινο
            0xFFFF5722, // Πορτοκαλί
            0xFF9C27B0, // Μωβ
            0xFF00BCD4  // Κυανό
    };

    private static final int SPLASH_DURATION = 6000;
    private static final String TEXT = "Help Up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        ImageView imageView = findViewById(R.id.imageView);

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
                            new ForegroundColorSpan(COLORS[index % COLORS.length]),
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
