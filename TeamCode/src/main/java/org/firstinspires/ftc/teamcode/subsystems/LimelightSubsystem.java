package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.RobotConstants;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class LimelightSubsystem extends SubsystemBase {
    private Limelight3A limelight;

    public LimelightSubsystem(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, RobotConstants.LIMELIGHT_NAME);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    private List<LLResultTypes.FiducialResult> fiducialResults;

    @Override
    public void periodic() {
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            fiducialResults = result.getFiducialResults();
        } else {
            fiducialResults = null;
        }
    }

    public boolean isTagVisible(List<Integer> ids) {
        if (fiducialResults == null) return false;
        for (LLResultTypes.FiducialResult result : fiducialResults) {
            if (ids.contains(result.getFiducialId())) return true;
        }
        return false;
    }

    public boolean isTagVisible(int id) {
        return isTagVisible(Arrays.asList(id));
    }

    public Trigger getTagTrigger(List<Integer> ids) {
        return new Trigger(() -> {
            return isTagVisible(ids);
        });
    }

    public Trigger getTagTrigger(int id) {
        return new Trigger(() -> {
            return isTagVisible(id);
        });
    }
}
