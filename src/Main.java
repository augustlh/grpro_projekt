import java.awt.Color;
import java.io.File;
import java.util.*;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week1/t1-1a.txt");
        resourceManager.getProgram().show();
    }


}