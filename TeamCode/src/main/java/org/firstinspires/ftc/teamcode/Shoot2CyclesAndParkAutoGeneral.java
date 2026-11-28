package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ConditionalCommand;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ShootCommand;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

import java.util.List;

@Autonomous
public class Shoot2CyclesAndParkAutoGeneral extends CommandOpMode {

    private GamepadEx driverGamepad;

    private Follower follower;
    private ShooterSubsystem shooter;
    private IntakeSubsystem intake;

    private LimelightSubsystem limelight;

    private Pose startingPose = new Pose(
            60, 12, Math.toRadians(270)
    );

    private Pose shootPoseClose = new Pose(
            60, 30, Math.toRadians(90 + 180)
    );

    private Pose intakeMidPose = new Pose(
            11, 64, Math.toRadians(90 + 180)
    );

    private Pose intakePose = new Pose(
            11,13,Math.toRadians(270)
    );


    private Pose shootPoseClosetoShootPoseFarMidPose = new Pose (
            -18,60,0
    );


    private Pose shootPoseFar = new Pose(
            60,144-30+5,Math.toRadians(90)
    );

    private Pose shootFarToParkMidPose = new Pose(
            36, 110, 0 // control point heading is disregarded anyway
    );

    private Pose shootCloseToParkMidPose = new Pose(
            10, 48, 0 // control point heading is disregarded anyway
    );

    private Pose shootCloseToParkFarMidPose2 = new Pose(
            36, 119,0
    );

    private Path shootPath;

    private Path intakePath;

    private Path shootPathTip;

    private Path parkPathTip;

    private Path parkPathNoTip;

    private Path gardenToScanPath;

    private static final long SHOOT_TIME = 6000;

    private static final double INTAKE_POWER = 1;

    private static final long INTAKE_MAX_TIME = 5000;

    private static final long TAG_WAIT_TIME = 1000;


    @Override
    public void initialize() {
        reset();

        driverGamepad = new GamepadEx(gamepad1);

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startingPose);
        RobotState.updateRobotPose(startingPose); // initialise robot pose to starting pose from auto - in case we do not proceed with starting

        shooter = new ShooterSubsystem(hardwareMap, RobotConstants.LAUNCHER_MOTOR_NAME, RobotConstants.WINDMILL_SERVO_NAME);
        register(shooter);

        intake = new IntakeSubsystem(hardwareMap);
        register(intake);

        limelight = new LimelightSubsystem(hardwareMap);
        register(limelight);

        shootPath = Paths.line(startingPose, shootPoseClose).linear(startingPose, shootPoseClose);
        intakePath = Paths.curve(shootPoseClose,intakeMidPose,intakePose).linear(shootPoseClose,intakePose);
        gardenToScanPath = Paths.line(intakePose,shootPoseClose).linear(intakePose,shootPoseClose);
        shootPathTip = Paths.curve(shootPoseClose,shootPoseClosetoShootPoseFarMidPose,shootPoseFar).linear(shootPoseClose,shootPoseFar);
    }

    private Alliance alliance = Alliance.BLUE;

    private ParkSide parkSide = ParkSide.CLOSE;

    @Override
    public void initialize_loop() {
        // TODO: things to run after initialize(), while the robot is still in Init (think auto config)
        // NOTE: if we're doing auto config here then we'll have to store it in a class member to pass it to preRun()

        driverGamepad.readButtons();

        if(driverGamepad.wasJustPressed(GamepadKeys.Button.CIRCLE)){

            alliance = (alliance == Alliance.BLUE) ? Alliance.RED : Alliance.BLUE;
        }

        if(driverGamepad.wasJustPressed(GamepadKeys.Button.TRIANGLE)){

            parkSide = (parkSide == ParkSide.CLOSE) ? ParkSide.FAR : ParkSide.CLOSE;
        }

        telemetry.addData("Alliance is (Press Circle to switch)", alliance);
        telemetry.addData("Park-side is (Press Triangle to switch)", parkSide);
        telemetry.update();

    }

    @Override
    public void preRun() {
        // NOTE: this is run the moment Play is pressed

        Command gardenIntakeCommand =
                new ParallelCommandGroup(
                        new FollowPathCommand(follower, intakePath,true),
                        new IntakeCommand(intake,INTAKE_POWER)
                ).withTimeout(INTAKE_MAX_TIME);

        List<Integer> startTagIds = (alliance == Alliance.BLUE) ? LimelightSubsystem.BLUE_CLOSE_IDS : LimelightSubsystem.RED_CLOSE_IDS;

        Pose parkPose = (parkSide == ParkSide.CLOSE) ? new Pose(13, 96, 0) : new Pose (13, 120, 0);
        parkPathTip = Paths.curve(shootPoseFar, shootFarToParkMidPose, parkPose).linear(shootPoseFar, parkPose);
        if (parkSide == ParkSide.CLOSE) {
            parkPathNoTip = Paths.curve(shootPoseClose, shootCloseToParkMidPose, parkPose).linear(shootPoseClose, parkPose);
        } else {
            parkPathNoTip = Paths.curve(shootPoseClose, shootCloseToParkMidPose, shootCloseToParkFarMidPose2, parkPose).linear(shootPoseClose, parkPose);
        }


        schedule(new SequentialCommandGroup(
                new FollowPathCommand(follower, shootPath, true),
                new ShootCommand(shooter, RobotConstants.SHOOT_SPEED_CLOSE).withTimeout(SHOOT_TIME),
                gardenIntakeCommand,
                new FollowPathCommand(follower,gardenToScanPath),
                new WaitUntilCommand(
                        () -> limelight.isTagVisible(startTagIds)
                ).withTimeout(TAG_WAIT_TIME),
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                new FollowPathCommand(follower, shootPathTip),
                                new ShootCommand(shooter, RobotConstants.SHOOT_SPEED_CLOSE).withTimeout(SHOOT_TIME),
                                new FollowPathCommand(follower, parkPathTip)

                        ),
                        new SequentialCommandGroup(
                                new ShootCommand(shooter, RobotConstants.SHOOT_SPEED_CLOSE).withTimeout(SHOOT_TIME),
                                new FollowPathCommand(follower, parkPathNoTip)
                        ),() -> !limelight.isTagVisible(startTagIds)
                )
        ));
    }

    @Override
    public void run() {
        super.run();

        follower.update(); // REQUIRED FOR DRIVETRAIN TO RUN!!!!

        // publish robot position data
        Pose pose = follower.pose();
        telemetry.addData("Pose", "heading: %.2f deg, X: %.2f in, Y: %.2f in", Math.toDegrees(pose.heading()), pose.x(), pose.y());
        RobotState.updateRobotPose(pose);

        telemetry.addData("Tag visible", limelight.isTagVisible((alliance == Alliance.BLUE) ? LimelightSubsystem.BLUE_CLOSE_IDS : LimelightSubsystem.RED_CLOSE_IDS));

        telemetry.update();
    }
}
