package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import com.mysema.query.jpa.impl.JPAQuery;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.QIdentity;
import eu.alertproject.iccs.stardom.domain.api.QProfile;
import org.apache.commons.lang.StringUtils;
import org.springframework.expression.spel.ast.QualifiedIdentifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 28/06/11
 * Time: 09:43
 * To change this template use File | Settings | File Templates.
 */
@Repository("identityDao")
public class JpaIdentityDao extends JpaCommonDao<Identity> implements IdentityDao{
    protected JpaIdentityDao() {
        super(Identity.class);
    }

    @Override
    @Transactional
    public Identity insert(Identity bean) {
        Identity insert = super.insert(bean);//To change body of overridden methods use File | Settings | File Templates.
        insert.setUuid(org.apache.commons.codec.digest.DigestUtils.md5Hex(String.valueOf(insert.getId())));
        return insert;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Identity> findPossibleMatches(Profile profile) {

        JPAQuery q = new JPAQuery(getEntityManager());
        QIdentity qi = QIdentity.identity;
        QProfile qp = QProfile.profile;

        q.from(qi);
        q.leftJoin(qi.profiles,qp);

        if(!StringUtils.isEmpty(profile.getName())){
            q.where(qp.name.toUpperCase().eq(profile.getName().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getLastname())){
            q.where(qp.lastname.toUpperCase().eq(profile.getLastname().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getUsername())){
            q.where(qp.username.toUpperCase().eq(profile.getUsername().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getEmail())){
            q.where(qp.email.toUpperCase().eq(profile.getEmail().toUpperCase()));
        }
//
//
//
//
//        Query query = getEntityManager().createQuery(
//                "SELECT DISTINCT i FROM Identity i " +
//                        " LEFT JOIN i.profiles p"+
//                        " WHERE    " +
//                        "       UPPER(p.email) = :email " +
//                        " OR    UPPER(p.name) = :name" +
//                        " OR    UPPER(p.lastname) = :lastname " +
//                        " OR    UPPER(p.username) = :username");
//
//
//
//
//        query.setParameter("email",profile.getEmail().toUpperCase());
//        query.setParameter("name",profile.getName().toUpperCase());
//        query.setParameter("lastname",profile.getLastname().toUpperCase());
//        query.setParameter("username",profile.getUsername().toUpperCase());


        List<Identity> list = q.list(qi);
        return list;
    }
}
