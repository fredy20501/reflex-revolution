package ca.unb.mobiledev.reflexrevolution.utils;

import android.content.Context;
import android.media.MediaPlayer;

import ca.unb.mobiledev.reflexrevolution.instructions.Instruction;

public class VoiceCommandManager {

    private Context context;
    private MediaPlayer player;

    public VoiceCommandManager(Context context){ this.context = context;}

    public void playInstruction(Instruction instruction){
        player = MediaPlayer.create(context, instruction.getVoiceCommand());

        //When sound is done, release media player properly
        player.setOnCompletionListener(v -> {
            player.stop();
            player.release();
            player = null;
        });

        player.start();
    }
}
