package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.commands.DriveCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ShootCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;
import org.firstinspires.ftc.teamcode.utilities.ShooterTargeting;

@TeleOp(name = "Command-Based Teleop", group = "StarterBot")
public class StarterbotCommandBasedTeleop extends CommandOpMode {
    private DriveSubsystem drive;
    private IntakeSubsystem intake;
    private ShooterSubsystem shooter;

    private GamepadEx driverGamepad;
    private GamepadEx mechGamepad;


    private final double SHOOTER_CHANGE_STEP = 25;
    private final double SHOOTER_MIN_SPEED = 1000;

    private double shootSpeedClose = RobotConstants.SHOOT_SPEED_CLOSE;

    private double shootSpeedFar = RobotConstants.SHOOT_SPEED_FAR;

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

        try {
            intake = new IntakeSubsystem(hardwareMap);
            register(intake);
        } catch (Exception e) {

        }

        try {
            shooter = new ShooterSubsystem(
                    hardwareMap,
                    RobotConstants.LAUNCHER_MOTOR_NAME, RobotConstants.WINDMILL_SERVO_NAME
            );
            register(shooter);
        } catch (Exception e) {

        }

        driverGamepad = new GamepadEx(gamepad1);
        mechGamepad = new GamepadEx(gamepad2);

        drive.setDefaultCommand(new DriveCommand(
                drive, () -> isFieldCentric,
                () -> driverGamepad.getLeftY(),
                () -> driverGamepad.getLeftX(),
                () -> driverGamepad.getRightX()
        ));

        if(intake != null){
            intake.setDefaultCommand(new IntakeCommand(
                    intake,
                    () -> {
                        double power = (
                                driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                                - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)
                        );
                        if (shooter != null && shooter.isTargetVelocityReached())
                            power += 0.5;
                        return power;
                    }
            ));
        }

        driverGamepad.getGamepadButton(GamepadKeys.Button.PS)
                .whenPressed(() -> {
                    isFieldCentric = !isFieldCentric;
                });
        driverGamepad.getGamepadButton(GamepadKeys.Button.TRIANGLE)
                .whenPressed(new InstantCommand(
                        () -> { drive.resetHeading(); },
                        drive
                ));

        if (shooter != null) {
            GamepadButton rightBumper = mechGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER);
            GamepadButton leftBumper = mechGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER);

            rightBumper
                    .and(leftBumper.negate())
                    .whileActiveContinuous(new ShootCommand(shooter, () -> shootSpeedClose));


            leftBumper
                    .and(rightBumper.negate())
                    .whileActiveContinuous(new ShootCommand(shooter, () -> shootSpeedFar));

            leftBumper
                    .and(rightBumper)
                    .whileActiveContinuous(new ShootCommand(shooter, ShooterTargeting.calculateVelocity(drive.getTargetDistance())));

            mechGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP)
                    .whenPressed(() -> {
                        double newSpeed = shootSpeedClose + SHOOTER_CHANGE_STEP;
                        if (newSpeed <= ShooterSubsystem.MAX_TICKS_PER_SECOND) {
                            shootSpeedClose = newSpeed;
                        }
                    });
            mechGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN)
                    .whenPressed(() -> {
                        double newSpeed = shootSpeedClose - SHOOTER_CHANGE_STEP;
                        if (newSpeed >= SHOOTER_MIN_SPEED) {
                            shootSpeedClose = newSpeed;
                        }
                    });

            mechGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT)
                    .whenPressed(() -> {
                        double newSpeed = shootSpeedFar + SHOOTER_CHANGE_STEP;
                        if (newSpeed <= ShooterSubsystem.MAX_TICKS_PER_SECOND) {
                            shootSpeedFar = newSpeed;
                        }
                    });
            mechGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT)
                    .whenPressed(() -> {
                        double newSpeed = shootSpeedFar - SHOOTER_CHANGE_STEP;
                        if (newSpeed >= SHOOTER_MIN_SPEED) {
                            shootSpeedFar = newSpeed;
                        }
                    });
        }
    }

    @Override
    public void preRun() {
        drive.setPose(RobotState.robotPose); // pose setting must be done in preRun(), otherwise pinpoint.resetPosAndIMU() will reset the pose too
    }

    @Override
    public void run() {
        super.run();

        telemetry.addData("Distance to target shooting position", drive.getTargetDistance());

        // publish robot position data
        Pose2d pose = drive.getPose();
        telemetry.addData("Current pose", "heading: %.2f deg, X: %.2f in, Y: %.2f in", Math.toDegrees(pose.getHeading()), pose.getX(), pose.getY());
        RobotState.updateRobotPose(pose);

        telemetry.addData("Field-Centric", isFieldCentric);
        telemetry.addData("Close Shooter speed (Dpad Up and Down)", shootSpeedClose);
        telemetry.addData("Far Shooter speed (Dpad Left and Right)", shootSpeedFar);

        telemetry.update();
    }
}
