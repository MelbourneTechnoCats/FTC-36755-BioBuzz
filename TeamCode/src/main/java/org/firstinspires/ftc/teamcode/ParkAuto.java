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
public class ParkAuto extends CommandOpMode {

    private GamepadEx driverGamepad;

    private Follower follower;

    private Pose startingPose = new Pose(
            12, 132, 0
    );

   private Pose parkPoseFar = new Pose(
           12, 120, 0
   );

   private Pose parkPoseClose = new Pose(
           12, 96,0
   );

    private Path parkPathClose;

    private Path parkPathFar;


    @Override
    public void initialize() {
        reset();

        driverGamepad = new GamepadEx(gamepad1);

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startingPose);
        RobotState.updateRobotPose(startingPose); // initialise robot pose to starting pose from auto - in case we do not proceed with starting

        parkPathFar = Paths.line(startingPose, parkPoseFar).linear(startingPose,parkPoseFar);
        parkPathClose = Paths.line(startingPose, parkPoseClose).linear(startingPose,parkPoseClose);
    }

    private ParkSide parkSide = ParkSide.CLOSE;

    @Override
    public void initialize_loop() {
        // TODO: things to run after initialize(), while the robot is still in Init (think auto config)
        // NOTE: if we're doing auto config here then we'll have to store it in a class member to pass it to preRun()

        driverGamepad.readButtons();


        if(driverGamepad.wasJustPressed(GamepadKeys.Button.TRIANGLE)){

            parkSide = (parkSide == ParkSide.CLOSE) ? ParkSide.FAR : ParkSide.CLOSE;
        }

        telemetry.addData("Park-side is (Press Triangle to switch)", parkSide);
        telemetry.update();

    }

    @Override
    public void preRun() {
        // NOTE: this is run the moment Play is pressed


        Path parkPath = (parkSide == ParkSide.CLOSE) ? parkPathClose : parkPathFar;



        schedule(new SequentialCommandGroup(
                new FollowPathCommand(follower, parkPath, true)


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


        telemetry.update();
    }
}
