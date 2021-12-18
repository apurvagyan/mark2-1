package com.team1678.frc2021.auto.modes;

import com.team1678.frc2021.Constants;
import com.team1678.frc2021.subsystems.Intake;
import com.team1678.frc2021.subsystems.Swerve;
import com.team1678.frc2021.auto.AutoModeEndedException;
import com.team1678.frc2021.auto.actions.LambdaAction;
import com.team1678.frc2021.auto.actions.SwerveTrajectoryAction;

import java.util.List;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

public class TestStraightPathMode extends AutoModeBase {
    
    // Swerve instance 
    private final Swerve s_Swerve = new Swerve();

    // trajectory actions
    SwerveTrajectoryAction swerveTrajectoryAction;

    public TestStraightPathMode() {
        TrajectoryConfig config = new TrajectoryConfig(Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                        .setKinematics(Constants.Swerve.swerveKinematics);

        // An example trajectory to follow. All units in meters.
        Trajectory testTrajectory = TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                new Pose2d(0, 0, new Rotation2d(0)),
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(new Translation2d(1, 0), new Translation2d(2, 0)),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(3, 0, new Rotation2d(0)), config);

        var thetaController = new ProfiledPIDController(Constants.AutoConstants.kPThetaController, 0, 0,
                Constants.AutoConstants.kThetaControllerConstraints);
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        swerveTrajectoryAction = new SwerveTrajectoryAction(testTrajectory,
                s_Swerve::getPose, Constants.Swerve.swerveKinematics,
                new PIDController(Constants.AutoConstants.kPXController, 0, 0),
                new PIDController(Constants.AutoConstants.kPYController, 0, 0), thetaController,
                s_Swerve::setModuleStates);
    }

    @Override
    protected void routine() throws AutoModeEndedException {
        System.out.println("Running test mode auto!");

        // reset odometry at the start of the trajectory
        runAction(new LambdaAction(() -> s_Swerve.resetOdometry(swerveTrajectoryAction.getInitialPose())));

        // run trajectory
        //(new LambdaAction(() -> Intake.getInstance().setState(Intake.WantedAction.INTAKE)));
        runAction(swerveTrajectoryAction);

        System.out.println("Finished driving");
    }
}