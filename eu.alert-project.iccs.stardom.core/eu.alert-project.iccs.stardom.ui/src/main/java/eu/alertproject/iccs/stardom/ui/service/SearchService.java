package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import eu.alertproject.iccs.stardom.ui.beans.MetricResult;
import eu.alertproject.iccs.stardom.ui.beans.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 22:50
 */
@Service("searchService")
public class SearchService {

    @Autowired
    ProfileDao profileDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    MetricDao metricDao;
    
    @Transactional(readOnly = true)
    public Map<String, SearchResult> search(String query){
        
        Map<String,SearchResult> map = new LinkedHashMap<String,SearchResult>();

        List<Profile> profiles = profileDao.findByAny(query);

        //for each profile find the identity
        for(Profile p : profiles){

            Identity byProfileId = identityDao.findByProfileId(p.getId());

            if(!map.containsKey(byProfileId.getUuid())){

                map.put(byProfileId.getUuid(),
                        new SearchResult(
                                byProfileId.getId(),
                                getMetricsResultList(byProfileId),
                                getProfiles(byProfileId)
                        )
                );

            }


        }

        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, SearchResult> searchMetricQuantitative(int quantity){

        Map<String,SearchResult> map = new LinkedHashMap<String,SearchResult>();

        List<Class<? extends MetricQuantitative>> metrics =
                                                    new ArrayList<Class<? extends MetricQuantitative>>();


        metrics.add(ScmActivityMetric.class);
        metrics.add(ScmApiIntroducedMetric.class);
        metrics.add(ItsActivityMetric.class);
        metrics.add(MailingListActivityMetric.class);



        for(Class<? extends MetricQuantitative> metric : metrics){

            List<? extends MetricQuantitative> byQuantity = metricDao.findByQuantity(quantity, metric);

            for(MetricQuantitative mq: byQuantity){
                Identity identity = mq.getIdentity();


                map.put(identity.getUuid(),
                        new SearchResult(
                                    identity.getId(),
                                    getMetricsResultList(identity),
                                    getProfiles(identity)
                        )
                );
                
            }

        }


        return map;

    }
    
    private List<Profile> getProfiles(Identity identity){
        return this.<Profile>asSortedList(identity.getProfiles(),new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
    }

    private List<MetricResult> getMetricsResultList(Identity identity){
        //get metrics
        List<Class<? extends Metric>> profileMetrics =
                                                    new ArrayList<Class<? extends Metric>>();


        profileMetrics.add(ScmActivityMetric.class);
        profileMetrics.add(ScmTemporalMetric.class);
        profileMetrics.add(ScmApiIntroducedMetric.class);
        profileMetrics.add(ItsActivityMetric.class);
        profileMetrics.add(ItsTemporalMetric.class);
        profileMetrics.add(MailingListActivityMetric.class);
        profileMetrics.add(MailingListTemporalMetric.class);

        List<MetricResult> metricList = new ArrayList<MetricResult>();

        for(Class<? extends Metric> profileMetric : profileMetrics){

            Metric recentMetric = metricDao.getMostRecentMetric(identity, profileMetric);
            if(recentMetric !=null){

                metricList.add(new MetricResult(
                        recentMetric.getId(),
                        recentMetric.getLabel(),
                        ((Number)recentMetric.getValue()).intValue()
                ));
            }

        }

        return metricList;

    }
    
    @Transactional(readOnly = true)
    public SearchResult findIdentityById(Integer ids){

        Identity identity = identityDao.findById(ids);

        if(identity == null ){
            return null;
        }

        return new SearchResult(
                    identity.getId(),
                    getMetricsResultList(identity),
                    getProfiles(identity)
        );

    }

    
    private <T> List<T> asSortedList(Collection<T> c, Comparator<T> comparator) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list,comparator);
      return list;
    }
}
