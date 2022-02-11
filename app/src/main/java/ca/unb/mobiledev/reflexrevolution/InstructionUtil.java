package ca.unb.mobiledev.reflexrevolution;


import java.util.ArrayList;

public class InstructionUtil {

    public static ArrayList<Instruction> createInstructions(GameMode mode){
        ArrayList<Instruction> instructions = new ArrayList<>();
        //Construct list of instructions based on gamemode
        switch(mode){
            case BASIC:
                instructions.add(Instruction.BUTTON);
                break;

            default:
                break;
        }
        return instructions;
    }
}


