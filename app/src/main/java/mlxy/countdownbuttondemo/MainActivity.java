package mlxy.countdownbuttondemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import mlxy.countdownbutton.CountdownButton;

public class MainActivity extends AppCompatActivity {
    private CountdownButton countdownButton;
    private CheckBox checkboxStartOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        countdownButton = (CountdownButton) findViewById(R.id.countdownButton);
        countdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Countdown started", Toast.LENGTH_SHORT).show();
            }
        });
        countdownButton.setOnCountingDownListener(new CountdownButton.OnCountingDownListener() {
            @Override
            public void onCountingDown(long millisUntilFinished, long countdown) {
                Log.d("countdownbutton", millisUntilFinished + "/" + countdown);
            }
        });

        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkboxStartOnClick.isChecked()) {
                    countdownButton.startCountdown();
                }
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownButton.cancelCountdown();
            }
        });

        findViewById(R.id.buttonApply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConfigs();
            }
        });

        findViewById(R.id.buttonNewActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });

        setConfigs();
    }

    private void setConfigs() {
        CheckBox checkboxDisableOnCountdown = (CheckBox) findViewById(R.id.checkboxDisableOnCountdown);
        countdownButton.setDisableOnCountdown(checkboxDisableOnCountdown.isChecked());

        checkboxStartOnClick = (CheckBox) findViewById(R.id.checkboxStartOnClick);
        countdownButton.setStartOnClick(checkboxStartOnClick.isChecked());

        RadioButton radiobuttonMillis = (RadioButton) findViewById(R.id.radiobuttonMillis);
        RadioButton radiobuttonSeconds = (RadioButton) findViewById(R.id.radiobuttonSeconds);
        if (radiobuttonMillis.isChecked()) {
            countdownButton.setTimeUnit(CountdownButton.TIMEUNIT_MILLISECOND);
        }
        if (radiobuttonSeconds.isChecked()) {
            countdownButton.setTimeUnit(CountdownButton.TIMEUNIT_SECOND);
        }

        EditText edittextText = (EditText) findViewById(R.id.edittextText);
        countdownButton.setText(edittextText.getText().toString());

        EditText edittextCountdown = (EditText) findViewById(R.id.edittextCountdown);
        countdownButton.setCountdown(Long.parseLong(edittextCountdown.getText().toString()));

        EditText edittextInterval = (EditText) findViewById(R.id.edittextInterval);
        countdownButton.setInterval(Integer.parseInt(edittextInterval.getText().toString()));

        EditText edittextPrefix = (EditText) findViewById(R.id.edittextPrefix);
        countdownButton.setPrefix(edittextPrefix.getText().toString());

        EditText edittextSuffix = (EditText) findViewById(R.id.edittextSuffix);
        countdownButton.setSuffix(edittextSuffix.getText().toString());
    }
}
