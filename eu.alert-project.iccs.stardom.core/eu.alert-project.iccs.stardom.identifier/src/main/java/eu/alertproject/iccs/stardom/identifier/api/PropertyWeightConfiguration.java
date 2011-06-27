package eu.alertproject.iccs.stardom.identifier.api;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class PropertyWeightConfiguration {


    public enum Property{
        FIRSTNAME,
        LASTNAME,
        USERNAME,
        EMAIL,
        NONE
    }


    private Property property;

    /**
     * The univocity level
     */
    private double uvl;

    /**
     * Values per user
     */
    private double vpu;

    /**
     * Misleading level
     */
    private double ml;


    private String id;


    public PropertyWeightConfiguration() {

        this(
                Property.NONE,
                0.0,
                0.0,
                0.0
        );

    }

    public PropertyWeightConfiguration(
            Property property,
            double uvl,
            double vpu,
            double ml
            ) {


        this.property = property;
        this.uvl = uvl;
        this.vpu = vpu;
        this.ml = ml;

    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }


    public double getUvl() {
        return uvl;
    }

    public void setUvl(double uvl) {
        this.uvl = uvl;
    }

    public double getVpu() {
        return vpu;
    }

    public void setVpu(double vpu) {
        this.vpu = vpu;
    }

    public double getMl() {
        return ml;
    }

    public void setMl(double ml) {
        this.ml = ml;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static PropertyWeightConfiguration generateDefault(Property property){
        return new PropertyWeightConfiguration(
                            property,
                            0.0,
                            0.0,
                            0.0);

    }
}
