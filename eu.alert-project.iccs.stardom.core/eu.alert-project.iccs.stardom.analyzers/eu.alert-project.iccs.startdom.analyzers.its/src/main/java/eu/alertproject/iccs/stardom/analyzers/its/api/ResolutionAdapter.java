package eu.alertproject.iccs.stardom.analyzers.its.api;

/**
 * User: fotis
 * Date: 04/02/12
 * Time: 19:25
 */
public interface ResolutionAdapter {

    boolean isResolved(String value);

    boolean isReopened(String value);
}
