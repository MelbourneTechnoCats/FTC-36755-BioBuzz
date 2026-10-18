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

    private final double kP = 4.0;
    private final double kI = 0.0;
    private final double kD = 0.0;

    private final double kS = 73.41;
    private final double kV = 1.073;

    private final double ENCODER_RESOLUTION = 28; // counts per rotation
    private final double MAX_RPM = 6000;

    private final double WINDMILL_THRESHOLD = 0.9; // 90% target velocity minimum

    private final double MAX_TICKS_PER_SECOND = (MAX_RPM / 60) * ENCODER_RESOLUTION;

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
        return (targetVelocity > 0 && getVelocity() > WINDMILL_THRESHOLD * targetVelocity);
    }

    @Override
    public void periodic() {
        dashboardTelemetry.addData("Shooter Target Velocity", targetVelocity);
        dashboardTelemetry.addData("Shooter Current Velocity", getVelocity());
        dashboardTelemetry.update();

        launcherMotor.set(targetVelocity / MAX_TICKS_PER_SECOND);

        if (isTargetVelocityReached())
            windmillServo.set(1);
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
