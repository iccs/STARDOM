package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:41
 */
@Repository("metricDao")
public class JpaMetricDao extends JpaCommonDao<Metric> implements MetricDao{

    protected JpaMetricDao() {
        super(Metric.class);
    }

    @Override
    public List<Metric> getForIdentity(Identity identity) {
        throw new NotImplementedException();

    }
}
