package org.frc2851.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Drivetrain extends Subsystem
{
    private static Drivetrain mInstance = new Drivetrain();

    private Drivetrain()
    {
        super(new Drivebase(), new GearShifter());
    }

    public static Drivetrain getInstance()
    {
        return mInstance;
    }

    private static class Drivebase extends Component
    {
        private CANSparkMax mLeftMaster, mLeftFollowerA, mLeftFollowerB,
                mRightMaster, mRightFollowerA, mRightFollowerB;
        private CANEncoder mLeftEncoder, mRightEncoder;

        public Drivebase()
        {
            super(Drivetrain.class);

            mLeftMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftMasterPort);
            mLeftFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerAPort);
            mLeftFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerBPort);
            mRightMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightMasterPort);
            mRightFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerAPort);
            mRightFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerBPort);

            mRightMaster.setInverted(true);
            mRightFollowerA.setInverted(true);
            mRightFollowerB.setInverted(true);

            mLeftFollowerA.follow(mLeftMaster);
            mLeftFollowerB.follow(mLeftMaster);
            mRightFollowerA.follow(mRightMaster);
            mRightFollowerB.follow(mRightMaster);

            mLeftEncoder = mLeftMaster.getEncoder();
            mRightEncoder = mRightMaster.getEncoder();

            setDefaultCommand(new RunCommand(this::arcadeDrive, "arcade drive", this));
        }

        public void arcadeDrive()
        {
            double throttle = Constants.driverController.get(Constants.drivetrainThrottleAxis);
            double turn = Constants.driverController.get(Constants.drivetrainTurnAxis);

            double leftOut = throttle + turn;
            double rightOut = throttle - turn;

            // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
            mLeftMaster.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
            mRightMaster.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
        }
    }

    private static class GearShifter extends Component
    {
        private DoubleSolenoid mShifterSolenoid;

        public GearShifter()
        {
            super(Drivetrain.class);

            mShifterSolenoid = new DoubleSolenoid(Constants.drivetrainShifterSolenoidForward, Constants.drivetrainShifterSolenoidReverse);

            CommandScheduler.getInstance().addTrigger(() -> !Constants.driverController.get(Constants.drivetrainShiftGearButton),
                    new InstantCommand(() -> mShifterSolenoid.set(DoubleSolenoid.Value.kForward), "high gear", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.driverController.get(Constants.drivetrainShiftGearButton),
                    new InstantCommand(() -> mShifterSolenoid.set(DoubleSolenoid.Value.kReverse), "low gear", this));
        }
    }
}
