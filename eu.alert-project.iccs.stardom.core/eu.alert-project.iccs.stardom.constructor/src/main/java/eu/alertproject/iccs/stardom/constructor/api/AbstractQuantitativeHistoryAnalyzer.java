package eu.alertproject.iccs.stardom.constructor.api;

import eu.alertproject.iccs.stardom.connector.api.ConnectorAction;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: fotis
 * Date: 28/01/12
 * Time: 15:14
 */
public abstract class AbstractQuantitativeHistoryAnalyzer<T extends ConnectorAction,E extends MetricQuantitative> implements Analyzer<T> {

    private Logger logger = LoggerFactory.getLogger(AbstractQuantitativeHistoryAnalyzer.class);

    @Autowired
    private MetricDao metricDao;
    private final Class<E> clazz;
    private ReentrantLock lock;

    protected AbstractQuantitativeHistoryAnalyzer(Class<E> clazz) {
        this.clazz = clazz;
    }

    public MetricDao getMetricDao() {
        return metricDao;
    }

    @PostConstruct
    public void init(){
        this.lock = new ReentrantLock();
    }

    @PreDestroy
    public void lock(){
        if(lock != null){
            lock.unlock();
            lock =null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionalAnalyze(Identity identity, T action) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Integer numberForIdentityAfer = getMetricDao().getNumberForIdentityAfer(identity, action.getDate(), getMetricClass());

        //introduce a new metric and increase the quantity of the rest of the metrics
        if(numberForIdentityAfer <= 0 ){

            logger.trace("void analyze() The date is after the most recent one");
            E sqm = getMetricDao().<E>getMostRecentMetric(identity, getMetricClass());

            logger.trace("void analyze() Handling {} -> {} ",identity.getUuid(),action.getDate());
            Constructor<E> constructor = getMetricClass().getConstructor();
            E e = constructor.newInstance();
            e.setCreatedAt(action.getDate());
            e.setIdentity(identity);
            e.setQuantity(sqm == null ? 1 : sqm.getQuantity() + 1);
            e = (E) getMetricDao().insert(e);

            logger.trace("void analyze() {} = {} -> {} ",
                                new Object[]{identity.getUuid(),(sqm ==null ?0:sqm.getQuantity()),e.getQuantity()});

        }else{
            logger.trace("void analyze() The date is between and we need to correct the metrics");

            List<E> forIdentityAfer = getMetricDao().getForIdentityAfer(identity, action.getDate(), getMetricClass());

            E metric = (E) forIdentityAfer.get(0);
            Integer quantity = metric.getQuantity();

            Constructor<E> constructor = getMetricClass().getConstructor();
            E newMetric = constructor.newInstance();
            newMetric.setCreatedAt(action.getDate());
            newMetric.setIdentity(identity);
            newMetric.setQuantity(quantity);
            newMetric = (E) getMetricDao().insert(newMetric);

            quantity++;
            for(E m : forIdentityAfer){

                m.setQuantity(quantity);
                quantity++;

                getMetricDao().update(m);
            }
        }

    }

    @Override

    public void analyze(Identity identity, T action) {

        if(identity == null ){
            return;
        }

        logger.trace("void analyze() Locking");
        lock.lock();

        try{
            transactionalAnalyze(identity,action);
        } catch (NoSuchMethodException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (InvocationTargetException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (InstantiationException e) {
            logger.warn("Couldn't work with reflection {}",e);
        } catch (IllegalAccessException e) {
            logger.warn("Couldn't work with reflection {}",e);
        }finally {

            logger.trace("void analyze() unlocking");
            lock.unlock();

        }

    }


    public Class<E> getMetricClass() {
        return this.clazz;
    }

}
