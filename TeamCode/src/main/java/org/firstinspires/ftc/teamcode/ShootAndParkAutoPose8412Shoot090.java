package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.commands.ShootCommand;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

@Autonomous()
public class ShootAndParkAutoPose8412Shoot090 extends CommandOpMode {
    private Follower follower;
    private ShooterSubsystem shooter;

    private Pose startingPose = new Pose(
            84, 12, Math.toRadians(90)
    );

    private Pose shootPose = new Pose(
            60, 18, Math.toRadians(90 + 180)
    );

    private Pose parkMidPose = new Pose(
            24, 48, 0 // control point heading is disregarded anyway
    );

    private Pose parkPose = new Pose(
            12, 96, Math.toRadians(0)
    );

    private Path shootPath;
    private Path parkPath;

    private static final double SHOOT_SPEED = 1400;
    private static final long SHOOT_TIME = 5000;

    @Override
    public void initialize() {
        reset();

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startingPose);

        shooter = new ShooterSubsystem(hardwareMap, RobotConstants.LAUNCHER_MOTOR_NAME, RobotConstants.WINDMILL_SERVO_NAME);
        register(shooter);

        shootPath = Paths.line(startingPose, shootPose).linear(startingPose, shootPose);
        parkPath = Paths.curve(shootPose, parkMidPose, parkPose).linear(shootPose, parkPose);

        schedule(new SequentialCommandGroup(
                new FollowPathCommand(follower, shootPath, true),
                new ShootCommand(shooter, SHOOT_SPEED).withTimeout(SHOOT_TIME),
                new FollowPathCommand(follower, parkPath, false)
        ));
    }

    @Override
    public void run() {
        super.run();

        follower.update(); // REQUIRED FOR DRIVETRAIN TO RUN!!!!

        // publish robot position data
        Pose pose = follower.pose();
        telemetry.addData("Pose", "heading: %.2f deg, X: %.2f in, Y: %.2f in", Math.toDegrees(pose.heading()), pose.x(), pose.y());

        telemetry.update();
    }
}
