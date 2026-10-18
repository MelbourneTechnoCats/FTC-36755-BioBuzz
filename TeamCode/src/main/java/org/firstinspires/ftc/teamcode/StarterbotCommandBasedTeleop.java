package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name = "Command-Based Teleop", group = "StarterBot")
public class StarterbotCommandBasedTeleop extends CommandOpMode {
    private DriveSubsystem drive;
    private IntakeSubsystem intake;

    private GamepadEx driverGamepad;

    private final String leftFrontName = "left_front_drive";
    private final String rightFrontName = "right_front_drive";
    private final String leftBackName = "left_back_drive";
    private final String rightBackName = "right_back_drive";
    private final String pinpointName = "pinpoint";
    private final String intakeMotorName = "intake";
    private final String leftIntakeServoName = "left_intake_servo";
    private final String rightIntakeServoName = "right_intake_servo";

    private boolean isFieldCentric = true;

    @Override
    public void initialize() {
        drive = new DriveSubsystem(
                hardwareMap,
                leftFrontName, leftBackName, rightFrontName, rightBackName,
                pinpointName
        );
        register(drive); // must be done before setting default command!

        intake = new IntakeSubsystem(
                hardwareMap,
                intakeMotorName, leftIntakeServoName, rightIntakeServoName
        );
        register(intake);

        driverGamepad = new GamepadEx(gamepad1);

        drive.setDefaultCommand(new DriveCommand(
                drive, () -> isFieldCentric,
                () -> driverGamepad.getLeftY(),
                () -> driverGamepad.getLeftX(),
                () -> driverGamepad.getRightX()
        ));

        intake.setDefaultCommand(new IntakeCommand(
                intake,
                () -> {
                    return (
                            driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                            - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
                    );
                }
        ));

        publishFieldCentricToTelemetry();
        driverGamepad.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(() -> {
                    isFieldCentric = !isFieldCentric;
                    publishFieldCentricToTelemetry();
                });
    }

    void publishFieldCentricToTelemetry() {
        telemetry.addData("Field-Centric", isFieldCentric);
        telemetry.update();
    }
}
