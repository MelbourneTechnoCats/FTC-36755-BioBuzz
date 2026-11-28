package org.firstinspires.ftc.teamcode.utilities;

import com.seattlesolvers.solverslib.util.InterpLUT;

public class ShooterTargeting {
    public static final InterpLUT VELOCITY_LUT = new InterpLUT() {{
        add(0.1, 4000);
        add(0.2, 4500);
        // TODO fill in the rest of the table
        createLUT();
    }};

    private static final double MINIMUM_VELOCITY_THRESHOLD = 500;
        //TODO Must assign later after tuning shooter
    private static final double MAXIMUM_VELOCITY_THRESHOLD = 7000;
        //TODO Must assign later after tuning shooter


    public static double calculateVelocity(double distance){
        double velocity = VELOCITY_LUT.get(distance);

            return Math.min(MAXIMUM_VELOCITY_THRESHOLD, Math.max(MINIMUM_VELOCITY_THRESHOLD, velocity));

    }
}
