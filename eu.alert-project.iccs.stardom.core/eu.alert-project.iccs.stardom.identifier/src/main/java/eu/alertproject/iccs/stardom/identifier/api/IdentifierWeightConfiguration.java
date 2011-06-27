package eu.alertproject.iccs.stardom.identifier.api;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 10:45
 * To change this template use File | Settings | File Templates.
 */
public class IdentifierWeightConfiguration {

    private double threshold;
    private LevelWeightConfiguration levelWeights;
    private PropertyWeightConfiguration firstName;
    private PropertyWeightConfiguration lastName;
    private PropertyWeightConfiguration username;
    private PropertyWeightConfiguration email;


    public IdentifierWeightConfiguration(
            double threshold,
            LevelWeightConfiguration levelWeights,
            PropertyWeightConfiguration firstName,
            PropertyWeightConfiguration lastName,
            PropertyWeightConfiguration username,
            PropertyWeightConfiguration email) {

        this.threshold = threshold;
        this.levelWeights = levelWeights;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
    }


    public IdentifierWeightConfiguration() {

        this(
                0.0,
                new LevelWeightConfiguration(0.0,0.0,0.0),
                PropertyWeightConfiguration.generateDefault(PropertyWeightConfiguration.Property.FIRSTNAME),
                PropertyWeightConfiguration.generateDefault(PropertyWeightConfiguration.Property.LASTNAME),
                PropertyWeightConfiguration.generateDefault(PropertyWeightConfiguration.Property.USERNAME),
                PropertyWeightConfiguration.generateDefault(PropertyWeightConfiguration.Property.EMAIL));

    }


    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public LevelWeightConfiguration getLevelWeights() {
        return levelWeights;
    }

    public void setLevelWeights(LevelWeightConfiguration levelWeights) {
        this.levelWeights = levelWeights;
    }

    public PropertyWeightConfiguration getFirstName() {
        return firstName;
    }

    public void setFirstName(PropertyWeightConfiguration firstName) {
        this.firstName = firstName;
    }

    public PropertyWeightConfiguration getLastName() {
        return lastName;
    }

    public void setLastName(PropertyWeightConfiguration lastName) {
        this.lastName = lastName;
    }

    public PropertyWeightConfiguration getUsername() {
        return username;
    }

    public void setUsername(PropertyWeightConfiguration username) {
        this.username = username;
    }

    public PropertyWeightConfiguration getEmail() {
        return email;
    }

    public void setEmail(PropertyWeightConfiguration email) {
        this.email = email;
    }
}
