package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {
    private final DriveSubsystem drive;
    private final DoubleSupplier forwardSupplier;
    private final DoubleSupplier strafeSupplier;
    private final DoubleSupplier rotateSupplier;

    private final boolean isFieldCentric;

    public DriveCommand(
            DriveSubsystem drive,
            boolean isFieldCentric,
            DoubleSupplier forwardSupplier,
            DoubleSupplier strafeSupplier,
            DoubleSupplier rotateSupplier
    ) {
        this.isFieldCentric = isFieldCentric;
        this.forwardSupplier = forwardSupplier;
        this.strafeSupplier = strafeSupplier;
        this.rotateSupplier = rotateSupplier;
        this.drive = drive;

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
