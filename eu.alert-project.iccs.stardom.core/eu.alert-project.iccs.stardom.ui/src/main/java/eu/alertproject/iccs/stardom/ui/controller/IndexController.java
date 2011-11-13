package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.constructor.api.CiCalculatorService;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.datastore.api.dao.MetricDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.domain.api.metrics.*;
import eu.alertproject.iccs.stardom.ui.beans.IdentityBean;
import eu.alertproject.iccs.stardom.ui.utils.PaginationBuilderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    PaginationBuilderService paginationBuilderService;

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IdentityDao identityDao;

    @Autowired
    private MetricDao metricDao;

    @Autowired
    private CiCalculatorService ciCalculatorService;

    private int max;
    private Integer pagesSize;

    private int current=1;
    private int numberOfPages;

    @PostConstruct
    public void init(){

        this.max = Integer.valueOf(systemProperties.get("ui.page.size").toString());
        this.pagesSize = Integer.valueOf(systemProperties.get("ui.paginator.size").toString());


    }

    @RequestMapping(value="/ui/list/previous", method = RequestMethod.GET)
    public ModelAndView previous(){
        if(current <=1){
            return list(current);
        }

        return list(current-1);
    }

    @RequestMapping(value="/ui/list/next", method = RequestMethod.GET)
    public ModelAndView next(){

        if(current > numberOfPages - 1 ){
            return list(current);
        }

        return list(current+1);
    }

    @RequestMapping(value="/ui/list/current", method = RequestMethod.GET)
    public ModelAndView current(){
        return list(current);
    }

    @RequestMapping(value = "/ui/list/{page}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable("page") Integer page){

        int offest = page - 1;

        ModelAndView mv = new ModelAndView();

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

            double ci = 0.0;
            int scm = 0;
            int its = 0;
            int mailing =0;
            for(Class<? extends Metric> m : metrics){
                Metric recentMetric = metricDao.getMostRecentMetric(i, m);

                if(recentMetric != null ){
                    if(m == ScmActivityMetric.class){
                        scm = ((Number)recentMetric.getValue()).intValue();
                    }else if(m == ItsActivityMetric.class){
                        its = ((Number)recentMetric.getValue()).intValue();
                    }else if(m == MailingListActivityMetric.class){
                        mailing=((Number)recentMetric.getValue()).intValue();
                    }

                    beanMetrics.add(recentMetric);
                }

            }

            identityBean.setCi(ciCalculatorService.ci(scm,its,mailing));



            beans.add(identityBean);
        }

        mv.addObject("identities", beans);
        mv.addObject("current",page);
        mv.addObject("total",numberOfPages);
        mv.setViewName(page == 0? null : "ui/list");

        current=page;
        return mv;

    }

    private Map<String, Object> buildPages(int selectedPage){

        int count = identityDao.count();
        numberOfPages =(int) Math.ceil(count/max);


        Map<String,Object> pagination = paginationBuilderService.buildPages(
                selectedPage,
                numberOfPages,
                pagesSize);

        return pagination;
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    private ModelAndView index(){

        ModelAndView mv = new ModelAndView();
        mv.addObject("pagination", buildPages(1));
        mv.addObject("selected", 1 );
        mv.setViewName("page");
        return mv;

    }

    @RequestMapping(value="/pagination", method = RequestMethod.GET)
    private @ResponseBody Map<String,Object> getCurrentPagination(){
        return buildPages(current);

    }
}
