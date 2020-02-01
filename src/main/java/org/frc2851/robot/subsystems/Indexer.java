package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.util.MotorControllerFactory;

public class Indexer extends Subsystem
{
    private static Indexer mInstance = new Indexer();

    private Indexer()
    {
        addComponents(new Snail(), new Elevator());
    }

    public static Indexer getInstance()
    {
        return mInstance;
    }

    public static class Snail extends Component
    {
        private VictorSPX mMotor;

        public Snail()
        {
            super(Indexer.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.indexerSnailMotorPort);
        }
    }

    public static class Elevator extends Component
    {
        private VictorSPX mMotor;

        public Elevator()
        {
            super(Indexer.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.indexerElevatorMotorPort);
        }
    }
}
