package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class Instruction {

    protected static final float DEFAULT_TEXT_SIZE = 80;
    protected static final float SMALL_TEXT_SIZE = 16;

    protected Context context;
    protected Callback callback;
    protected ViewGroup layout;
    protected boolean done;

    public Instruction(ViewGroup layout, Callback callback) {
        this.context = layout.getContext();
        this.layout = layout;
        this.callback = callback;
        this.done = false;
        setup();
    }

    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    public void addTextView(String label, float textSize) {
        TextView text = new TextView(context);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        text.setText(label);
        layout.addView(text);
    }

    public void init() { this.done = false; }

    protected abstract void setup();

    public abstract void display();

    public abstract void enable();

    public abstract void disable();

    public abstract void timerFinished();
}
