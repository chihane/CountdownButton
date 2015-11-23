#CountdownButton

An Android library, provides a Button with a countdown timer.

##Samples

<img src="http://raw.github.com/mlxy/CountdownButton/master/sample/sample1.jpg" width = "480" height = "854" alt="sample1" align=center />

<img src="http://raw.github.com/mlxy/CountdownButton/master/sample/sample2.jpg" width = "480" height = "854" alt="sample2" align=center />

##Integration

Add the following line to the `dependencies` section of `build.gradle`.

    compile 'mlxy.countdownbutton:countdownbutton:1.3'
    
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

`CountdownButton` is also compatible with `Button`'s methods like:

    countdownButton.setText("Click me to count down");

Also, if you set `CountdownButton.setStartOnClick()` with `false`, you can start it when you want by:

    countdownButton.startCountdown();
    
Or cancel it at any time by:

    countdownButton.cancelCountdown();
    
**Only use `CountdownButton.setDisableOnCountdown(boolean)` when you really need it, it still have some glitches that I can't deal with.*

**Try not to mess with `View.setEnabled(boolean)`, I don't know what will happen.*

###**For further details, see `app` module.**

##Author

**mlxy**

- <http://www.cnblogs.com/chihane/>
- <chihane@yeah.net>

##License

    The MIT License (MIT)
    
    Copyright (c) 2015 mlxy
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.