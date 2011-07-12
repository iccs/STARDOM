package eu.alertproject.iccs.stardom.bus.api.annotation;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * User: fotis
 * Date: 12/07/11
 * Time: 19:28
 */
public class EventHandlersBeanPostProcessor implements BeanPostProcessor {

    private Logger logger = LoggerFactory.getLogger(EventHandlersBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        if(bean !=null && (bean.getClass().isAnnotationPresent(EventHandler.class)) ){
            logger.trace("Object postProcessAfterInitialization() Adding {} ",bean);
            AnnotationProcessor.process(bean);
        }

        return bean;

    }

}
