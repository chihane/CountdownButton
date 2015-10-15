package mlxy.countdownbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountdownButton extends Button implements View.OnClickListener {
    public static final int TIMEUNIT_MILLISECOND = 0;
    public static final int TIMEUNIT_SECOND = 1;

    private Config config;
    private CountdownTimer timer;

    public CountdownButton(Context context) { this(context, null); }
    public CountdownButton(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public CountdownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnClickListener(this);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountdownButton);
        initConfig(a);
    }

    private void initConfig(TypedArray typedArray) {
        config = new Config();

        if (typedArray != null) {
            config.textNormal = getText().toString();
            config.prefix = typedArray.getString(R.styleable.CountdownButton_prefix);
            if (config.prefix == null) config.prefix = Config.DEFAULT_PREFIX;
            config.suffix = typedArray.getString(R.styleable.CountdownButton_suffix);
            if (config.suffix == null) config.suffix = Config.DEFAULT_SUFFIX;

            config.disableOnCountdown = typedArray.getBoolean(R.styleable.CountdownButton_disableOnCountdown, Config.DEFAULT_DISABLE_ON_COUNTDOWN);
            config.startOnClick = typedArray.getBoolean(R.styleable.CountdownButton_startOnClick, Config.DEFAULT_START_ON_CLICK);
            config.countdown = typedArray.getInt(R.styleable.CountdownButton_countdown, Config.DEFAULT_COUNTDOWN);
            config.timeUnit = typedArray.getInt(R.styleable.CountdownButton_timeUnit, Config.DEFAULT_TIMEUNIT);
            config.interval = typedArray.getInt(R.styleable.CountdownButton_interval, Config.DEFAULT_INTERVAL);
            typedArray.recycle();
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CountdownButton.this.onClick(v);

                if (l != null) {
                    l.onClick(v);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (config.startOnClick) {
            startCountdown();
        }
    }

    public void startCountdown() {
        if (config.disableOnCountdown) {
            disable();
        }

        if (timer != null) {
            timer.cancel();
        }

        // To be compatible with setText(CharSequence) (which can't be overwritten).
        config.textNormal = getText().toString();

        int countdown = Config.DEFAULT_COUNTDOWN;
        int interval = Config.DEFAULT_INTERVAL;
        switch (config.timeUnit) {
            case TIMEUNIT_MILLISECOND:
                countdown = config.countdown;
                interval = config.interval;
                break;
            case TIMEUNIT_SECOND:
                countdown = config.countdown * 1000;
                interval = config.interval * 1000;
                break;
        }
        timer = new CountdownTimer(countdown, interval);
        timer.start();
    }

    public void cancelCountdown() {
        enable();
        if (timer != null) {
            timer.cancel();
        }
        updateText(0);
    }

    private void updateText(long millisUntilFinished) {
        if (millisUntilFinished <= 0) {
            setText(config.textNormal);
        } else {
            setText(config.prefix + format(millisUntilFinished) + config.suffix);
        }
    }

    private String format(long millisUntilFinished) {
        switch (config.timeUnit) {
            case TIMEUNIT_MILLISECOND:
                return String.valueOf(millisUntilFinished);
            case TIMEUNIT_SECOND:
                return String.valueOf(millisUntilFinished / 1000);
            default:
                return String.valueOf(millisUntilFinished);
        }
    }

    private void enable() {
        if (!isEnabled()) {
            setEnabled(true);
        }
    }

    private void disable() {
        if (isEnabled()) {
            setEnabled(false);
        }
    }

    class CountdownTimer extends CountDownTimer {
        public CountdownTimer(long millisInFuture, long countDownInterval) { super(millisInFuture, countDownInterval); }

        @Override
        public void onTick(long millisUntilFinished) {
            updateText(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            updateText(0);
            enable();
        }
    }

    static class Config {
        public static final CharSequence DEFAULT_TEXT = "";
        public static final CharSequence DEFAULT_PREFIX = "";
        public static final CharSequence DEFAULT_SUFFIX = "";
        public static final boolean DEFAULT_DISABLE_ON_COUNTDOWN = true;
        public static final boolean DEFAULT_START_ON_CLICK = true;
        public static final int DEFAULT_COUNTDOWN = 60 * 1000;
        public static final int DEFAULT_TIMEUNIT = TIMEUNIT_MILLISECOND;
        public static final int DEFAULT_INTERVAL = 1;

        public Config() {
            init();
        }

        void init() {
            textNormal = DEFAULT_TEXT;
            prefix = DEFAULT_PREFIX;
            suffix = DEFAULT_SUFFIX;
            disableOnCountdown = DEFAULT_DISABLE_ON_COUNTDOWN;
            startOnClick = DEFAULT_START_ON_CLICK;
            countdown = DEFAULT_COUNTDOWN;
            timeUnit = DEFAULT_TIMEUNIT;
            interval = DEFAULT_INTERVAL;
        }

        CharSequence textNormal;
        CharSequence prefix;
        CharSequence suffix;
        boolean disableOnCountdown;
        boolean startOnClick;
        int countdown;
        int timeUnit;
        int interval;
//        public DateFormat
    }

    /*----------------------Getters&Setters----------------------*/
    public void setPrefix(CharSequence prefix) {
        config.prefix = prefix;
    }

    public CharSequence getPrefix() {
        return config.prefix;
    }

    public void setSuffix(CharSequence suffix) {
        config.suffix = suffix;
    }

    public CharSequence getSuffix() {
        return config.suffix;
    }

    public void setDisableOnCountdown(boolean disableOnCountdown) {
        config.disableOnCountdown = disableOnCountdown;
    }

    public boolean willDisableOnCountdown() {
        return config.disableOnCountdown;
    }

    public void setStartOnClick(boolean startOnClick) {
        config.startOnClick = startOnClick;
    }

    public boolean willStartOnClick() {
        return config.startOnClick;
    }

    public void setCountdown(int countdown) {
        config.countdown = countdown;
    }

    public int getCountdown() {
        return config.countdown;
    }

    public void setTimeUnit(int timeUnit) {
        config.timeUnit = timeUnit;
    }

    public int getTimeUnit() {
        return config.timeUnit;
    }

    public void setInterval(int interval) {
        config.interval = interval;
    }

    public int getInterval() {
        return config.interval;
    }
    /*----------------------Getters&Setters----------------------*/
}
