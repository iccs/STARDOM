package src.test.java.eu.alertproject.iccs.stardom.ui.utils;

import jsr166y.LinkedTransferQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.alertproject.iccs.stardom.ui.utils.PaginationBuilderService;

import java.util.Map;

/**
 * User: fotis
 * Date: 18/07/11
 * Time: 00:52
 */

public class PaginationBuilderServiceTest  {

    private Logger logger = LoggerFactory.getLogger(PaginationBuilderServiceTest.class);
    private PaginationBuilderService paginationBuilderService;

    @Before
    public void init(){

        paginationBuilderService = new PaginationBuilderService();
    }

    @Test
    public void testPaginationFitsLeft(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(3, 15, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(3,selected, 0);
        Assert.assertEquals(9, pages.length);

        // 1,2,[3],4,5,6,7,8, 9 | 10,11,12,13,14,15
        testPages(pages,
                new Integer[]{
                        1,2,3,4,5,6,7,8,9
                });

    }

    @Test
    public void testTotalCalculatedPagesLessThanTotal(){
        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(3, 5, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(3,selected, 0);
        Assert.assertEquals(5, pages.length);

        // 12[3]45
        testPages(pages,
                new Integer[]{
                        1,2,3,4,5
                });
    }

    @Test
    public void testEdgeCaseCenter(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(5, 10, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(5,selected, 0);
        Assert.assertEquals(9, pages.length);

        // 1234[5]6789
        testPages(pages,
                new Integer[]{
                      1,2,3,4,5,6,7,8,9
                });

    }

    @Test
    public void testScrollRightFitSize(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(6, 10, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(6,selected, 0);
        Assert.assertEquals(9, pages.length);

        // 2,3,4,5,[6],7,8,9,10
        testPages(pages,
                new Integer[]{
                      2,3,4,5,6,7,8,9,10
                });

    }

    @Test
    public void testScrollRightNotFitSize(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(9, 12, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(9,selected, 0);
        Assert.assertEquals(9, pages.length);

        // 1,2,3,| 4,5,6,7,8,[9],10,11,12 |
        testPages(pages,
                new Integer[]{
                      4,5,6,7,8,9,10,11,12
                });

    }


    @Test
    public void testEnd(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(13, 15, 6);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(13,selected, 0);
        Assert.assertEquals(13, pages.length);

        // 1,2,| 3,4,5,6,7,8,9,10,11,12,[13],14,15 |
        testPages(pages,
                new Integer[]{
                      3,4,5,6,7,8,9,10,11,12,13,14,15
                });

    }


    @Test
    public void testProductionCase(){

        Map<String,Object> stringObjectMap = paginationBuilderService.buildPages(19, 166, 4);

        Integer[] pages = (Integer[]) stringObjectMap.get("pages");
        Integer selected = (Integer) stringObjectMap.get("selected");


        Assert.assertEquals(19,selected, 0);
        Assert.assertEquals(9, pages.length);

        testPages(pages,
                new Integer[]{
                      15,16,17,18,19,20,21,22,23
                });

    }


    private void testPages(Integer[] actual, Integer[] pages){

        Assert.assertTrue(actual.length == pages.length);

        logger.trace("void testPages(actual) {} ",actual);
        Assert.assertArrayEquals(pages,actual);

    }
}
