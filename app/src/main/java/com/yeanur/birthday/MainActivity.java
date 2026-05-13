package com.yeanur.birthday;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String TARGET_DATE = "2026-05-13 00:00:00";

    private TextView tvCountdown, tvLateMessage, tvTapHint, tvFinalMessage, tvHappyBirthday;
    private ImageView imgSky, imgFlame, imgCake, imgCandle20;
    private Button btnLateWish;
    private View darkOverlay;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCountdown = findViewById(R.id.tvCountdown);
        tvLateMessage = findViewById(R.id.tvLateMessage);
        tvTapHint = findViewById(R.id.tvTapHint);
        tvFinalMessage = findViewById(R.id.tvFinalMessage);
        tvHappyBirthday = findViewById(R.id.tvHappyBirthday);
        imgSky = findViewById(R.id.imgSky);
        imgFlame = findViewById(R.id.imgFlame);
        imgCake = findViewById(R.id.imgCake);
        imgCandle20 = findViewById(R.id.imgCandle20);
        btnLateWish = findViewById(R.id.btnLateWish);
        darkOverlay = findViewById(R.id.darkOverlay);

        checkTimeAndStart();
    }

    private void checkTimeAndStart() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date target = sdf.parse(TARGET_DATE);
            long diff = target.getTime() - System.currentTimeMillis();

            if (diff > 0) {
                startCountdown(diff);
            } else {
                showLateScreen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCountdown(long duration) {
        tvCountdown.setVisibility(View.VISIBLE);
        tvLateMessage.setVisibility(View.GONE);
        btnLateWish.setVisibility(View.GONE);

        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millis) {
                long days = TimeUnit.MILLISECONDS.toDays(millis);
                long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
                long mins = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
                long secs = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

                String time = String.format(Locale.getDefault(), "%02d Days %02d:%02d:%02d", days, hours, mins, secs);
                tvCountdown.setText(time);
            }

            @Override
            public void onFinish() {
                tvCountdown.setVisibility(View.GONE);
                startFadeToBlack();
            }
        }.start();
    }

    private void showLateScreen() {
        tvCountdown.setVisibility(View.GONE);
        tvLateMessage.setVisibility(View.VISIBLE);
        btnLateWish.setVisibility(View.VISIBLE);

        tvLateMessage.setText(getString(R.string.late_message));
        
        btnLateWish.setOnClickListener(v -> {
            tvLateMessage.setVisibility(View.GONE);
            btnLateWish.setVisibility(View.GONE);
            startCountdown(5000);
        });
    }

    private void startFadeToBlack() {
        darkOverlay.setVisibility(View.VISIBLE);
        darkOverlay.animate().alpha(1f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgSky.setVisibility(View.GONE);
                revealFlameAndCake();
            }
        });
    }

    private void revealFlameAndCake() {
        imgFlame.setVisibility(View.VISIBLE);
        imgFlame.setAlpha(0f);
        imgFlame.animate().alpha(1f).setDuration(1500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imgCake.setVisibility(View.VISIBLE);
                imgCake.setAlpha(0f);
                imgCake.animate().alpha(1f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        transformCandle();
                    }
                });
            }
        });
    }

    private void transformCandle() {
        imgCandle20.setVisibility(View.VISIBLE);
        imgCandle20.setAlpha(0f);
        imgCandle20.animate().alpha(1f).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                enableTapToBlow();
            }
        });
    }

    private void enableTapToBlow() {
        tvTapHint.setVisibility(View.VISIBLE);
        tvTapHint.setText(getString(R.string.tap_to_blow_hint));

        imgCandle20.setOnClickListener(v -> blowOutSequence());
    }

    private void blowOutSequence() {
        imgFlame.setVisibility(View.GONE);
        tvTapHint.setVisibility(View.GONE);
        
        tvHappyBirthday.setVisibility(View.VISIBLE);
        tvHappyBirthday.setAlpha(0f);
        tvHappyBirthday.animate().alpha(1f).setDuration(1000);

        startPetalRain();
        playMusic();

        new Handler().postDelayed(this::showFinalMessage, 4000);
    }

    private void startPetalRain() {
        
    }

    private void playMusic() {
        
    }

    private void showFinalMessage() {
        darkOverlay.animate().alpha(0f).setDuration(2000);
        imgCake.animate().alpha(0.3f).setDuration(2000);
        imgCandle20.animate().alpha(0.3f).setDuration(2000);

        tvFinalMessage.setVisibility(View.VISIBLE);
        tvFinalMessage.setAlpha(0f);
        tvFinalMessage.setText(getString(R.string.late_message));
        tvFinalMessage.animate().alpha(1f).setDuration(2000);
    }
}
