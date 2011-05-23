package eu.alertproject.iccs.stardom.identifier.api;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 23/05/11
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class LevelWeightConfiguration {

    private double levelUl;
    private double levelVpu;
    private double levelMl;

    public LevelWeightConfiguration(
            double levelUl,
            double levelVpu,
            double levelMl) {

        this.levelUl = levelUl;
        this.levelVpu = levelVpu;
        this.levelMl = levelMl;
    }


    public double getLevelUl() {
        return levelUl;
    }

    public double getLevelVpu() {
        return levelVpu;
    }

    public double getLevelMl() {
        return levelMl;
    }
}

