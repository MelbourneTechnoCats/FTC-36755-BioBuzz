package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ShootCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@TeleOp(name = "Command-Based Teleop", group = "StarterBot")
public class StarterbotCommandBasedTeleop extends CommandOpMode {
    private DriveSubsystem drive;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;

    private GamepadEx driverGamepad;

    private final double SHOOTER_SPEED = 1500;

    private boolean isFieldCentric = true;

    @Override
    public void initialize() {
        drive = new DriveSubsystem(
                hardwareMap,
                RobotConstants.LEFT_FRONT_NAME, RobotConstants.LEFT_BACK_NAME,
                RobotConstants.RIGHT_FRONT_NAME, RobotConstants.RIGHT_BACK_NAME,
                RobotConstants.PINPOINT_NAME
        );
        register(drive); // must be done before setting default command!

        intake = new IntakeSubsystem(
                hardwareMap,
                RobotConstants.INTAKE_MOTOR_NAME,
                RobotConstants.LEFT_INTAKE_SERVO_NAME, RobotConstants.RIGHT_INTAKE_SERVO_NAME
        );
        register(intake);

        shooter = new ShooterSubsystem(
                hardwareMap,
                RobotConstants.LAUNCHER_MOTOR_NAME, RobotConstants.WINDMILL_SERVO_NAME
        );
        register(shooter);

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
                    double power = (
                            driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                            - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
                    );
                    if (shooter.isTargetVelocityReached())
                        power += 0.5;
                    return power;
                }
        ));

        driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenHeld(new ShootCommand(shooter, SHOOTER_SPEED));

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
