package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.detectors.TouchDetector;

public class TypeInstruction extends Instruction{
    private Random rand;
    private String word;
    private String[] wordList;
    private InputMethodManager keyboardDisplayManager;

    public TypeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        wordList = new String[]{"word"};
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void init() {
        super.init();
        // Get random word to type
        word = wordList[rand.nextInt(wordList.length)];
    }

    @Override
    public void display() {
        addTextView(word, SMALL_TEXT_SIZE);
        addTextView("TYPE", DEFAULT_TEXT_SIZE);
        EditText textField = new EditText(context);

        //Listener that checks for text being updated
        textField.addTextChangedListener(new TextWatcher() {
            //If text equals current word, trigger success
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(word)){
                    //Hide keyboard
                    Log.d("tag", "Here");
                    keyboardDisplayManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
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
        addView(textField);

        //Select field, and force the keyboard to show
        textField.requestFocus();
        keyboardDisplayManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void enable() {}

    @Override
    public void disable() {}

    @Override
    public void timerFinished() {
        //Hide keyboard
        keyboardDisplayManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        fail();
    }
}
