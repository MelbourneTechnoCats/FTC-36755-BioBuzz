package org.firstinspires.ftc.teamcode.commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

import java.util.function.DoubleSupplier;

public class IntakeCommand extends CommandBase {
    private final IntakeSubsystem intake;
    private final DoubleSupplier powerSupplier;

    public IntakeCommand(
            final IntakeSubsystem intake,
            final DoubleSupplier powerSupplier
    ) {
        this.intake = intake;
        this.powerSupplier = powerSupplier;
        addRequirements(intake);
    }

    public IntakeCommand(
            final IntakeSubsystem intake,
            final double power
    ) {
        this(intake, () -> power);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        intake.run(powerSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        intake.run(0);
    }
}
