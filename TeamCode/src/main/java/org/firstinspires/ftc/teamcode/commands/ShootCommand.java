package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ShooterSubsystem;

import java.util.function.DoubleSupplier;

public class ShootCommand extends CommandBase {
    private final ShooterSubsystem shooter;
    private final DoubleSupplier velocitySupplier;

    public ShootCommand(
            final ShooterSubsystem shooter,
            final DoubleSupplier velocitySupplier
    ) {
        this.shooter = shooter;
        this.velocitySupplier = velocitySupplier;
        addRequirements(shooter);
    }

    public ShootCommand(
            final ShooterSubsystem shooter,
            final double velocity
    ) {
        this(shooter, () -> velocity);
    }

    @Override
    public void initialize() {
        shooter.run(velocitySupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.run(0);
    }
}
