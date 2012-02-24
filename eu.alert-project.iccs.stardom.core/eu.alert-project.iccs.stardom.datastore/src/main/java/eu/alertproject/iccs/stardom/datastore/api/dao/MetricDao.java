package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:41
 */
public interface MetricDao extends CommonDao<Metric>{

    public List<Metric> getForIdentity(Identity identity);
    public <T extends Metric> List<T> getForIdentity(Identity identity, Class<T> aClass);
    public <T extends Metric> T getMostRecentMetric(Identity identity, Class<T> aClass);


    @SuppressWarnings({"unchecked"})
    <T extends MetricQuantitative> List<T> findByQuantity(int quantity, Class<T> aClass);
    <T extends Metric> List<T> getForIdentityAfer(Identity identity, Date date, Class<T> aClass);

    <T extends Metric> Integer getNumberForIdentityAfer(Identity identity, Date date, Class<T> aClass);
}
