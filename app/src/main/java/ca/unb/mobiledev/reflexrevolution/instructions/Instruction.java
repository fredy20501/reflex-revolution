package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import ca.unb.mobiledev.reflexrevolution.R;

public abstract class Instruction {

    // Static constants
    protected enum LabelType { PRIMARY, SECONDARY }
    protected final ContextThemeWrapper primaryContext;
    protected final ContextThemeWrapper secondaryContext;

    // Initialized constants
    protected final Context context;
    protected final LinearLayout layout;

    // Callback variables
    private final Callback callback;
    private boolean done;

    // Default constructor
    public Instruction(LinearLayout layout, Callback callback) {
        this.context = layout.getContext();
        this.layout = layout;
        this.callback = callback;
        this.done = false;
        this.primaryContext = new ContextThemeWrapper(context, R.style.instructionPrimary);
        this.secondaryContext = new ContextThemeWrapper(context, R.style.instructionSecondary);
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
    protected void addTextView(String label, LabelType labelType, int gravity) {
        ContextThemeWrapper styledContext;
        switch (labelType) {
            default:
            case PRIMARY: styledContext = primaryContext; break;
            case SECONDARY: styledContext = secondaryContext; break;
        }
        TextView text = new TextView(styledContext);
        text.setText(label);
        text.setGravity(gravity);
        layout.addView(text);
    }
    protected void addTextView(String label, LabelType labelType) {
        addTextView(label, labelType, Gravity.CENTER);
    }

    // Initialize the state of the instruction
    // (Called before each time the instruction is used)
    public void init() { this.done = false; }

    // Add the UI elements to the layout
    public abstract void display();

    // Activate the instruction listeners (if needed)
    public abstract void enable();

    // Deactivate the instruction listeners (if needed)
    public abstract void disable();

    // Define what happens when the instruction timer runs out
    public abstract void timerFinished();
}
