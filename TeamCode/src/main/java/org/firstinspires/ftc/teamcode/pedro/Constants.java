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
                Controller primaryTranslationalForward = Controller.proportional(0.21378726411438811);
                Controller secondaryTranslationalForward = Controller.proportional(0.07898868158936063);
                Controller primaryTranslationalLateral = Controller.proportional(0.503409946838375);
                Controller secondaryTranslationalLateral = Controller.proportional(0.18599652399526279);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01691496336574512));
                c.brake.set(Controller.proportionalFeedforward(0.014377718860883352));

                c.headingFeedback.set(Controller.proportional(4.062292390658076));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.053236856231154414, 0.005130649096632887));

                c.linearBrakeCoefficients.set(Matrix.diag(0.1017451762430657, 0.06787410805798964));
                c.quadraticBrakeCoefficients.set(Matrix.diag(5.215921984273621E-4, 0.0014390264923962396));

                c.maxAchievableForwardVelocity.set(63.18234268390586);
                c.maxAchievableStrafeVelocity.set(49.01765234229226);
                c.naturalForwardDeceleration.set(36.676819747859014);
                c.naturalStrafeDeceleration.set(63.6109152317641);
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