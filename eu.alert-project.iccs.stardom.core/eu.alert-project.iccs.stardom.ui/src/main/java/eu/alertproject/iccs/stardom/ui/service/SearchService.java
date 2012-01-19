package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
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
    
    @Transactional
    public Map<String, SearchResult> search(String query){
        
        Map<String,SearchResult> map = new LinkedHashMap<String,SearchResult>();

        List<Class<? extends Metric>> metrics =
                                            new ArrayList<Class<? extends Metric>>();


        metrics.add(ScmActivityMetric.class);
        metrics.add(ScmTemporalMetric.class);
        metrics.add(ScmApiIntroducedMetric.class);
        metrics.add(ItsActivityMetric.class);
        metrics.add(ItsTemporalMetric.class);
        metrics.add(MailingListActivityMetric.class);
        metrics.add(MailingListTemporalMetric.class);

        List<Profile> profiles = profileDao.findByAny(query);

        //for each profile find the identity
        for(Profile p : profiles){

            
            Identity byProfileId = identityDao.findByProfileId(p.getId());


            byProfileId.getMetrics();
            if(!map.containsKey(byProfileId.getUuid())){

                List<MetricResult> metricList = new ArrayList<MetricResult>();
                for(Class<? extends Metric> metric : metrics){

                    Metric recentMetric = metricDao.getMostRecentMetric(byProfileId, metric);
                    if(recentMetric !=null){

                        metricList.add(new MetricResult(
                                recentMetric.getId(),
                                recentMetric.getLabel(),
                                ((Number)recentMetric.getValue()).intValue()
                        ));
                    }

                }

                List<Profile> profiles2 = this.<Profile>asSortedList(byProfileId.getProfiles(),new Comparator<Profile>() {
                    @Override
                    public int compare(Profile o1, Profile o2) {
                        return o1.getId().compareTo(o2.getId());
                    }
                });


                map.put(byProfileId.getUuid(),
                        new SearchResult(
                                    byProfileId.getUuid(),
                                    metricList,
                                    profiles2
                        )
                );

            }


        }

        return map;
    }


    private <T> List<T> asSortedList(Collection<T> c, Comparator<T> comparator) {
      List<T> list = new ArrayList<T>(c);
      java.util.Collections.sort(list,comparator);
      return list;
    }
}
