package eu.alertproject.iccs.stardom.datastore.internal.dao;

import com.existanze.libraries.orm.dao.JpaCommonDao;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.QIdentity;
import eu.alertproject.iccs.stardom.domain.api.QProfile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: fotis
 * Date: 28/06/11
 * Time: 09:43
 */
@Repository("identityDao")
public class JpaIdentityDao extends JpaCommonDao<Identity> implements IdentityDao{

    private Logger logger = LoggerFactory.getLogger(JpaIdentityDao.class);


    protected JpaIdentityDao() {
        super(Identity.class);
    }

    @Override
    public Identity insert(Identity bean) {
        Identity insert = super.insert(bean);//To change body of overridden methods use File | Settings | File Templates.
        insert.setUuid(UUID.randomUUID().toString());
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

        List<BooleanExpression> be = new ArrayList<BooleanExpression>();

        if(!StringUtils.isEmpty(profile.getName())){
            be.add(qp.name.toUpperCase().eq(profile.getName().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getLastname())){
            be.add(qp.lastname.toUpperCase().eq(profile.getLastname().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getUsername())){
            be.add(qp.username.toUpperCase().eq(profile.getUsername().toUpperCase()));
        }

        if(!StringUtils.isEmpty(profile.getEmail())){
            be.add(qp.email.toUpperCase().eq(profile.getEmail().toUpperCase()));
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

        if(be.size() <=0){
            return new ArrayList<Identity>();
        }
        
        q.where(BooleanExpression.anyOf(be.toArray(new BooleanExpression[be.size()])));
        q.distinct();

        return q.list(qi);
    }


    @SuppressWarnings({"unchecked"})
    @Override
    public List<Identity> findAllPaginableOrderByLastName(int from, int pageSize) {

        JPAQuery q = new JPAQuery(getEntityManager());
        QIdentity qi = QIdentity.identity;
        QProfile qp = QProfile.profile;

        q.from(qi);
        q.leftJoin(qi.profiles,qp);
        q.orderBy(qp.lastname.asc());
        q.limit(pageSize);
        q.offset(from);


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

        q.distinct();

        return q.list(qi);
    }

    @Override
    public Identity findByProfileId(Integer profileId) {

        Query query = getEntityManager().createQuery(
                "SELECT i FROM Identity  i " +
                        " JOIN i.profiles p " +
                        " WHERE p.id = :id"
        );

        query.setParameter("id",profileId);
        Identity i = null;

        try {
            i = (Identity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Couldn't find identity for profile_id={}",profileId);
        }
        
        return i;

    }

    @Override
    public Identity findByProfileUuid(String uuid) {

        Query query = getEntityManager().createQuery(
                "SELECT i FROM Identity  i  WHERE i.uuid = :uuid"
        );

        query.setParameter("uuid",uuid);

        Identity i = null;

        try {
            i = (Identity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Couldn't find identity for uuid={}",uuid);
        }

        return i;

    }

    @Override
    public Identity findByEmail(String email) {

        Query query = getEntityManager().createQuery(
                "SELECT i FROM Identity  i " +
                        " JOIN i.profiles p " +
                        " WHERE p.email = :email"
        );

        query.setParameter("email",email);

        Identity i = null;

        try {
            i = (Identity) query.getSingleResult();
        } catch (NoResultException e) {
            logger.warn("Couldn't find identity for email={}",email);
        }

        return i;

    }
}
