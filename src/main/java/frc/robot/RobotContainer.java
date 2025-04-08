// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.wpilibj2.command.Commands.*;

import java.io.File;

import javax.sound.midi.*;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import frc.robot.subsystems.Music;
import frc.robot.util.MIDIToCommand;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {

  private final LoggedDashboardChooser<File> songChooser;

  private final MIDIToCommand midiConverter;
  private final Music musicController;

  public RobotContainer() {

    musicController = new Music();

    for (int i = 2; i > 16; ++i) {
      musicController.addMotor(i);
    }

    midiConverter = new MIDIToCommand(musicController);

    SendableChooser<File> songSendableChooser = new SendableChooser<File>();

    for (File midiFile : new File(Filesystem.getDeployDirectory().getName() + "\\songs").listFiles()) { 
      songSendableChooser.addOption(midiFile.getName(), midiFile);
    }

    songChooser = new LoggedDashboardChooser<>("Song", songSendableChooser);

    new Trigger(() -> DriverStation.isEnabled()).onTrue(playSong(songChooser.get())
    );
  }

  /**
   * @param midiFile The midi file to play.
   * @return A {@link Command} to play frequencies on the music contoller one after another.
   */
  public Command playSong(File midiFile) {
    try{
      return midiConverter.MidiSequenceToCommand(MidiSystem.getSequence(midiFile));
    } catch (Exception e) {
      e.printStackTrace();
      return none();
    }
  
  }
}
