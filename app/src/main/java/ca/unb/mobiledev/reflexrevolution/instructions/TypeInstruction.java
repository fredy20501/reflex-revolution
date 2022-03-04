package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

public class TypeInstruction extends Instruction{
    private Random rand;
    private String word;
    private EditText field;
    //private ArrayList<String> wordList; //Currently unused
    private RandomAccessFile wordListFile;
    private int linesInFile;
    private final int SIZE_OF_FILE_LINE = 7;
    private final InputMethodManager keyboardDisplayManager;

    public TypeInstruction(ViewGroup layout, Callback callback) {
        super(layout, callback);
        rand = new Random();
        keyboardDisplayManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        linesInFile = 0;
        setUpRandomWordList();
    }

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
            linesInFile = (int)wordListFile.length() / SIZE_OF_FILE_LINE;
        } catch(IOException e){ e.printStackTrace(); }
    }

    //Writes list of words from json file to arraylist. Currently not being used.
    /*
    private void setUpWordList(){
        try {
            wordList = new ArrayList<>();
            InputStream stream = context.getAssets().open("TypeWords.json");
            JsonReader reader = new JsonReader(new InputStreamReader(stream, "UTF-8"));
            reader.beginArray();
            while(reader.hasNext()){
                wordList.add(reader.nextString());
            }
            reader.endArray();
            reader.close();
        } catch (IOException e){ e.printStackTrace(); }
    }
    */

    @Override
    public void init() {
        super.init();
        //Get random word to type
        try {
            //For some reason, the word returned is sometimes empty, so loop until we get one that isn't
            word = "";
            while(word.isEmpty()) {
                int line = rand.nextInt(linesInFile);
                //Jump to the start of a random line and read it
                wordListFile.seek((long) line * SIZE_OF_FILE_LINE);
                word = wordListFile.readLine();
            }
        } catch (IOException e) { e.printStackTrace(); }
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
