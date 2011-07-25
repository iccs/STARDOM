package eu.alertproject.iccs.stardom.identifier.api;

/**
 * User: fotis
 * Date: 22/07/11
 * Time: 17:44
 */
public interface SluggifierService {


    /**
     * <p>
     *     This method sluggifies an incomming string replacing some characters with
     *     whitespace
     * </p>
     * <p>Characters Being Replaced</p>
     * <ul>
     *     <li>+</li>
     *     <li>.</li>
     *     <li>@</li>
     *     <li>-</li>
     *     <li>_</li>
     * </ul>
     *
     * @return The sluggified string
     */
    public String sluggify(String string);
}
