package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class DriveSubsystem extends SubsystemBase {
    private final Motor leftFrontDrive;
    private final Motor leftBackDrive;
    private final Motor rightFrontDrive;
    private final Motor rightBackDrive;

    private final GoBildaPinpointDriver pinpoint;

    private final MecanumDrive drive;

    private final double PINPOINT_X_OFFSET = 0.0;
    private final double PINPOINT_Y_OFFSET = 0.0;
    // in mm

    private final GoBildaPinpointDriver.EncoderDirection PINPOINT_X_DIRECTION
            = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    private final GoBildaPinpointDriver.EncoderDirection PINPOINT_Y_DIRECTION
            = GoBildaPinpointDriver.EncoderDirection.FORWARD;

    public DriveSubsystem(
            final HardwareMap hardwareMap,
            final String leftFrontName, final String leftBackName,
            final String rightFrontName, final String rightBackName,
            final String pinpointName
    ) {
        leftFrontDrive = new Motor(hardwareMap, leftFrontName, Motor.GoBILDA.RPM_312);
        leftBackDrive = new Motor(hardwareMap, leftBackName, Motor.GoBILDA.RPM_312);
        rightFrontDrive = new Motor(hardwareMap, rightFrontName, Motor.GoBILDA.RPM_312);
        rightBackDrive = new Motor(hardwareMap, rightBackName, Motor.GoBILDA.RPM_312);

        leftFrontDrive.setInverted(true);
        leftBackDrive.setInverted(true);
        rightFrontDrive.setInverted(false);
        rightBackDrive.setInverted(false);

        leftFrontDrive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        drive = new MecanumDrive(leftFrontDrive, rightFrontDrive, leftBackDrive, rightBackDrive);

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, pinpointName);
        pinpoint.setOffsets(PINPOINT_X_OFFSET, PINPOINT_Y_OFFSET, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(PINPOINT_X_DIRECTION, PINPOINT_Y_DIRECTION);
        pinpoint.resetPosAndIMU();
    }

    public void driveRobotCentric(double forward, double strafe, double rotate) {
        drive.driveRobotCentric(forward, strafe, rotate);
    }

    public Pose2d getPose() {
        Pose2D pose = pinpoint.getPosition();
        return new Pose2d(
                pose.getX(DistanceUnit.INCH), pose.getY(DistanceUnit.INCH),
                pose.getHeading(AngleUnit.DEGREES)
        );
    }

    public void driveFieldCentric(double forward, double strafe, double rotate) {
        drive.driveFieldCentric(forward, strafe, rotate, getPose().getHeading());
    }
}
