import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week1/t1-1a.txt", 1200, 400);
        Program program = resourceManager.getProgram();
        program.show();
    }
}