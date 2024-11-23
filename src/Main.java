import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week1/t1-3a.txt", 800, 500);
        Program program = resourceManager.getProgram();
        program.show();
    }
}