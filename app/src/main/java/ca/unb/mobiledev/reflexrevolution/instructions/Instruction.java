package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;

public abstract class Instruction {

    // Static constants
    protected static final float DEFAULT_TEXT_SIZE = 80;
    protected static final float SMALL_TEXT_SIZE = 16;

    // Initialized constants
    protected final Context context;
    protected final ViewGroup layout;

    // Callback variables
    private final Callback callback;
    private boolean done;

    //Voice command variables
    private final Random rand;
    protected int[] voiceCommands;

    // Default constructor
    public Instruction(ViewGroup layout, Callback callback) {
        this.context = layout.getContext();
        this.layout = layout;
        this.callback = callback;
        this.done = false;
        this.rand = new Random();
        setVoiceCommands();
    }

    // Callback functions used to tell the game if success/fail
    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    // Intermediate functions to guarantee that either callback is only called once
    protected void success() {
        if (!done) {
            done = true;
            callback.onSuccess();
        }
    }
    protected void fail() {
        if (!done) {
            done = true;
            callback.onFailure();
        }
    }

    // Add a basic text view to the layout
    protected void addTextView(String label, float textSize) {
        TextView text = new TextView(context);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        text.setText(label);
        layout.addView(text);
    }

    // Initialize the state of the instruction
    // (Called before each time the instruction is used)
    public void init() { this.done = false; }

    // Get a random voice command for this instruction
    public int getVoiceCommand(){ return voiceCommands[rand.nextInt(voiceCommands.length)]; }

    // All Instructions must set their list of voice commands
    protected abstract void setVoiceCommands();

    // Add the UI elements to the layout
    public abstract void display();

    // Activate the instruction listeners (if needed)
    public abstract void enable();

    // Deactivate the instruction listeners (if needed)
    public abstract void disable();

    // Define what happens when the instruction timer runs out
    public abstract void timerFinished();
}
