import help.ResourceManager;
import itumulator.executable.Program;

public static void main(String[] args) {
    ResourceManager resourceManager = new ResourceManager("src/Data/Week2/t2-2a.txt", 1000, 500);
    Program program = resourceManager.getProgram();
    program.show();
}