package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;
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
    public <T extends Metric> List<T> findAll(Class<T> aClass) {

        Query query = getEntityManager().createQuery("SELECT m FROM " + aClass.getName() + " m ");
        return query.getResultList();

    }


    @Override
    public List<Metric> getForIdentity(Identity identity) {
        if(identity == null){
            return null;
        }
        Query query = getEntityManager().createQuery(
                "SELECT m FROM Metric m " +
                "WHERE m.identity.id = :id");
        query.setParameter("id",identity.getId());


        return query.getResultList();
    }

    /**
     * The method returns a metrics for the specified {@link Metric} subclass
     *
     * @param identity The identity to look for the metric
     * @param aClass A subclass of metric
     * @return The metric if it exists, null otherwise
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public <T extends Metric> List<T> getForIdentity(Identity identity, Class<T> aClass) {

        if(identity == null){
            return null;
        }

        Query query = getEntityManager().createQuery(
                "SELECT m FROM " + aClass.getName() + " m " +
                "WHERE m.identity.id = :id " +
                "ORDER BY m.createdAt DESC");
        query.setParameter("id",identity.getId());

        return query.getResultList();

    }

    @Override
    public <T extends Metric> List<T> getForIdentityAfer(Identity identity, Date date, Class<T> aClass) {
        
        Query query = getEntityManager().createQuery(
                "SELECT m FROM " + aClass.getName() + " m " +
                "WHERE m.identity.id = :id " +
                "AND m.createdAt >= :date " +
                "ORDER BY m.createdAt,m.id DESC");

        query.setParameter("id",identity.getId());
        query.setParameter("date",date);


        return query.getResultList();

    }

    @Override
    public <T extends Metric> Integer getNumberForIdentityAfer(Identity identity, Date date, Class<T> aClass) {

        Query query = getEntityManager().createQuery(
                "SELECT COUNT(m) FROM " + aClass.getName() + " m " +
                "WHERE m.identity.id = :id " +
                "AND m.createdAt >= :date " +
                "ORDER BY m.createdAt,m.id DESC");

        query.setParameter("id",identity.getId());
        query.setParameter("date",date);

        return ((Number)query.getSingleResult()).intValue();

    }



    @Override
    public <T extends Metric> T getMostRecentMetric(Identity identity, Class<T> aClass) {

        if(identity == null){
            return null;
        }
        logger.trace("T getMostRecentMetric() {} ",aClass.getName());

        Query query = getEntityManager().createQuery(
                "SELECT m FROM " + aClass.getName() + " m " +
                "WHERE m.identity.id = :id " +
                "ORDER BY m.createdAt DESC");

        query.setMaxResults(1);
        query.setParameter("id",identity.getId());

        T ret = null;
        try{
            ret = aClass.cast(query.getSingleResult());
        }catch (NoResultException e){
            logger.warn("No instance of Metric({}) for Identity {} ",aClass,identity);
        }

        return ret;

    }

    /**
     * The method returns a metrics for the specified {@link Metric} subclass
     *
     * @param quantity The amount the metric has to match or be greater than in order to be eligible
     * @param aClass A subclass of metric
     * @return The metric if it exists, null otherwise
     */
    @SuppressWarnings({"unchecked"})
    @Override
    public <T extends MetricQuantitative> List<T> findByQuantity(int quantity, Class<T> aClass) {


        Query query = getEntityManager().createQuery(
                "SELECT m FROM " + aClass.getName() + " m " +
                "WHERE m.quantity >= :quantity");
        query.setParameter("quantity",quantity);

        return query.getResultList();

    }
}
