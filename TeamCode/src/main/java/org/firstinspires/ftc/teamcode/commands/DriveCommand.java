package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {
    private final DriveSubsystem drive;
    private final DoubleSupplier forwardSupplier;
    private final DoubleSupplier strafeSupplier;
    private final DoubleSupplier rotateSupplier;

    private final boolean isFieldCentric;

    private final Telemetry telemetry;

    public DriveCommand(
            DriveSubsystem drive,
            boolean isFieldCentric,
            DoubleSupplier forwardSupplier,
            DoubleSupplier strafeSupplier,
            DoubleSupplier rotateSupplier,
            Telemetry telemetry
    ) {
        this.isFieldCentric = isFieldCentric;
        this.forwardSupplier = forwardSupplier;
        this.strafeSupplier = strafeSupplier;
        this.rotateSupplier = rotateSupplier;
        this.drive = drive;
        this.telemetry = telemetry;

        addRequirements(drive);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        double forward = forwardSupplier.getAsDouble();
        double strafe = strafeSupplier.getAsDouble();
        double rotate = rotateSupplier.getAsDouble();

        telemetry.addData("Drive Control", "forward %.2f, strafe %.2f, rotate %.2f", forward, strafe, rotate);
        telemetry.update();

        if (isFieldCentric)
            drive.driveFieldCentric(forward, strafe, rotate);
        else
            drive.driveRobotCentric(forward, strafe, rotate);
    }

    @Override
    public void end(boolean interrupted) {
        drive.driveRobotCentric(0, 0, 0);
    }
}
