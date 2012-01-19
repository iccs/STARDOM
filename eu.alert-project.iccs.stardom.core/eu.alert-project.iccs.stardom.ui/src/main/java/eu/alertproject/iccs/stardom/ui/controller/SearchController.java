package eu.alertproject.iccs.stardom.ui.controller;

import eu.alertproject.iccs.stardom.domain.api.Metric;
import eu.alertproject.iccs.stardom.ui.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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


    @Autowired
    SearchService searchService;
    
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
    
    
}
