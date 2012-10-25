package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.ui.beans.CIResults;
import eu.alertproject.iccs.stardom.ui.service.DefaultIdentityMergeService;
import eu.alertproject.iccs.stardom.ui.beans.SearchResult;
import eu.alertproject.iccs.stardom.ui.service.MergeService;
import eu.alertproject.iccs.stardom.ui.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: fotis
 * Date: 18/01/12
 * Time: 20:12
 */

@Controller
@RequestMapping("/search")
public class SearchController {

    private Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;
    
    @Autowired
    MergeService mergeService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("search");
        mv.addObject("query", "");
        mv.addObject("results", null);
        return mv;
    }


    @RequestMapping(value = "/{query}", method = RequestMethod.GET)
    public @ResponseBody ModelMap search(@PathVariable("query") String query){

        Map<String,List<Metric>> map = new LinkedHashMap<String,List<Metric>>();
        
        ModelMap m = new ModelMap();
        m.put("query", query);
        m.put("results","");

        query = StringUtils.trimToEmpty(query);
        if(StringUtils.isEmpty(query) || query.length() <3){
            return m;
        }

        m.put("results", searchService.search(query));


        return m;

    }

    @RequestMapping(value = "/{uuid}/uuid", method = RequestMethod.GET)
    public @ResponseBody CIResults searchByUuid(@PathVariable("uuid") String uuid){
        return searchService.searchByUuid(uuid);
    }

    @RequestMapping(value = "/quantitative/{query}", method = RequestMethod.GET)
    public @ResponseBody ModelMap searchQuantitative(@PathVariable("query") Integer query){

        Map<String,List<Metric>> map = new LinkedHashMap<String,List<Metric>>();

        ModelMap m = new ModelMap();
        m.put("query", query);
        m.put("results","");

        if(query <= 20 ){
            return m;
        }

        m.put("results", searchService.searchMetricQuantitative(query));


        return m;

    }


    @RequestMapping(value = "/merge", method = RequestMethod.POST)
    public @ResponseBody ModelMap merge(@RequestParam("ids") Integer[] ids){
        
        ModelMap m = new ModelMap();
        if(ids ==null || ids.length <= 0 ){
            m.put("merged","");
            return m;
        }

        logger.trace("ModelMap merge(ids) {} ",ids);

        mergeService.merge(ids);
        
        //get the first one
        Integer id = ids[0];

        SearchResult identityById = searchService.findIdentityById(id);
        m.put("merged",identityById);
        
        return m;

    }
    
    
}
