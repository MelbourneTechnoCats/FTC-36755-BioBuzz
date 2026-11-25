package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.commands.IntakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;

import java.util.Arrays;

@TeleOp(name = "Limelight Test")
public class LimelightOpMode extends CommandOpMode {
    private LimelightSubsystem limelight;

    private IntakeSubsystem intake;

    @Override
    public void initialize() {
        limelight = new LimelightSubsystem(hardwareMap);
        register(limelight);

        intake = new IntakeSubsystem(hardwareMap);
        register(intake);

        limelight.getTagTrigger(Arrays.asList(30, 31, 32, 33))
                .whileActiveOnce(new IntakeCommand(intake, 1));
    }

    @Override
    public void run() {
        super.run();
        telemetry.update();
    }
}
