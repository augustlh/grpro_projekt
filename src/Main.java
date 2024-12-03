import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week2/t2-1c.txt", 1000, 200);
        Program program = resourceManager.getProgram();
        program.show();
    }
}