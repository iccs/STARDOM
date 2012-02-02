package eu.alertproject.iccs.stardom.identifier.api;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * User: fotis
 * Date: 22/01/12
 * Time: 13:41
 */
@Service("mergeService")
public class IdentityMergeService {

    
    @Autowired
    IdentityDao identityDao;

    /**
     * This function merges one or more identities into a single one
     * 
     * The result will be an entirely new identity made up of all
     * of them
     * 
     * @param ids
     */
    @Transactional
    public void merge(Integer... ids){


        Identity first = null;
        
        List<Integer> deleteIds = new ArrayList<Integer>();
        for(Integer id : ids){

            // here we are working against the first identity
            if(first == null ){
                first = identityDao.findById(id);
                continue;
            }



            Identity byId = identityDao.findById(id);
            
            
            //join the profilse
            Iterator<Profile> iterator = byId.getProfiles().iterator();

            while(iterator.hasNext()){

                Profile p = iterator.next();
                first.getProfiles().add(p);
                iterator.remove();
            }


            Iterator<Metric> metricIterator = byId.getMetrics().iterator();

            while(metricIterator.hasNext()){
                Metric m = metricIterator.next();
                
                if(m instanceof MetricQuantitative){
                    
                    boolean metricFound = false;
                    for(Metric om : first.getMetrics()){

                        if(m.getClass().equals(om.getClass())){
                            
                            ((MetricQuantitative)om).setQuantity(((MetricQuantitative) om).getQuantity()+((MetricQuantitative) m).getQuantity());

                            /*
                            Make sure that the date is updated on the quatity
                             */
                            Date omCreatedAt = om.getCreatedAt();
                            Date createdAt = m.getCreatedAt();
                            
                            if(createdAt.after(omCreatedAt)){
                                om.setCreatedAt(createdAt);
                            }
                            metricFound = true;
                        }
                    }

                    /*
                    Because we are looking only at the first instance metrics, we need to add
                    the metrics that are not immediately available from the second identity
                     */
                    if(!metricFound){

                        m.setIdentity(first);
                        first.getMetrics().add(m);
                        
                        metricIterator.remove();

                    }

                }else if(m instanceof MetricTemporal){

                    boolean metricFound = false;

                    for(Metric om : first.getMetrics()){
                    
                        if(m.getClass().equals(om.getClass())){

                            Date omTemporal = ((MetricTemporal) om).getTemporal();
                            Date mTemporal = ((MetricTemporal) m).getTemporal();

                            
                            if(mTemporal.after(omTemporal)){
                                ((MetricTemporal) om).setTemporal(mTemporal);
                            }
                            
                            Date omCreatedAt = om.getCreatedAt();
                            Date createdAt = m.getCreatedAt();
                            if(createdAt.after(omCreatedAt)){
                                om.setCreatedAt(createdAt);
                            }

                            metricFound=true;
                        }

                    }

                    if(!metricFound){
                        
                        m.setIdentity(first);
                        first.getMetrics().add(m);
                        metricIterator.remove();
                    }
                }
            }

            /*
            This should remove profiles and metrics!!!!
             */
//            deleteIds.add(id);
            identityDao.update(first);
            identityDao.delete(id);
        }


        //delete the rest

//        for(Integer id : deleteIds){
//            identityDao.delete(id);
//        }
//
//
        
    }
}
