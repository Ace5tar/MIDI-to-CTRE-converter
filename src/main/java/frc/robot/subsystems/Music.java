package frc.robot.subsystems;

import java.util.ArrayList;
import com.ctre.phoenix6.controls.MusicTone;
import com.ctre.phoenix6.hardware.TalonFX;


public class Music {

    /** List of motors to play music on.  */
    private final ArrayList<TalonFX> motors = new ArrayList<TalonFX>();

    /** General music controller class. Allows to set the frequency played by multiple motors at a time. */
    public Music() {}


    /** 
     * Set the frequency of all motors on this motor controller.
     * @param freq Frequency to set the motors to.
    */
    public void setFrequency(double freq) {
        for (final TalonFX motor : motors) {
            motor.setControl(new MusicTone(freq));
        }
    }

    /** 
     * Add a usable motor to list of motors this controller can use.
     * @param deviceId ID of the motor to add.
     */
    public void addMotor(int deviceId) {
        motors.add(new TalonFX(deviceId));
    }
}
