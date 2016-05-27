package mlxy.countdownbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CountdownButton extends Button implements View.OnClickListener {
    public static final int TIMEUNIT_MILLISECOND = 0;
    public static final int TIMEUNIT_SECOND = 1;

    private static final String PREFS_KEY_IS_COUNTING_DOWN = "CountdownButton_is_counting_down";
    private static final String PREFS_KEY_TERMINAL_TIME = "CountdownButton_terminal_time";

    private Config initialConfig;
    private Config config;
    private CountdownTimer timer;

    private OnCountingDownListener listener;

    private boolean isCountingDown = false;

    public CountdownButton(Context context) { this(context, null); }
    public CountdownButton(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public CountdownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountdownButton);
        initConfig(a);
        restoreState();
    }

    private void initConfig(TypedArray typedArray) {
        config = new Config();
        initialConfig = new Config();

        if (typedArray != null) {
            config.textNormal = getText().toString();
            config.prefix = typedArray.getString(R.styleable.CountdownButton_prefix);
            if (config.prefix == null) config.prefix = Config.DEFAULT_PREFIX;
            config.suffix = typedArray.getString(R.styleable.CountdownButton_suffix);
            if (config.suffix == null) config.suffix = Config.DEFAULT_SUFFIX;

            config.keepCountingDownInBackground = typedArray.getBoolean(R.styleable.CountdownButton_keepCountingDownInBackground, Config.DEFAULT_KEEP_COUNTING_DOWN_IN_BACKGROUND);
            config.disableOnCountdown = typedArray.getBoolean(R.styleable.CountdownButton_disableOnCountdown, Config.DEFAULT_DISABLE_ON_COUNTDOWN);
            config.startOnClick = typedArray.getBoolean(R.styleable.CountdownButton_startOnClick, Config.DEFAULT_START_ON_CLICK);
            config.timeUnit = typedArray.getInt(R.styleable.CountdownButton_timeUnit, Config.DEFAULT_TIMEUNIT);
            config.interval = typedArray.getInt(R.styleable.CountdownButton_interval, Config.DEFAULT_INTERVAL);

//            config.countdown = typedArray.getInt(R.styleable.CountdownButton_countdown, Config.DEFAULT_COUNTDOWN);
            String countdownString = typedArray.getString(R.styleable.CountdownButton_countdown);
            config.countdown = countdownString != null ? Long.valueOf(countdownString) : Config.DEFAULT_COUNTDOWN;

            typedArray.recycle();

            initialConfig = config.clone();
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

    private void saveState(boolean isCountingDown, long terminalTime) {
        Prefs.put(getContext(), PREFS_KEY_IS_COUNTING_DOWN, isCountingDown);
        Prefs.put(getContext(), PREFS_KEY_TERMINAL_TIME, terminalTime);
    }

    private void restoreState() {
        if (!config.keepCountingDownInBackground) return;

        boolean isCountingDown = Prefs.get(getContext(), PREFS_KEY_IS_COUNTING_DOWN, false);
        if (isCountingDown) {

            long terminalTime = Prefs.get(getContext(), PREFS_KEY_TERMINAL_TIME, -1L);
            if (terminalTime != -1L) {

                long timeRemain = terminalTime - System.currentTimeMillis();
                if (timeRemain > 0) {

                    config.countdown = timeRemain;

                    switch (config.timeUnit) {
                        case TIMEUNIT_MILLISECOND:
                            config.countdown = timeRemain;
                            break;
                        case TIMEUNIT_SECOND:
                            config.countdown = timeRemain / 1000;
                            break;
                    }

                    startCountdown();
                }
            }
        }
    }

    private void clearState() {
        Prefs.delete(getContext(), PREFS_KEY_IS_COUNTING_DOWN);
        Prefs.delete(getContext(), PREFS_KEY_TERMINAL_TIME);
    }

    public void startCountdown() {
        if (config.disableOnCountdown) {
            setEnabled(false);
        }

        if (timer != null) {
            timer.cancel();
        }

        // To be compatible with setText(CharSequence) (which can't be overwritten).
        config.textNormal = getText();

        long countdown = Config.DEFAULT_COUNTDOWN;
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

        isCountingDown = true;
        saveState(true, System.currentTimeMillis() + countdown);
    }

    public void cancelCountdown() {
        if (timer != null) {
            timer.cancel();
        }
        initialize();
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

    private void initialize() {
        updateText(0);
        setEnabled(true);
        clearState();
        isCountingDown = false;
        config = initialConfig.clone();
    }

    class CountdownTimer extends CountDownTimer {
        private final long countdown;

        public CountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            countdown = millisInFuture;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (listener != null) {
                listener.onCountingDown(millisUntilFinished, countdown);
            }
            updateText(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            if (listener != null) {
                listener.onCountingDown(0, countdown);
            }
            initialize();
        }
    }

    public interface OnCountingDownListener {
        void onCountingDown(long millisUntilFinished, long millisTotal);
    }

    static class Config implements Cloneable {
        public static final CharSequence DEFAULT_TEXT = "";
        public static final CharSequence DEFAULT_PREFIX = "";
        public static final CharSequence DEFAULT_SUFFIX = "";
        public static final boolean DEFAULT_KEEP_COUNTING_DOWN_IN_BACKGROUND = false;
        public static final boolean DEFAULT_DISABLE_ON_COUNTDOWN = true;
        public static final boolean DEFAULT_START_ON_CLICK = true;
        public static final long DEFAULT_COUNTDOWN = 60 * 1000;
        public static final int DEFAULT_TIMEUNIT = TIMEUNIT_MILLISECOND;
        public static final int DEFAULT_INTERVAL = 1;

        public Config() {
            init();
        }

        void init() {
            textNormal = DEFAULT_TEXT;
            prefix = DEFAULT_PREFIX;
            suffix = DEFAULT_SUFFIX;
            keepCountingDownInBackground = DEFAULT_KEEP_COUNTING_DOWN_IN_BACKGROUND;
            disableOnCountdown = DEFAULT_DISABLE_ON_COUNTDOWN;
            startOnClick = DEFAULT_START_ON_CLICK;
            countdown = DEFAULT_COUNTDOWN;
            timeUnit = DEFAULT_TIMEUNIT;
            interval = DEFAULT_INTERVAL;
        }

        CharSequence textNormal;
        CharSequence prefix;
        CharSequence suffix;
        boolean keepCountingDownInBackground;
        boolean disableOnCountdown;
        boolean startOnClick;
        long countdown;
        int timeUnit;
        int interval;
//        public DateFormat


        @Override
        public Config clone() {
            Config config = null;

            try {
                config = (Config) super.clone();
                config.textNormal = textNormal.subSequence(0, textNormal.length());
                config.prefix = prefix.subSequence(0, prefix.length());
                config.suffix = suffix.subSequence(0, suffix.length());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            return config;
        }
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

    /* Does't work very well. */
//    public void setKeepCountingDownInBackground(boolean keepCountingDownInBackground) {
//        config.keepCountingDownInBackground = keepCountingDownInBackground;
//    }

    public boolean keepCountingDownInBackground() {
        return config.keepCountingDownInBackground;
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

    public void setCountdown(long countdown) {
        config.countdown = countdown;
    }

    public long getCountdown() {
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

    public boolean isCountingDown() {
        return isCountingDown;
    }

    public void setOnCountingDownListener(OnCountingDownListener listener) {
        this.listener = listener;
    }

    public OnCountingDownListener getOnCountingDownListener() {
        return listener;
    }
    /*----------------------Getters&Setters----------------------*/
}
