package org.frc2851.robot.framework;

import java.util.List;
import java.util.Vector;

public abstract class Subsystem
{
    private Vector<Component> mComponents;

    public void addComponents(Component... components)
    {
        mComponents = new Vector<>(List.of(components));
    }

    public Vector<Component> getComponents()
    {
        return mComponents;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
}
