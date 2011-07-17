package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */
@Repository("profileDao")
public class JpaProfileDao extends JpaCommonDao<Profile> implements ProfileDao{
    protected JpaProfileDao() {
        super(Profile.class);
    }
}
