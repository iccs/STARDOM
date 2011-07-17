package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:41
 */
@Repository("metricDao")
public class JpaMetricDao extends JpaCommonDao<Metric> implements MetricDao{

    private Logger logger = LoggerFactory.getLogger(JpaMetricDao.class);

    protected JpaMetricDao() {
        super(Metric.class);
    }

    @Override
    public List<Metric> getForIdentity(Identity identity) {
        return null;
    }

    /**
     * The method returns a metrics for the specified {@link Metric} subclass
     *
     * @param identity The identity to look for the metric
     * @param aClass A subclass of metric
     * @return The metric if it exists, null otherwise
     */
    @Override
    @Transactional(readOnly = true)
    public <T extends Metric> T getForIdentity(Identity identity, Class<T> aClass) {

        if(identity == null){
            return null;
        }

        Query query = getEntityManager().createQuery(
                "SELECT m FROM " + aClass.getName() + " m " +
                "WHERE m.identity.id = :id");
        query.setParameter("id",identity.getId());

        T ret = null;
        try{
            ret = aClass.cast(query.getSingleResult());
        }catch (NoResultException e){
            logger.warn("No instance of Metric({}) for Identity {} ",aClass,identity);
        }

        return ret;

    }
}
