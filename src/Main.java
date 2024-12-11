import help.ResourceManager;
import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/demofile1.txt", 1300, 400);
        Program program = resourceManager.getProgram();
        program.show();
    }
}