import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;

import java.awt.Color;

public class Rabbit implements DynamicDisplayInformationProvider {

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(Color.red, "rabbit-large");
    }
}
