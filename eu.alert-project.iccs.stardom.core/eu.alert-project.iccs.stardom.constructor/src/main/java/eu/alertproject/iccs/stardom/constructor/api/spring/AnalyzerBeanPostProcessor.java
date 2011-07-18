package eu.alertproject.iccs.stardom.constructor.api.spring;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.constructor.api.Analyzer;
import eu.alertproject.iccs.stardom.constructor.api.Analyzers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 22:35
 */
public class AnalyzerBeanPostProcessor implements BeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(AnalyzerBeanPostProcessor.class);

    @Autowired
    private Analyzers analyzers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof Analyzer<?>){
            logger.debug("Adding analyzer {}",bean);

            analyzers.add((Analyzer<ConnectorAction>) bean);
        }
        return bean;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
