import itumulator.executable.Program;

/**
 * The Main class serves as the entry point for the application.
 * It initializes a ResourceManager with a specified file and parameters,
 * retrieves a Program object, and invokes its show method to display the program.
 */
public class Main {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager("src/Data/Week2/t2-2a.txt", 1000, 500);
        Program program = resourceManager.getProgram();
        program.show();
    }
}