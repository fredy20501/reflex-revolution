package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;

public class DialInstruction extends Instruction{
    private final Random rand;
    private String number;
    private EditText field;
    private final InputMethodManager keyboardDisplayManager;
    private final ContextThemeWrapper keyboardInputContext;
    private final ViewGroup.LayoutParams wrapContent;

    public DialInstruction(LinearLayout layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardInputContext = new ContextThemeWrapper(context, R.style.keyboardInput);
        wrapContent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void init() {
        super.init();
        // Get random number to dial
        number = "";
        for(int i=0; i<3; i++) number += rand.nextInt(10);
        number += "-";
        for(int i=0; i<4; i++) number += rand.nextInt(10);
    }

    @Override
    public void display() {
        // Align the labels to the top of the screen to make room for the keyboard
        layout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        addTextView("DIAL", LabelType.PRIMARY);
        addTextView(number, LabelType.SECONDARY);
        field = new EditText(keyboardInputContext);
        field.setInputType(InputType.TYPE_CLASS_PHONE);
        field.setTypeface(getInstructionTypeFace());
        field.setLayoutParams(wrapContent);
        field.setEms(5);
        field.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //Listener that checks for text being updated
        field.addTextChangedListener(new TextWatcher() {
            //If text equals current word, trigger success
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(number.replace("-", ""))){
                    //Hide keyboard
                    success();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Auto-generated
            }
            @Override
            public void afterTextChanged(Editable s) {
                //Auto-generated
            }
        });
        layout.addView(field);
    }

    //Select field and force the keyboard to show
    @Override
    public void enable() {
        field.requestFocus();
        keyboardDisplayManager.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
    }

    //Close keyboard
    @Override
    public void disable() {
        keyboardDisplayManager.hideSoftInputFromWindow(field.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void timerFinished() {
        //Hide keyboard
        fail();
    }
}
