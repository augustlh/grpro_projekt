import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week2/t2-4a.txt", 800, 500);
        Program program = resourceManager.getProgram();
        program.show();
    }
}