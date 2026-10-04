package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

@TeleOp(name = "Command-Based Teleop", group = "StarterBot")
public class StarterbotCommandBasedTeleop extends CommandOpMode {
    private DriveSubsystem drive;

    private GamepadEx driverGamepad;

    private final String leftFrontName = "left_front_drive";
    private final String rightFrontName = "right_front_drive";
    private final String leftBackName = "left_back_drive";
    private final String rightBackName = "right_back_drive";
    private final String pinpointName = "pinpoint";

    @Override
    public void initialize() {
        drive = new DriveSubsystem(
                hardwareMap,
                leftFrontName, leftBackName, rightFrontName, rightBackName,
                pinpointName
        );
        register(drive); // must be done before setting default command!

        driverGamepad = new GamepadEx(gamepad1);

        drive.setDefaultCommand(new DriveCommand(
                drive, true,
                () -> -driverGamepad.getLeftY(),
                () -> -driverGamepad.getLeftX(),
                () -> -driverGamepad.getRightX(),
                telemetry
        ));
    }
}
