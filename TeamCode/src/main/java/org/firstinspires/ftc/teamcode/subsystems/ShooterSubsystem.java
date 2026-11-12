package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterSubsystem extends SubsystemBase {
    private final Motor launcherMotor;
    private final CRServo windmillServo;

    private final double kP = 50.0;
    private final double kI = 0.0;
    private final double kD = 0.0;

    private final double kS = 43.4433182;
    private final double kV = 1.077729822;

    private static final double WINDMILL_SPEED = 0.3;

    private static final double ENCODER_RESOLUTION = 28; // counts per rotation
    private static final double MAX_RPM = 6000;

    private static final double WINDMILL_THRESHOLD = 50; // difference between target and actual velocity before we can run windmill

    public static final double MAX_TICKS_PER_SECOND = (MAX_RPM / 60) * ENCODER_RESOLUTION;

    private final Telemetry dashboardTelemetry;

    public ShooterSubsystem(
            final HardwareMap hardwareMap,
            final String launcherName, final String windmillName
    ) {
        launcherMotor = new Motor(hardwareMap, launcherName, ENCODER_RESOLUTION, MAX_RPM);
        launcherMotor.setRunMode(Motor.RunMode.VelocityControl);
        launcherMotor.setFeedforwardCoefficients(kS, kV);
        launcherMotor.setVeloCoefficients(kP, kI, kD);
        launcherMotor.setBuffer(1.0);

        windmillServo = new CRServo(hardwareMap, windmillName);
        windmillServo.setInverted(true);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
    }

    private double targetVelocity = 0;

    public boolean isTargetVelocityReached() {
        return (targetVelocity > 0 && getVelocity() > targetVelocity - WINDMILL_THRESHOLD);
    }

    @Override
    public void periodic() {
        dashboardTelemetry.addData("Shooter Target Velocity", targetVelocity);
        dashboardTelemetry.addData("Shooter Target Threshold", targetVelocity - WINDMILL_THRESHOLD);
        dashboardTelemetry.addData("Shooter Current Velocity", getVelocity());
        dashboardTelemetry.update();

        launcherMotor.set(targetVelocity / MAX_TICKS_PER_SECOND);

        if (isTargetVelocityReached())
            windmillServo.set(WINDMILL_SPEED);
        else
            windmillServo.set(0);
    }

    public void run(double targetVelocity) { // in ticks per second
        this.targetVelocity = targetVelocity;
    }

    public double getVelocity() { // in ticks per second
        return launcherMotor.getCorrectedVelocity();
    }
}
