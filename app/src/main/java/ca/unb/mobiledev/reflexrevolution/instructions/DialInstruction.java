package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class DialInstruction extends Instruction{
    private final InputMethodManager keyboardDisplayManager;

    private String number;
    private EditText field;

    public DialInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        voiceCommands = getVoiceCommands("dial");
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
        if (field == null) return;
        field.requestFocus();
        keyboardDisplayManager.showSoftInput(field, InputMethodManager.SHOW_IMPLICIT);
    }

    //Close keyboard
    @Override
    public void disable() {
        if (field == null) return;
        keyboardDisplayManager.hideSoftInputFromWindow(field.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void timerFinished() {
        //Hide keyboard
        fail();
    }
}
