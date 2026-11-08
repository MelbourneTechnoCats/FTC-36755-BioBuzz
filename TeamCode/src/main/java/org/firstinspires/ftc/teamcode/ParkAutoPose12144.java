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
public class ParkAutoPose12144 extends CommandOpMode {
    private Follower follower;
    private ShooterSubsystem shooter;

    private Pose startingPose = new Pose(
            12, 144, Math.toRadians(270)
    );

    private Pose parkPose = new Pose(
            14, 120, Math.toRadians(0)
    );




    private Path parkPath;


    @Override
    public void initialize() {
        reset();

        follower = Constants.createFollower(hardwareMap);
        follower.setPose(startingPose);


        parkPath = Paths.curve(startingPose, parkPose).linear(startingPose, parkPose);

        schedule(new FollowPathCommand(follower, parkPath, false));
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
