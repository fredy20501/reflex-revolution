package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.text.Editable;
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

public class TypeInstruction extends Instruction{
    private Random rand;
    private String word;
    private EditText field;
    private ArrayList<String> wordList;
    private InputMethodManager keyboardDisplayManager;

    public TypeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setUpWordList();
    }

    private void setUpWordList(){
        try {
            wordList = new ArrayList<>();
            InputStream stream = layout.getContext().getAssets().open("TypeWords.json");
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            while(reader.hasNext()){
                wordList.add(reader.nextString());
            }
            reader.endArray();
            reader.close();
        } catch (IOException e){
            Log.e("tag", "fail");
        }
    }

    @Override
    public void init() {
        super.init();
        // Get random word to type
        word = wordList.get(rand.nextInt(wordList.size()));
    }

    @Override
    public void display() {
        addTextView(word, SMALL_TEXT_SIZE);
        addTextView("TYPE", DEFAULT_TEXT_SIZE);
        field = new EditText(context);

        //Listener that checks for text being updated
        field.addTextChangedListener(new TextWatcher() {
            //If text equals current word, trigger success
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().toUpperCase().equals(word)){
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
