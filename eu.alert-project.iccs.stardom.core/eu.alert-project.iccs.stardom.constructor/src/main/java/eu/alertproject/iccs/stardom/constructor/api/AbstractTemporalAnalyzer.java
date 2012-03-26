package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricTemporal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * User: fotis
 * Date: 28/01/12
 * Time: 22:52
 */
public abstract class AbstractTemporalAnalyzer<T extends ConnectorAction,E extends MetricTemporal> implements Analyzer<T> {

    private Logger logger = LoggerFactory.getLogger(AbstractTemporalAnalyzer.class);
    private Class<E> clazz;

    @Autowired
    private MetricDao metricDao;

    protected AbstractTemporalAnalyzer(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void analyze(Identity identity, T action) {

        try{

            if(identity == null){
                logger.warn("void analyze() Can't work with a null identity {}");
                return;
            }

            Constructor<E> constructor = getMetricClass().getConstructor();
            E newMetrics = constructor.newInstance();
            newMetrics.setIdentity(identity);
            newMetrics.setCreatedAt(new Date());
            newMetrics.setTemporal(action.getDate());
            Metric insert = metricDao.insert(newMetrics);

            logger.trace("void analyze() Created Metric {} ",insert);

        } catch (NoSuchMethodException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (InvocationTargetException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (InstantiationException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (IllegalAccessException e) {
            logger.warn("Couldn't work with reflection {}",e);
        }

    }

    public Class<E> getMetricClass() {
        return this.clazz;
    }

}
