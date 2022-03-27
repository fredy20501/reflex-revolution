package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.view.ContextThemeWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;

public class TypeInstruction extends Instruction{
    private final Random rand;
    private String word;
    private EditText field;
    private RandomAccessFile wordListFile;
    private long fileLength;
    private final int SIZE_OF_FILE_LINE = 7;
    private final InputMethodManager keyboardDisplayManager;
    private final ContextThemeWrapper keyboardInputContext;
    private final ViewGroup.LayoutParams wrapContent;

    public TypeInstruction(LinearLayout layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardInputContext = new ContextThemeWrapper(context, R.style.keyboardInput);
        wrapContent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fileLength = 0;
        setUpRandomWordList();
    }

    //Transfer the text file containing all the words to a RandomAccessFile
    //Adapted from https://stackoverflow.com/a/48865091
    private void setUpRandomWordList(){
        try {
            File outputDir = context.getCacheDir();
            File destFile = new File(outputDir, "TypeWords.txt");
            destFile.delete();
            OutputStream dest = new FileOutputStream(destFile, true);
            InputStream src = context.getAssets().open("TypeWords.txt", AssetManager.ACCESS_STREAMING);
            byte[] buff = new byte[100 * 1024];
            for (; ; ) {
                int cnt = src.read(buff);
                if (cnt <= 0)
                    break;
                dest.write(buff, 0, cnt);
            }
            dest.flush();
            dest.close();
            wordListFile = new RandomAccessFile(destFile, "r");

            //Get number of lines in file
            fileLength = wordListFile.length();
        } catch(IOException e){ e.printStackTrace(); }
    }

    @Override
    public void init() {
        super.init();
        //Get random word to type
        try {
            //Subtracting file line size outside random so there's an equal chance of getting the first string in the list as the others
            int pos = (rand.nextInt((int)fileLength) - SIZE_OF_FILE_LINE);
            if(pos < 0) pos = 0;
            wordListFile.seek(pos);
            if(pos != 0) wordListFile.readLine();
            word = wordListFile.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            word = "TESTS";
        }
    }

    @Override
    public void display() {
        // Align the labels to the top of the screen to make room for the keyboard
        layout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

        addTextView("TYPE", LabelType.PRIMARY);
        addTextView(word, LabelType.SECONDARY);
        field = new EditText(keyboardInputContext);
        field.setTypeface(getInstructionTypeFace());
        field.setLayoutParams(wrapContent);
        field.setEms(5);
        field.setAllCaps(true);
        field.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //Listener that checks for text being updated
        field.addTextChangedListener(new TextWatcher() {
            //If text equals current word, trigger success
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().toUpperCase().equals(word)){
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
        fail();
    }
}
