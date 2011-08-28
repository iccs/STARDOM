package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import eu.alertproject.iccs.stardom.ui.beans.IdentityBean;
import eu.alertproject.iccs.stardom.ui.utils.PaginationBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * User: fotis
 * Date: 17/07/11
 * Time: 16:34
 */
@Controller
public class IndexController {

    @Autowired
    PaginationBuilderService paginationBuilderService;

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IdentityDao identityDao;

    @Autowired
    private MetricDao metricDao;

    private int max;
    private Integer pagesSize;

    @PostConstruct
    public void init(){

        this.max = Integer.valueOf(systemProperties.get("ui.page.size").toString());
        this.pagesSize = Integer.valueOf(systemProperties.get("ui.paginator.size").toString());


    }


    @RequestMapping(value = "/identities", method = RequestMethod.GET)
    public ModelAndView index(){
        return getIdentities(1);
    }


    @RequestMapping(value = "/identities/{page}", method = RequestMethod.GET)
    public ModelAndView index(@PathVariable("page") Integer page){
        return getIdentities(page);
    }

    private Map<String, Object> buildPages(int selectedPage){

        int count = identityDao.count();
        int numberOfPages=(int) Math.ceil(count/max);


        Map<String,Object> pagination = paginationBuilderService.buildPages(
                selectedPage,
                numberOfPages,
                pagesSize);

        return pagination;
    }

    private ModelAndView getIdentities(
            Integer page
    ){
        int offest = page - 1;

        ModelAndView mv = new ModelAndView();


        mv.addObject("pagination", buildPages(page));
        mv.addObject("selected", page );


        /*
         * The code bellow should be in an external service
         */

        List<Identity> allPaginableOrderByLastName = identityDao.findAllPaginableOrderByLastName(
                offest * max,
                max
        );

        List<IdentityBean> beans = new ArrayList<IdentityBean>();



        List<Class<? extends Metric>> metrics =
                new ArrayList<Class<? extends Metric>>();


        metrics.add(ScmActivityMetric.class);
        metrics.add(ScmTemporalMetric.class);
        metrics.add(ItsActivityMetric.class);
        metrics.add(ItsTemporalMetric.class);
        metrics.add(MailingListActivityMetric.class);
        metrics.add(MailingListTemporalMetric.class);

        for(Identity i : allPaginableOrderByLastName){

            IdentityBean identityBean = new IdentityBean();
            identityBean.setIdentity(i);


            //for all of the possible metrics build the metrics array

            List<Metric> beanMetrics = identityBean.getMetrics();
            for(Class<? extends Metric> m : metrics){
                Metric recentMetric = metricDao.getMostRecentMetric(i, m);
                if(recentMetric != null ){
                    beanMetrics.add(recentMetric);
                }
            }


            beans.add(identityBean);
        }

        mv.addObject("identities", beans);


        mv.setViewName(page == 0? null : "identities");
        return mv;
    }

}
