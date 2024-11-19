import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ResourceManager {
    protected Program p;

    public ResourceManager(String filepath) {
        try {
            initializeProgram(filepath);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "Fejl i indlæsning af fil");
        }
    }

    void initializeProgram(String filepath) throws Exception {
        // Make hashmap with all classes (only for grass now)
        File file = new File(filepath);
        Scanner s = new Scanner(file);
        int size = Integer.parseInt(s.nextLine());
        p = new Program(size,800,150);
        World world = p.getWorld();
        while(s.hasNextLine()){
            List<String> line = Arrays.asList( s.nextLine().split("[ -]"));
            if (line.size()<=2){
                for (int i = 0; i<=Integer.parseInt(line.getLast()); i++){
                    world.setTile(makeLocation(p.getSize(),world),new Grass());
                }
            }
        }
    }

    Location makeLocation(int size, World world){
        Random r = new Random();
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);
        while (!world.containsNonBlocking(l)){
            x=r.nextInt(size);
            y=r.nextInt(size);
            l = new Location(x, y);
        }
        return l;
    }
    Program getProgram(){
        return p;
    }
}
