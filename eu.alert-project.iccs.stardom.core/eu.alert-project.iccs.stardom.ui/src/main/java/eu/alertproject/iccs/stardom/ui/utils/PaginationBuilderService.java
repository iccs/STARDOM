package eu.alertproject.iccs.stardom.ui.utils;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 00:49
 */
@Service("paginationBuilderService")
public class PaginationBuilderService {


    private Logger logger = LoggerFactory.getLogger(PaginationBuilderService.class);

    /**
     * <p>This method creates a pagination map with page numbers
     * the view. The map contains
     * </p>
     * <ul>
     *     <li>first = 1</li>
     *     <li>pages = 1,2,3...</li>
     *     <li>last = totalPages-1</li>
     * </ul>
     *
     * <p>
     *     The idea taken from
     *     http://stackoverflow.com/questions/163809/smart-pagination-algorithm
     * </p>
     *
     * @param selectedPage The currently selected page, can't be null
     * @return
     */
    public Map<String, Object> buildPages(
            Integer selectedPage,
            int totalPages,
            int depthSize
    ){


        logger.trace("Map<String,Object> buildPages(selectedPage= {},totalPages={},depthSize={})",new Object[]{selectedPage,totalPages,depthSize});
        Map<String,Object> pagination = new HashMap<String, Object>();


        // depthSize*2 = Left an right number of pages
        // +1 The selected page
        //protect the return size
        int returnSize = Math.min(((depthSize*2)+1),totalPages);

        Integer[] pages = new Integer[returnSize];

        /*
          * If the selected page is less that the depthSize,
          * there is no need to scroll the bottom results
         */
        if(selectedPage <= depthSize+1){
            logger.trace("Map<String,Object> buildPages(pages) Keeping to left side no need to scroll ");
            //no need to scroll the results
            for(int i =1; i <= returnSize ; i++){
                pages[i-1]=i;
            }
        }else if( selectedPage > totalPages - (depthSize*2)){

            logger.trace("Map<String,Object> buildPages(pages) Working from right <- left, not enough spaces on the right {}",returnSize);

            //from right to left
            for(int i =0 ; i < returnSize; i++){
                pages[i] = (totalPages-returnSize+1)+i;
            }


        }else{
            //it is in the middle
            //get the previous four numbers and the next four numbers

            pages[depthSize]=selectedPage;
            for(int i=0; i< depthSize; i++){

                pages[i]= selectedPage-(depthSize-i);
                pages[depthSize+1+i] = selectedPage+1+i;
            }



        }


        pagination.put("first",1);
        pagination.put("pages",pages);
        pagination.put("last",totalPages);
        pagination.put("selected",selectedPage);

        return pagination;
    }
}
