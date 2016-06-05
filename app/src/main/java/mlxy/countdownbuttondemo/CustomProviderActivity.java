package mlxy.countdownbuttondemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import mlxy.countdownbutton.CountdownButton;

public class CustomProviderActivity extends AppCompatActivity {
    private mlxy.countdownbutton.CountdownButton countdownButton;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, CustomProviderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_provider);
        this.countdownButton = (CountdownButton) findViewById(R.id.countdown_button);

        countdownButton.setCountdownProvider(new CountdownButton.IProvider() {
            @NonNull
            @Override
            public CharSequence getCountdownText(long millisUntilFinished, int timeUnit) {
                SpannableString spannableString = new SpannableString("自定义计数器文本" + millisUntilFinished / 1000);

                int index = (int) (millisUntilFinished / 1000 % spannableString.length());
                spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CustomProviderActivity.this, R.color.colorPrimary)), index, index + 1, Spanned.SPAN_USER);
                int index2 = (int) (millisUntilFinished / 500 % spannableString.length());
                spannableString.setSpan(new BackgroundColorSpan(ContextCompat.getColor(CustomProviderActivity.this, R.color.colorAccent)), index2, index2 + 1, Spanned.SPAN_USER);

                return spannableString;
            }
        });
    }
}
