import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week3/tf3-2a.txt", 1000, 500);
        Program program = resourceManager.getProgram();
        program.show();
    }
}