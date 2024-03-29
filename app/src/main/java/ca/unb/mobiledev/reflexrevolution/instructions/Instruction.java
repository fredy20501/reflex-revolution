package ca.unb.mobiledev.reflexrevolution.instructions;

import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.res.ResourcesCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.unb.mobiledev.reflexrevolution.R;
import ca.unb.mobiledev.reflexrevolution.utils.LocalData;

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

    //Voice command variables
    protected final Random rand;
    protected Integer[] voiceCommands;
    private final SoundPool soundPool;

    // Default constructor
    public Instruction(LinearLayout layout, Callback callback) {
        this.context = layout.getContext();
        this.layout = layout;
        this.callback = callback;
        this.done = false;
        this.rand = new Random();
        this.primaryContext = new ContextThemeWrapper(context, R.style.instructionPrimary);
        this.secondaryContext = new ContextThemeWrapper(context, R.style.instructionSecondary);
        LocalData.initialize(context);

        // Create sound pool
        AudioAttributes audioAttributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool
                .Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
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
    protected void addTextView(String label, LabelType labelType) {
        ContextThemeWrapper styledContext;
        switch (labelType) {
            default:
            case PRIMARY: styledContext = primaryContext; break;
            case SECONDARY: styledContext = secondaryContext; break;
        }
        TextView text = new TextView(styledContext);
        text.setText(label);
        text.setGravity(Gravity.CENTER);
        text.setTypeface(getInstructionTypeFace());
        layout.addView(text);
    }
    protected Typeface getInstructionTypeFace() {
        return ResourcesCompat.getFont(context, R.font.rocknroll_one);
    }

    // Return the voice command resources using the given prefix
    protected Integer[] getVoiceCommands(String prefix) {
        List<Integer> idList = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            try {
                if (field.getName().contains(prefix)) {
                    idList.add(field.getInt(null));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return idList.toArray(new Integer[0]);
    }

    // Play a random voice command associated with this instruction
    public void playVoiceCommand() {
        // Do nothing if don't have any voiceCommands set
        if (voiceCommands == null || voiceCommands.length == 0) return;

        // Setup a random voice command
        int randomSoundFileID = voiceCommands[rand.nextInt(voiceCommands.length)];
        float voiceVolume = LocalData.getValue(LocalData.Value.VOLUME_VOICE)/100f;
        int sound = soundPool.load(context, randomSoundFileID, 1);
        soundPool.setOnLoadCompleteListener((soundPool, i, i1) ->
                soundPool.play(sound, voiceVolume, voiceVolume, 0, 0, 1)
        );
    }

    // Initialize the state of the instruction
    // (Called before each time the instruction is used)
    public void init() { this.done = false; }

    // Second init for practice mode
    public void init(boolean success) { init(); }

    // Add the UI elements to the layout
    public abstract void display();

    // Return the minimum duration for this instruction
    public abstract int getMinDuration();

    // Activate the instruction listeners (if needed)
    public abstract void enable();

    // Deactivate the instruction listeners (if needed)
    public abstract void disable();

    // Define what happens when the instruction timer runs out
    public abstract void timerFinished();
}
