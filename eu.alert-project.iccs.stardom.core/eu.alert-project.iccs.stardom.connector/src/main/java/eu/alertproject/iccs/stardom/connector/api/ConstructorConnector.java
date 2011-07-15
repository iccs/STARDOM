package eu.alertproject.iccs.stardom.connector.api;

/**
 * User: fotis
 * Date: 07/07/11
 * Time: 11:39
 *
 *
 *
 */
public interface ConstructorConnector<T extends ConnectorContext<? extends ConnectorAction>>{

    //http://stackoverflow.com/questions/5726583/spring-rest-multiple-requestbody-parameters-possible
    public void action(T context);

}
