package eu.alertproject.iccs.stardom.datastore.api.dao;

import com.existanze.libraries.orm.dao.CommonDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;

import java.util.List;

/**
 * User: fotis
 * Date: 15/07/11
 * Time: 21:41
 */
public interface MetricDao extends CommonDao<Metric>{

    public List<Metric> getForIdentity(Identity identity);

}
