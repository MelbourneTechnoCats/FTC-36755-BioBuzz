package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;
import com.seattlesolvers.solverslib.hardware.motors.CRServoGroup;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.RobotConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final Motor intakeMotor;
    private CRServo leftServo;
    private CRServo rightServo;
    private CRServoGroup servoGroup;

    public IntakeSubsystem(
            final HardwareMap hardwareMap
    ) {
        intakeMotor = new Motor(hardwareMap, RobotConstants.INTAKE_MOTOR_NAME, Motor.GoBILDA.RPM_117);
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        try {
            leftServo = new CRServo(hardwareMap, RobotConstants.LEFT_INTAKE_SERVO_NAME);
            rightServo = new CRServo(hardwareMap, RobotConstants.RIGHT_INTAKE_SERVO_NAME);
            rightServo.setInverted(true);

            servoGroup = new CRServoGroup(leftServo, rightServo);
        } catch (Exception e) {

        }

    }

    public void run(double power) {
        if (servoGroup != null) {
            servoGroup.set(power);
        }
        intakeMotor.set(power);
    }
}
