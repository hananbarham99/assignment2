package hanan.birzeit.comp438.example.assignment2;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    private EditText Input;
    private TextView TimerView;
    private Button Set;
    private Button pause;
    private Button reset;
    private CountDownTimer mCountDownTimer;
    private boolean running;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Input = findViewById(R.id.edit_text_input);
        TimerView = findViewById(R.id.text_view_countdown);
        Set = findViewById(R.id.button_set);
        pause = findViewById(R.id.button_start_pause);
        reset = findViewById(R.id.button_reset);
        Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = Input.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity2.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity2.this, "can't be a negative number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                Input.setText("");
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                running = false;
                updateWatchInterface();
            }
        }.start();
        running = true;
        updateWatchInterface();
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        running = false;
        updateWatchInterface();
    }
    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }
    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        TimerView.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() {
        if (running) {
            Input.setVisibility(View.INVISIBLE);
            Set.setVisibility(View.INVISIBLE);
            reset.setVisibility(View.INVISIBLE);
            pause.setText("Pause");
        } else {
            Input.setVisibility(View.VISIBLE);
            Set.setVisibility(View.VISIBLE);
            pause.setText("Start");
            if (mTimeLeftInMillis < 1000) {
                pause.setVisibility(View.INVISIBLE);
            } else {
                pause.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                reset.setVisibility(View.VISIBLE);
            } else {
                reset.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", running);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        running = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        if (running) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                running = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

}