package com.team1678.frc2021.states;

import com.team254.lib.util.PolynomialRegression;
import com.team254.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.InterpolatingDouble;



public class SuperstructureConstants {
    public static final double kTurretPaddingDegrees = 3;
    public static final double kHoodPaddingDegrees = 2;
    public static final double kShooterPaddingVelocity = 100;


    public static final double[] kPadding = {
            kTurretPaddingDegrees, kShooterPaddingVelocity, kHoodPaddingDegrees};

    //turret
    public static final double kTurretDOF = 360;


    //hood
    public static double kDefaultHoodAngle = Math.toRadians(0);
    public static boolean kUseHoodAutoAimPolynomial = false;

    public static boolean kUseSmartdashboard = false;

    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kHoodAutoAimMap = new InterpolatingTreeMap<>();
    public static PolynomialRegression kHoodAutoAimPolynomial;

    public static double[][] kHoodManualAngle = {
        { 49.0, 52.0},
        { 66.0, 60.},
        { 84.7, 70.},
        { 102.5, 75.0 },
        { 112.7, 77.0 },
        { 126.6, 78.0 },
        { 140.4, 81.5 },
        { 150.1, 82.0 },
        { 160.3, 83.0 },
        { 173.0, 83.3 },
        // { 257.0, 83.0 },
        // //{ 318.0, 89.0 },
        // { 330.0, 88.5 },
        
    };

    static {
        //iterate through the array and place each point into the interpolating tree
        for (double[] pair : kHoodManualAngle) {
            kHoodAutoAimMap.put(new InterpolatingDouble(pair[0]), new InterpolatingDouble(pair[1]));
        }
        
        kHoodAutoAimPolynomial = new PolynomialRegression(kHoodManualAngle, 1);
    }
    
    //shooter
    public static double kDefaultShootingRPM = 2950.0;
    public static boolean kUseFlywheelAutoAimPolynomial = false;

    public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kFlywheelAutoAimMap = new InterpolatingTreeMap<>();
    public static PolynomialRegression kFlywheelAutoAimPolynomial;

    public static double[][] kFlywheelManualRPM = {
        { 49.0, 1800},
        { 66.0, 2300},
        { 84.7, 2700},
        { 102.5, 3400 },
        { 112.7, 3500 },
        { 126.6, 3800 },
        { 140.4, 4000 },
        { 150.1, 4000 },
        { 160.3, 4000 },
        { 173.0, 4000 },
        // { 265.0, 3300 },
        // { 318.0, 3600 },
        // { 330.0, 3700 },
        
        // TODO: Fill in with values
    };

    static {
        for (double[] pair : kFlywheelManualRPM) {
            kFlywheelAutoAimMap.put(new InterpolatingDouble(pair[0]), new InterpolatingDouble(pair[1]));
        }

        kFlywheelAutoAimPolynomial = new PolynomialRegression(kFlywheelManualRPM, 2);
    }

}
