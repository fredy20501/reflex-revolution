package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class DialInstruction extends Instruction{
    private Random rand;
    private String number;
    private EditText field;
    private InputMethodManager keyboardDisplayManager;

    public DialInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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
        addTextView(number, SMALL_TEXT_SIZE);
        addTextView("DIAL", DEFAULT_TEXT_SIZE);
        field = new EditText(context);
        field.setInputType(InputType.TYPE_CLASS_PHONE);

        //Listener that checks for text being updated
        field.addTextChangedListener(new TextWatcher() {
            //If text equals current word, trigger success
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(number.replace("-", ""))){
                    //Hide keyboard
                    disable();
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
        addView(field);
        enable();
    }

    //Select field and force the keyboard to show
    @Override
    public void enable() {
        field.requestFocus();
        keyboardDisplayManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    //Close keyboard
    @Override
    public void disable() {
        keyboardDisplayManager.hideSoftInputFromWindow(field.getWindowToken(), 0);
    }

    @Override
    public void timerFinished() {
        //Hide keyboard
        disable();
        fail();
    }
}
