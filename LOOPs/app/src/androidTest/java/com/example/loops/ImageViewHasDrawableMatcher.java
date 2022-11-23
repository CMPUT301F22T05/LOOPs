package com.example.loops;

import androidx.test.espresso.matcher.BoundedDiagnosingMatcher;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/*
 * https://github.com/android/testing-samples/blob/main/ui/espresso/IntentsAdvancedSample/app/src/androidTest/java/com/example/android/testing/espresso/intents/AdvancedSample/ImageViewHasDrawableMatcher.java
 * Date accessed:2022-11-19
 */


/**
 * A Matcher for Espresso that checks if an ImageView has a drawable applied to it.
 */
public class ImageViewHasDrawableMatcher {

    public static Matcher<View> hasDrawable() {
        return new BoundedDiagnosingMatcher<View, ImageView>(ImageView.class) {
            @Override
            protected void describeMoreTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            protected boolean matchesSafely(ImageView imageView, Description mismatchDescription) {
                return imageView.getDrawable() != null;
            }
        };
    }
}
