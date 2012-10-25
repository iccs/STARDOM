package eu.alertproject.iccs.stardom.ui.service;

import eu.alertproject.iccs.events.stardom.StardomCIUpdatePayload;
import eu.alertproject.iccs.stardom.classification.CI;
import eu.alertproject.iccs.stardom.classification.CIUtils;
import eu.alertproject.iccs.stardom.constructor.api.CiCalculatorService;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.ProfileDao;
import eu.alertproject.iccs.stardom.datastore.api.metrics.MetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.MostRecentMetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.NumberMetricValueStrategy;
import eu.alertproject.iccs.stardom.datastore.api.metrics.TemporalMetricValueStrategy;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.MetricQuantitative;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import eu.alertproject.iccs.stardom.ui.beans.CIResults;
import eu.alertproject.iccs.stardom.ui.beans.MetricResult;
import eu.alertproject.iccs.stardom.ui.beans.SearchResult;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 22:50
 */
@Service("searchService")
public class SearchService {

    private Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    ProfileDao profileDao;

    @Autowired
    IdentityDao identityDao;

    @Autowired
    CiCalculatorService ciCalculatorService;

    @Autowired
    MetricDao metricDao;
    private HashMap<String, MetricValueStrategy<? extends Metric>> metrics;
    private CI ci;


    @PostConstruct
    public void init() throws IOException {

        ci = CIUtils.loadFromDefaultLocation();
        //get the metrics for each
        metrics = new HashMap<String,MetricValueStrategy<? extends Metric>>();

        metrics.put("ScmActivityMetric", new NumberMetricValueStrategy<ScmTemporalMetric>(ScmTemporalMetric.class));
        metrics.put("ScmApiIntroducedMetric", new NumberMetricValueStrategy<ScmApiIntroducedMetric>(ScmApiIntroducedMetric.class));
        metrics.put("ScmTemporalMetric", new TemporalMetricValueStrategy<ScmTemporalMetric>(ScmTemporalMetric.class));

        metrics.put("ItsActivityMetric", new NumberMetricValueStrategy<ItsTemporalMetric>(ItsTemporalMetric.class));
        metrics.put("ItsIssuesResolvedMetric", new MostRecentMetricValueStrategy<ItsIssuesResolvedMetric>(ItsIssuesResolvedMetric.class));
        metrics.put("ItsTemporalMetric", new TemporalMetricValueStrategy<ItsTemporalMetric>(ItsTemporalMetric.class));

        metrics.put("CommunicationActivityMetric", new TemporalMetricValueStrategy<MailingListTemporalMetric>(MailingListTemporalMetric.class));
        metrics.put("CommunicationTemporalMetric", new NumberMetricValueStrategy<MailingListTemporalMetric>(MailingListTemporalMetric.class));

    }
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
    public CIResults searchByUuid(String uuid){

        Identity byProfileUuid = identityDao.findByProfileUuid(uuid);
        //get the ci
        List<CI.Classifier> classifiers = ci.getClassifiers();

        Map<String, Double> ciForClass = new HashMap<String, Double>();
        Map<String, Integer> metricsValues = new HashMap<String, Integer>();

        for(CI.Classifier classifier : classifiers){

            List<CI.Classifier.Metric> classifierMetrics = classifier.getMetrics();

            Double prob = classifier.getPrior();

            for(CI.Classifier.Metric cim: classifierMetrics){

                try {
                    if(metrics.containsKey(cim.getName())){

                        Integer value = metrics.get(cim.getName()).getValue(metricDao, byProfileUuid);
                        metricsValues.put(cim.getName(),value);

                        if(cim.getStandardDeviation() > 0.0){
                            NormalDistribution d = new NormalDistributionImpl(cim.getMean(),cim.getStandardDeviation());
                            if(value >= cim.getMean()){
                                prob *= d.density(cim.getMean());
                            }else{
                                prob *= d.density(Double.valueOf(value));
                            }
                        }else{
                            prob *=1;
                        }

                    }
                } catch (RuntimeException e) {
                    logger.warn("The metric standard deviation is incorrect",e);
                    prob=0.0;
                } finally {
                }
            }

            ciForClass.put(classifier.getName(),prob);

        }


        return new CIResults(
            byProfileUuid.getUuid(),
            getMetricsResultList(byProfileUuid),
            getProfiles(byProfileUuid),
            ciForClass
        );

    }



    @Transactional(readOnly = true)
    public Map<String, SearchResult> searchMetricQuantitative(int quantity){

        Map<String,SearchResult> map = new LinkedHashMap<String,SearchResult>();

        List<Class<? extends MetricQuantitative>> metrics =
                                                    new ArrayList<Class<? extends MetricQuantitative>>();


        metrics.add(ScmActivityMetric.class);
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
        Set<String> strings = metrics.keySet();

        int id = 0;
        for(String key: strings){

            MetricValueStrategy<? extends Metric> metricValueStrategy = metrics.get(key);

            Integer value = metricValueStrategy.getValue(metricDao, identity);

            metricList.add(new MetricResult(
                        id++,
                        key,
                        value
                ));
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
