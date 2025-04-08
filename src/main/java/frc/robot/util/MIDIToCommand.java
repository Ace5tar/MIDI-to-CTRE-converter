package frc.robot.util;

import static edu.wpi.first.wpilibj2.command.Commands.*;
import javax.sound.midi.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Music;


public class MIDIToCommand {

    private final Music musicController;

    /**
     * System for converting midi data into wpilib commands with CTRE MusicNote control requests.
     * @param musicSystem System used to directly control the motors.
     */
    public MIDIToCommand(Music musicSystem) {
        musicController = musicSystem;
    }
    
    /**
     * Converts a midi sequence to a command that follows the structure ...setFrequency() - waitSeconds()... for each note in the midi file. Only supports playing one note at a time.
     * 
     * @param midiSequence The squence to convert from.
     * @return A command to call that plays frequencies on all of the motors
     */
    public Command MidiSequenceToCommand(Sequence midiSequence) {

        Command musicCommand = none();
        double lastTime = 0;
        int lastNote = -1;
        double ticksPerSecond = midiSequence.getTickLength() / (midiSequence.getMicrosecondLength() / 1000000);

        for (Track track : midiSequence.getTracks()) {
            for (int i = 0; i < track.size(); ++i) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        musicCommand = musicCommand.andThen(waitSeconds((event.getTick() / ticksPerSecond) - lastTime)).andThen(runOnce(() -> musicController.setFrequency(noteToFrequency(sm.getData1()))));
                        lastTime = event.getTick() / ticksPerSecond - lastTime;
                        lastNote = sm.getData1();
                    }
                    if (sm.getCommand() == ShortMessage.NOTE_OFF && sm.getData1() == lastNote) {
                        musicCommand = musicCommand.andThen(waitSeconds((event.getTick() / ticksPerSecond) - lastTime)).andThen(runOnce(() -> musicController.setFrequency(0)));
                        lastTime = event.getTick() / ticksPerSecond - lastTime;
                    }
                }
                
                
            }
        }


        return musicCommand;

    
    }

    /**
     * Converts a given MIDI integer note value to its corresponding frequency. Assumes that note A4 (integer value 69) has a frequency of 440 Hz.
     * @param note MIDI note value to convert from.
     * @return Frequency to play in order to play a note.
     */
    private double noteToFrequency(int note) {
        return Math.pow(2, (note - 69)/12) * 440;
    }


}
