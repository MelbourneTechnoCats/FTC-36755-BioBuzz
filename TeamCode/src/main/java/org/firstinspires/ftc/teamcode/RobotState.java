package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.seattlesolvers.solverslib.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

public class RobotState {
    public static Pose2d robotPose = new Pose2d(0, 0, 0);

    public static final double POSE_VALID_THRESHOLD = 0.1; // minimum X/Y value to consider pose to be valid

    public static void updateRobotPose(Pose2d pose) { // using SolversLib Pose2d object
        if (pose.getX() >= POSE_VALID_THRESHOLD || pose.getY() >= POSE_VALID_THRESHOLD) {
            robotPose = pose;
        }
    }

    public static void updateRobotPose(double x, double y, double heading) { // using discrete X/Y/heading values
        updateRobotPose(new Pose2d(x, y, heading));
    }

    public static void updateRobotPose(Pose pose) { // using Pedro Pathing Pose object
        updateRobotPose(pose.x(), pose.y(), pose.heading());
    }
}
