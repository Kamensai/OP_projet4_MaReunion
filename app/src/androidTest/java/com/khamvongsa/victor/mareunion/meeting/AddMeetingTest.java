package com.khamvongsa.victor.mareunion.meeting;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.khamvongsa.victor.mareunion.R;
import com.khamvongsa.victor.mareunion.controller.AddMeetingActivity;
import com.khamvongsa.victor.mareunion.utils.AddMeetingActivityViewAction;
import com.khamvongsa.victor.mareunion.utils.DeleteChipViewAction;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.removeGlobalAssertion;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AddMeetingTest {

    private static final String SUBJECT = "Subject";
    private static final String PARTICIPANT = "Participant";
    private static final int PARTICIPANT_COUNT = 0;


    @Rule
    public ActivityTestRule<AddMeetingActivity> mAddMeetingActivityRule =
            new ActivityTestRule<>(AddMeetingActivity.class);

    @Test
    public void writeTextSubjectMeeting() {
        // simulate user action to input some value into EditText:
        onView(withId(R.id.activity_add_meeting_editSubjectMeeting)).perform(typeText(SUBJECT));
        // Check if the value is true
        onView(withId(R.id.activity_add_meeting_editSubjectMeeting)).check(matches(withText(SUBJECT)));
    }

    @Test
    public void writeTextParticipantToAdd() {
        // simulate user action to input some value into EditText:
        onView(withId(R.id.activity_add_meeting_editText_participant)).perform(typeText(PARTICIPANT));
        // Check if the value is true
        onView(withId(R.id.activity_add_meeting_editText_participant)).check(matches(withText(PARTICIPANT)));
    }

    // TODO : A tester pour tous les champs une fois le ViewModel bien utilisé
    @Test
    public void errorMessageSubject(){
        // Cliquer pour créer une réunion alors que tous les champs sont vides
        onView(withId(R.id.activity_add_meeting_btn_createMeeting)).perform(click());
        // Vérifie si le message d'erreur s'affiche correctement
        onView(withId(R.id.activity_add_meeting_editSubjectMeeting)).check(matches(hasErrorText("Choose a subject please.")));
    }

        // TODO : Changer le nom de la méthode
    // Vérifier si le spinner est lancé, une fois les champs remplis
    @Test
    public void showSpinnerSelection() {
        final String dateChosen = "10/8/2022";
        final String startTimeChosen = "14h30m";
        final String endTimeChosen = "15h40m";
        final String roomMario = "Mario";

        // Click on DatePickerButton
        onView(withId(R.id.activity_add_meeting_btnAdd_Date)).perform(click());
        // Set Date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2022,8,10));
        // Clique sur ok pour confirmer et fermier le dialog
        onView(anyOf(withText(android.R.string.ok), withId(android.R.id.button1))).inRoot(isDialog()).perform(click());
        // vérifie que la date est bien notée
        onView(withId(R.id.activity_add_meeting_editDate)).check(matches(withText(dateChosen)));


        // Click on StartTimePickerButton
        onView(withId(R.id.activity_add_meeting_btnAdd_StartHour)).perform(click());
        //Set StartTime
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14,30));
        // Clique sur ok pour confirmer et fermier le dialog
        onView(anyOf(withText(android.R.string.ok), withId(android.R.id.button1))).inRoot(isDialog()).perform(click());
        // vérifie que le startTime est bien notée
        onView(withId(R.id.activity_add_meeting_editStartHour)).check(matches(withText(startTimeChosen)));

        // Click on EndTimePickerButton
        onView(withId(R.id.activity_add_meeting_btnAdd_EndHour)).perform(click());
        //Set EndTime
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,40));
        // Clique sur ok pour confirmer et fermier le dialog
        onView(anyOf(withText(android.R.string.ok), withId(android.R.id.button1))).inRoot(isDialog()).perform(click());
        // vérifie que le endTime est bien notée
        onView(withId(R.id.activity_add_meeting_editEndHour)).check(matches(withText(endTimeChosen)));

        //Check if the spinner shows String Item "Mario"
        onView(withId(R.id.activity_add_meeting_spinnerRoom)).check(matches(withSpinnerText(roomMario)));
    }

    // TODO : Tester l'ajout et la suppression d'un mail

    @Test
    public void showAddParticipantWithSuccess(){
        // simulate user action to input some value into EditText et on ferme le clavier
        onView(withId(R.id.activity_add_meeting_editText_participant)).perform(typeText(PARTICIPANT), closeSoftKeyboard());
        // Cliquer sur le bouton ajouter un participant
        onView(withId(R.id.activity_add_meeting_btnAdd_participant)).perform(click());
        // Vérifier si le participant s'affiche dans la liste
        onView(withId(R.id.activity_add_meeting_chipGroup)).check(matches(hasChildCount(PARTICIPANT_COUNT+1)));
        onView(withId(R.id.activity_add_meeting_chipGroup)).check(matches(withChild(withText(PARTICIPANT))));
    }

    @Test
    public void showDeleteParticipantWithSuccess(){
        // simulate user action to input some value into EditText et on ferme le clavier
        onView(withId(R.id.activity_add_meeting_editText_participant)).perform(typeText(PARTICIPANT), closeSoftKeyboard());
        // Cliquer sur le bouton ajouter un participant
        onView(withId(R.id.activity_add_meeting_btnAdd_participant)).perform(click());
        // Vérifier si le participant (chip) s'affiche dans la liste
        onView(withId(R.id.activity_add_meeting_chipGroup)).check(matches(hasChildCount(PARTICIPANT_COUNT +1)));
        onView(withId(R.id.activity_add_meeting_chipGroup)).check(matches(withChild(withText(PARTICIPANT))));
        // Supprimer le participant (chip)
        onView(withId(R.id.chip)).perform(new DeleteChipViewAction());
        // Vérifier que le participant (chip) ne s'affiche dans la liste
        onView(withId(R.id.activity_add_meeting_chipGroup)).check(matches(hasChildCount(PARTICIPANT_COUNT)));
    }

    // TODO : Tout mettre en anglais

}
