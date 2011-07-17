package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.ui.utils.PaginationBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
        mv.addObject("identities", identityDao.findAllPaginableOrderByLastName(
                offest * max,
                max
        ));


        mv.setViewName(page == 0? null : "identities");
        return mv;
    }

}
