package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotConstants;

public class Constants {
    public static MecanumConfig driveConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set(RobotConstants.LEFT_FRONT_NAME);
                c.backLeftName.set(RobotConstants.LEFT_BACK_NAME);
                c.frontRightName.set(RobotConstants.RIGHT_FRONT_NAME);
                c.backRightName.set(RobotConstants.RIGHT_BACK_NAME);

                c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(RobotConstants.PINPOINT_X_OFFSET);
        c.yPodOffset.set(RobotConstants.PINPOINT_Y_OFFSET);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(RobotConstants.PINPOINT_OFFSET_UNIT);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.28013459555762216);
                Controller secondaryTranslationalForward = Controller.proportional(0.10350224772428872);
                Controller primaryTranslationalLateral = Controller.proportional(0.35106129440556844);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1297077677523073);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013905521330217331));
                c.brake.set(Controller.proportionalFeedforward(0.011819693130684731));

                c.headingFeedback.set(Controller.proportional(4.465373790971206));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04529068024932341, 0.007563148611165829));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07972161409970677, 0.06899105356833087));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012171745261276143, 0.001425606565807203));

                c.maxAchievableForwardVelocity.set(74.59838606558533);
                c.maxAchievableStrafeVelocity.set(60.65199153115723);
                c.naturalForwardDeceleration.set(43.29693145436933);
                c.naturalStrafeDeceleration.set(73.13519707681095);
            }
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new Follower(
                new PinpointLocalizer(hardwareMap, Constants.localizerConfig),
                new Mecanum(hardwareMap, Constants.driveConfig),
                new Foresight(Constants.foresightConfig)
        );
    }
}