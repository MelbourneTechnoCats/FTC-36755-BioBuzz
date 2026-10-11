package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;
import com.seattlesolvers.solverslib.hardware.motors.CRServoGroup;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class IntakeSubsystem extends SubsystemBase {
    private final Motor intakeMotor;
    private final CRServo leftServo;
    private final CRServo rightServo;
    private final CRServoGroup servoGroup;

    public IntakeSubsystem(
            final HardwareMap hardwareMap,
            final String motorName, final String leftServoName, final String rightServoName
    ) {
        intakeMotor = new Motor(hardwareMap, motorName, Motor.GoBILDA.RPM_117);
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        leftServo = new CRServo(hardwareMap, leftServoName);
        rightServo = new CRServo(hardwareMap, rightServoName);
        rightServo.setInverted(true);

        servoGroup = new CRServoGroup(leftServo, rightServo);
    }

    public void run(double power) {
        intakeMotor.set(power);
        servoGroup.set(power);
    }
}
