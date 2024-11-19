package itumulator.executable;

/**
 * By implementing this interface, an object can dynamically control a objects display information at runtime. 
 * Implementing this interface overrides any image or color associated with a class.
 */
public interface DynamicDisplayInformationProvider {

    /**
     * Should provide {@link DisplayInformation} to determine the visualization of the object.
     * @return DisplayInformation.
     */
    public DisplayInformation getInformation();
}
