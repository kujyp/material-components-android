/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.textfield;

import static com.google.android.material.testutils.TestUtilsActions.setCompoundDrawablesRelative;
import static com.google.android.material.testutils.TestUtilsMatchers.withCompoundDrawable;
import static com.google.android.material.testutils.TextInputLayoutActions.clickIcon;
import static com.google.android.material.testutils.TextInputLayoutActions.setCustomEndIconContent;
import static com.google.android.material.testutils.TextInputLayoutActions.setEndIconMode;
import static com.google.android.material.testutils.TextInputLayoutActions.setEndIconOnClickListener;
import static com.google.android.material.testutils.TextInputLayoutActions.setStartIcon;
import static com.google.android.material.testutils.TextInputLayoutActions.setStartIconContentDescription;
import static com.google.android.material.testutils.TextInputLayoutActions.setStartIconOnClickListener;
import static com.google.android.material.testutils.TextInputLayoutActions.setStartIconTintList;
import static com.google.android.material.testutils.TextInputLayoutActions.setTransformationMethod;
import static com.google.android.material.testutils.TextInputLayoutMatchers.doesNotShowEndIcon;
import static com.google.android.material.testutils.TextInputLayoutMatchers.doesNotShowStartIcon;
import static com.google.android.material.testutils.TextInputLayoutMatchers.endIconHasContentDescription;
import static com.google.android.material.testutils.TextInputLayoutMatchers.endIconIsChecked;
import static com.google.android.material.testutils.TextInputLayoutMatchers.endIconIsNotChecked;
import static com.google.android.material.testutils.TextInputLayoutMatchers.showsEndIcon;
import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.AccessibilityChecks.accessibilityAssertion;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.google.android.material.testapp.R;
import com.google.android.material.testapp.TextInputLayoutWithIconsActivity;
import com.google.android.material.testapp.base.RecreatableAppCompatActivity;
import com.google.android.material.testutils.ActivityUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.KeyEvent;
import android.widget.EditText;
import androidx.test.espresso.ViewAssertion;
import androidx.test.filters.LargeTest;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class TextInputLayoutIconsTest {
  @Rule
  public final ActivityTestRule<TextInputLayoutWithIconsActivity> activityTestRule =
      new ActivityTestRule<>(TextInputLayoutWithIconsActivity.class);

  private static final String INPUT_TEXT = "Random input text";

  @Test
  public void testSetPasswordToggleProgrammatically() {
    // Set edit text input type to be password
    onView(withId(R.id.textinput_no_icon))
        .perform(setTransformationMethod(PasswordTransformationMethod.getInstance()));
    // Set end icon as the password toggle
    onView(withId(R.id.textinput_no_icon))
        .perform(setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE));

    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputLayout =
        activity.findViewById(R.id.textinput_no_icon);

    // Assert the end icon is the password toggle icon
    assertEquals(TextInputLayout.END_ICON_PASSWORD_TOGGLE, textInputLayout.getEndIconMode());
  }

  @Test
  public void testPasswordToggleClick() {
    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(typeText(INPUT_TEXT));

    final Activity activity = activityTestRule.getActivity();
    final EditText textInput =
        activity.findViewById(R.id.textinput_edittext_pwd);

    // Assert that the password is disguised
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());

    // Now click the toggle button
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));

    // And assert that the password is not disguised
    assertEquals(INPUT_TEXT, textInput.getLayout().getText().toString());
  }

  @Test
  public void testPasswordToggleDisable() {
    final Activity activity = activityTestRule.getActivity();
    final EditText textInput =
        activity.findViewById(R.id.textinput_edittext_pwd);

    // Set some text on the EditText
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(typeText(INPUT_TEXT));
    // Assert that the password is disguised
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());

    // Disable the password toggle
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_NONE));

    // Check that the password toggle view is not visible
    onView(withId(R.id.textinput_password))
        .check(matches(doesNotShowEndIcon()));
    // ...and that the password is disguised still
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());
  }

  @Test
  public void testPasswordToggleDisableWhenVisible() {
    final Activity activity = activityTestRule.getActivity();
    final EditText textInput =
        activity.findViewById(R.id.textinput_edittext_pwd);

    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(typeText(INPUT_TEXT));
    // Assert that the password is disguised
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());

    // Now click the toggle button
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));
    // Disable the password toggle
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_NONE));

    // Check that the password is disguised again
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());
  }

  @Test
  public void testPasswordToggleIsHiddenAfterReenable() {
    final Activity activity = activityTestRule.getActivity();
    final EditText textInput =
        activity.findViewById(R.id.textinput_edittext_pwd);

    // Type some text on the EditText and then click the toggle button
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(typeText(INPUT_TEXT));
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));

    // Set end icon to none, and then set it to be the password toggle
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_NONE))
        .perform(setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE));

    // Check that the password is disguised and the toggle button reflects the same state
    assertNotEquals(INPUT_TEXT, textInput.getLayout().getText().toString());
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));
  }

  @Test
  public void testPasswordToggleChangesWithTransformationMethod() {
    // Assert password toggle is not checked.
    onView(withId(R.id.textinput_password)).check(matches(endIconIsNotChecked()));

    // Change the edit text transformation method to null.
    onView(withId(R.id.textinput_password)).perform(setTransformationMethod(null));

    // Assert password toggle is now checked.
    onView(withId(R.id.textinput_password)).check(matches(endIconIsChecked()));
  }

  @Test
  public void testPasswordToggleIsCheckedIfTransformationMethodNotPassword() {
    // Set end icon as the password toggle
    onView(withId(R.id.textinput_no_icon))
        .perform(setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE));

    // Assert password toggle is not checked.
    onView(withId(R.id.textinput_no_icon)).check(matches(endIconIsChecked()));
  }

  @Test
  public void testPasswordToggleHasDefaultContentDescription() {
    // Check that the TextInputLayout says that it has a content description and that the
    // underlying toggle has content description as well
    onView(withId(R.id.textinput_password)).check(matches(endIconHasContentDescription()));
  }

  /**
   * Simple test that uses AccessibilityChecks to check that the password toggle icon is
   * 'accessible'.
   */
  @Test
  public void testPasswordToggleIsAccessible() {
    onView(
            allOf(
                withId(R.id.text_input_end_icon), isDescendantOfA(withId(R.id.textinput_password))))
        .check(accessibilityAssertion());
  }

  @Test
  public void testFocusMovesToEditTextWithPasswordEnabled() {
    // Focus the preceding EditText
    onView(withId(R.id.textinput_edittext_no_icon)).perform(click()).check(matches(hasFocus()));

    // Then send a TAB to focus the next view
    getInstrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_TAB);

    // And check that the EditText is focused
    onView(withId(R.id.textinput_edittext_pwd)).check(matches(hasFocus()));
  }

  @Test
  @LargeTest
  public void testSaveAndRestorePasswordVisibility() throws Throwable {
    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_pwd)).perform(typeText(INPUT_TEXT));
    onView(withId(R.id.textinput_password)).check(isPasswordToggledVisible(false));

    // Toggle password to be shown as plain text
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));
    onView(withId(R.id.textinput_password)).check(isPasswordToggledVisible(true));

    RecreatableAppCompatActivity activity = activityTestRule.getActivity();
    ActivityUtils.recreateActivity(activityTestRule, activity);
    ActivityUtils.waitForExecution(activityTestRule);

    // Check that the password is still toggled to be shown as plain text
    onView(withId(R.id.textinput_password)).check(isPasswordToggledVisible(true));
  }

  @Test
  public void testSetClearTextEndIconProgrammatically() {
    // Set end icon as the clear button
    onView(withId(R.id.textinput_no_icon))
        .perform(setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT));

    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputLayout =
        activity.findViewById(R.id.textinput_no_icon);

    // Assert the end icon is the clear button icon
    assertEquals(TextInputLayout.END_ICON_CLEAR_TEXT, textInputLayout.getEndIconMode());
  }

  @Test
  public void testClearTextEndIconClick() {
    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_clear))
        .perform(typeText(INPUT_TEXT));
    final Activity activity = activityTestRule.getActivity();
    final EditText textInput =
        activity.findViewById(R.id.textinput_edittext_clear);

    // Click clear button
    onView(withId(R.id.textinput_clear)).perform(clickIcon(true));

    // Assert EditText was cleared
    assertEquals(0, textInput.getLayout().getText().length());
    // Check that the clear button view is not visible
    onView(withId(R.id.textinput_clear))
        .check(matches(doesNotShowEndIcon()));
  }

  @Test
  public void testClearTextEndIconAppears() {
    // Check that the clear button view is not visible
    onView(withId(R.id.textinput_clear))
        .check(matches(doesNotShowEndIcon()));

    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_clear))
        .perform(typeText(INPUT_TEXT));

    // Check that the clear button is visible
    onView(withId(R.id.textinput_clear))
        .check(matches(showsEndIcon()));
  }

  @Test
  public void testClearTextEndIconDisappears() {
    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_clear))
        .perform(typeText(INPUT_TEXT));

    // Delete text
    onView(withId(R.id.textinput_edittext_clear))
        .perform(clearText());

    // Check that the clear button view is not visible
    onView(withId(R.id.textinput_clear))
        .check(matches(doesNotShowEndIcon()));
  }

  @Test
  public void testClearTextEndIconHasDefaultContentDescription() {
    // Check that the TextInputLayout says that it has a content description and that the
    // underlying toggle has content description as well
    onView(withId(R.id.textinput_clear))
        .check(matches(endIconHasContentDescription()));
  }

  @Test
  public void testClearTextEndIconIsAccessible() {
    onView(
            allOf(
                withId(R.id.text_input_end_icon),
                isDescendantOfA(withId(R.id.textinput_clear))))
        .check(accessibilityAssertion());
  }

  @Test
  public void testSwitchEndIconFromPasswordToggleToClearText() {
    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputLayoutPassword =
        activity.findViewById(R.id.textinput_password);
    final TextInputLayout textInputLayoutClear =
        activity.findViewById(R.id.textinput_clear);
    String clearTextContentDesc = textInputLayoutClear.getEndIconContentDescription().toString();

    // Set end icon as the clear button on the text field that has the password toggle set
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_CLEAR_TEXT));

    // Assert the end icon mode is the clear button icon
    assertEquals(TextInputLayout.END_ICON_CLEAR_TEXT, textInputLayoutPassword.getEndIconMode());
    assertEquals(
        clearTextContentDesc, textInputLayoutPassword.getEndIconContentDescription().toString());
    // Assert the clear button is not displayed as there was no text
    onView(withId(R.id.textinput_password))
        .check(matches(doesNotShowEndIcon()));
    // Type some text on the EditText
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(typeText(INPUT_TEXT));
    // Assert icon is now showing
    onView(withId(R.id.textinput_password))
        .check(matches(showsEndIcon()));
    // Assert icon works as expected
    onView(withId(R.id.textinput_password)).perform(clickIcon(true));
    assertEquals(0, textInputLayoutPassword.getEditText().getText().length());
  }

  @Test
  public void testSetCustomEndIconProgrammatically() {
    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputCustomEndIcon =
        activity.findViewById(R.id.textinput_custom);
    String expectedContentDesc = textInputCustomEndIcon.getEndIconContentDescription().toString();
    final TextInputLayout textInputLayout =
        activity.findViewById(R.id.textinput_no_icon);

    // Set end icon mode to custom end icon
    onView(withId(R.id.textinput_no_icon))
        .perform(setEndIconMode(TextInputLayout.END_ICON_CUSTOM));
    // Set the content of the custom end icon
    onView(withId(R.id.textinput_no_icon))
        .perform(setCustomEndIconContent());

    // Assert the end icon is the custom icon
    assertEquals(TextInputLayout.END_ICON_CUSTOM, textInputLayout.getEndIconMode());
    assertEquals(expectedContentDesc, textInputLayout.getEndIconContentDescription().toString());
  }

  @Test
  public void testCustomEndIconOnClickListener() {
    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputCustomEndIcon =
        activity.findViewById(R.id.textinput_custom);
    // Set custom on click listener
    onView(withId(R.id.textinput_custom))
        .perform(
            setEndIconOnClickListener(
                v -> textInputCustomEndIcon.getEditText().setText("Test custom icon.")));

    // Click custom end icon
    onView(withId(R.id.textinput_custom)).perform(clickIcon(true));

    // Assert onClickListener worked as expected
    assertEquals("Test custom icon.", textInputCustomEndIcon.getEditText().getText().toString());
  }

  @Test
  public void testEndIconMaintainsCompoundDrawables() {
    // Set a known set of test compound drawables on the EditText
    final Drawable start = new ColorDrawable(Color.RED);
    final Drawable top = new ColorDrawable(Color.GREEN);
    final Drawable end = new ColorDrawable(Color.BLUE);
    final Drawable bottom = new ColorDrawable(Color.BLACK);
    onView(withId(R.id.textinput_edittext_pwd))
        .perform(setCompoundDrawablesRelative(start, top, end, bottom));

    // Enable the password toggle and check that the start, top and bottom drawables are
    // maintained
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE));
    onView(withId(R.id.textinput_edittext_pwd))
        .check(matches(withCompoundDrawable(0, start)))
        .check(matches(withCompoundDrawable(1, top)))
        .check(matches(not(withCompoundDrawable(2, end))))
        .check(matches(withCompoundDrawable(3, bottom)));

    // Now remove the end icon and check that all of the original compound drawables
    // are set
    onView(withId(R.id.textinput_password))
        .perform(setEndIconMode(TextInputLayout.END_ICON_NONE));
    onView(withId(R.id.textinput_edittext_pwd))
        .check(matches(withCompoundDrawable(0, start)))
        .check(matches(withCompoundDrawable(1, top)))
        .check(matches(withCompoundDrawable(2, end)))
        .check(matches(withCompoundDrawable(3, bottom)));
  }

  @Test
  public void  testSetStartIconProgrammatically() {
    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputLayout = activity.findViewById(R.id.textinput_no_icon);
    Drawable drawable = new ColorDrawable(Color.BLACK);
    String contentDesc = "Start icon";

    // Set start icon
    onView(withId(R.id.textinput_no_icon)).perform(setStartIcon(drawable));
    onView(withId(R.id.textinput_no_icon)).perform(setStartIconContentDescription(contentDesc));
    onView(withId(R.id.textinput_no_icon)).perform(setStartIconTintList(null));

    // Assert the start icon is set
    assertNotNull(textInputLayout.getStartIconDrawable());
    assertEquals(contentDesc, textInputLayout.getStartIconContentDescription().toString());
  }

  @Test
  public void testStartIconDisables() {
    // Disable the start icon
    onView(withId(R.id.textinput_starticon)).perform(setStartIcon(null));

    // Check that the start icon view is not visible
    onView(withId(R.id.textinput_starticon)).check(matches(doesNotShowStartIcon()));
  }

  @Test
  public void testStartIconOnClickListener() {
    final Activity activity = activityTestRule.getActivity();
    final TextInputLayout textInputLayout = activity.findViewById(R.id.textinput_starticon);
    // Set click listener on start icon
    onView(withId(R.id.textinput_starticon))
        .perform(
            setStartIconOnClickListener(
                v -> textInputLayout.getEditText().setText("Start icon on click")));

    // Click start icon
    onView(withId(R.id.textinput_starticon)).perform(clickIcon(false));

    // Assert OnClickListener worked as expected
    assertEquals("Start icon on click", textInputLayout.getEditText().getText().toString());
  }

  /**
   * Simple test that uses AccessibilityChecks to check that the start icon is 'accessible'.
   */
  @Test
  public void testStartIconToggleIsAccessible() {
    onView(
        allOf(
            withId(R.id.text_input_start_icon),
            isDescendantOfA(withId(R.id.textinput_starticon))))
        .check(accessibilityAssertion());
  }

  private static ViewAssertion isPasswordToggledVisible(final boolean isToggledVisible) {
    return (view, noViewFoundException) -> {
      assertTrue(view instanceof TextInputLayout);
      EditText editText = ((TextInputLayout) view).getEditText();
      TransformationMethod transformationMethod = editText.getTransformationMethod();
      if (isToggledVisible) {
        assertNull(transformationMethod);
      } else {
        assertEquals(PasswordTransformationMethod.getInstance(), transformationMethod);
      }
    };
  }
}
