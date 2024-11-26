import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week2/t2-3a.txt", 800, 400);
        Program program = resourceManager.getProgram();
        program.show();
    }
}