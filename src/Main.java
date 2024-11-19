import itumulator.executable.Program;

public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week1/t1-1c.txt", 1200, 1000);
        Program program = resourceManager.getProgram();
        program.show();
    }


}