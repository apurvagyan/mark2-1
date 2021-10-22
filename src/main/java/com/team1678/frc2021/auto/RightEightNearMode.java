package com.team1678.frc2021.auto;

import com.team1678.frc2021.Constants;
import com.team1678.frc2021.subsystems.Superstructure;
import com.team1678.frc2021.subsystems.Swerve;
import java.util.List;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.spline.SplineHelper;
import edu.wpi.first.wpilibj.spline.Spline.ControlVector;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;

public class RightEightNearMode extends SequentialCommandGroup {

    public RightEightNearMode(Swerve s_Swerve) {

        TrajectoryConfig config = new TrajectoryConfig(Constants.AutoConstants.kMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                        .setKinematics(Constants.Swerve.swerveKinematics);

        TrajectoryConfig reversedConfig = new TrajectoryConfig(Constants.AutoConstants.kMaxSpeedMetersPerSecond,
            Constants.AutoConstants.kMaxAccelerationMetersPerSecondSquared)
                    .setKinematics(Constants.Swerve.swerveKinematics);
                
        reversedConfig.setReversed(true);

        TrajectoryConfig slowConfig = new TrajectoryConfig(Constants.AutoConstants.kSlowMaxSpeedMetersPerSecond,
                Constants.AutoConstants.kSlowMaxAccelerationMetersPerSecondSquared)
                        .setKinematics(Constants.Swerve.swerveKinematics);
        
        Trajectory trenchIntake =
            TrajectoryGenerator.generateTrajectory(
                new Pose2d(2.90, 0.71, Rotation2d.fromDegrees(0.0)),
                List.of(new Translation2d(5.0, 0.71)),
                new Pose2d(6.10, 0.71, Rotation2d.fromDegrees(0.0)), 
                config);
        
        Trajectory closeShot =
            TrajectoryGenerator.generateTrajectory(
                new Pose2d(6.10, 0.71, Rotation2d.fromDegrees(135.0)),
                List.of(//new Translation2d(5.10, 0.71),
                        //new Translation2d(2.54, 2.54)
                        //new Translation2d(1.52, 4.84)
                        ),
                new Pose2d(1.52, 5.84, Rotation2d.fromDegrees(90.0)),
                config);

        var thetaController =
            new ProfiledPIDController(
                0.37, 0, 0.55, Constants.AutoConstants.kThetaControllerConstraints);
        
        thetaController.enableContinuousInput(-Math.PI, Math.PI);

        SwerveControllerCommand trenchIntakeCommand =
            new SwerveControllerCommand(
                trenchIntake,
                s_Swerve::getPose,
                Constants.Swerve.swerveKinematics,
                new PIDController(Constants.AutoConstants.kPXController, 0, 0),
                new PIDController(Constants.AutoConstants.kPYController, 0, 0),
                thetaController,
                () -> Rotation2d.fromDegrees(0), //Swerve Heading
                s_Swerve::setModuleStates,
                s_Swerve);

        SwerveControllerCommand closeShotCommand =
        new SwerveControllerCommand(
            closeShot,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            thetaController,
            () -> Rotation2d.fromDegrees(180), // Swerve Heading
            s_Swerve::setModuleStates,
            s_Swerve);
        
        // SwerveControllerCommand sGIntakeCommand =
        // new SwerveControllerCommand(
        //     sGIntake,
        //     s_Swerve::getPose,
        //     Constants.Swerve.swerveKinematics,
        //     new PIDController(Constants.AutoConstants.kPXController, 0, 0),
        //     new PIDController(Constants.AutoConstants.kPYController, 0, 0),
        //     thetaController,
        //     s_Swerve::setModuleStates,
        //     s_Swerve);

        addCommands(
            new InstantCommand(() -> s_Swerve.resetOdometry(trenchIntake.getInitialPose())),
            trenchIntakeCommand,
            closeShotCommand
            // sGIntakeCommand
        );

    }
}