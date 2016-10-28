#CountdownButton

An Android library, provides a Button with a countdown timer.

##Samples

<img src="http://raw.github.com/mlxy/CountdownButton/master/sample/sample1.jpg" width = "480" height = "854" alt="sample1" align=center />

<img src="http://raw.github.com/mlxy/CountdownButton/master/sample/sample2.jpg" width = "480" height = "854" alt="sample2" align=center />

![device-2016-06-05-135126.png](/sample/device-2016-06-05-135126.png)

##Integration

Add the following line to the `dependencies` section of `build.gradle`.

    compile 'mlxy.countdownbutton:countdownbutton:1.5'
    
##Use XML Attributes

In the root element of your layout file (like `activity_main.xml`), declare the namespace:

    <XXXLayout
        ......
        xmlns:app="http://schemas.android.com/apk/res-auto" <!-- this line -->
        ......>
        
Here are several attributes supported in current version:

    <mlxy.countdownbutton.CountdownButton
        app:countdown="10"              		<!-- total countdown time -->
        app:disableOnCountdown="true"   		<!-- if the button will be disabled when countdown starts -->
        app:interval="1"                		<!-- interval between CountdownButton updates its text -->
        app:prefix="Retry in "          		<!-- prefix of the button text when counting down -->
        app:startOnClick="true"         		<!-- if countdown starts right after button clicked -->
        app:suffix=" second(s)"         		<!-- suffix of the button text when counting down -->
		app:keepCountingDownInBackground="true" <!-- to avoid sms bomb etc. -->
		app:identifier="button_register"        <!-- for saving background countdown status separately, see issue #8 -->
        app:timeUnit="second" />        		<!-- yes, time unit -->
        
##In code

Instantiate a instance by `findViewById()` or Constructor and configure with these APIs:

    countdownButton.setPrefix("I'm prefix");
    countdownButton.setSuffix("I'm suffix");
    countdownButton.setTimeUnit(CountdownButton.TIMEUNIT_MILLISECOND);
    countdownButton.setCountdown(10 * 1000);
    countdownButton.setInterval(1);
    countdownButton.setStartOnClick(true);
    countdownButton.setDisableOnCountdown(true);
    countdownButton.setIdentifier("button_forgot_password")

`CountdownButton` is also compatible with `Button`'s methods like:

    countdownButton.setText("Click me to count down");

Also, if you set `CountdownButton.setStartOnClick()` with `false`, you can start it when you want by:

    countdownButton.startCountdown();
    
Or cancel it at any time by:

    countdownButton.cancelCountdown();
    
**Only use `CountdownButton.setDisableOnCountdown(boolean)` when you really need it, it still have some glitches that I can't deal with.*

**Try not to mess with `View.setEnabled(boolean)`, I don't know what will happen.*

##Custom Countdown Text Provider

```java
        countdownButton.setCountdownProvider(new CountdownButton.IProvider() {
            @NonNull
            @Override
            public CharSequence getCountdownText(long millisUntilFinished, int timeUnit) {
                // custom code
            }
        });
```

###**For further details, see `app` module.**

##Author

**mlxy**

- <http://chihane.in>
- <chihane@yeah.net>
- <http://weibo.com/chihaneh>

##License

[The MIT License (MIT)](http://chihane.in/license/)