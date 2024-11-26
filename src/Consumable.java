import itumulator.world.World;

public interface Consumable {
    void onConsume(World world);
    double getNutritionalValue();
}
