import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week2/tf2-4.txt", 800, 200);
        Program program = resourceManager.getProgram();
        program.show();
    }
}