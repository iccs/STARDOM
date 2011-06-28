package eu.alertproject.iccs.stardom.testdata.api;

import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fotis
 * Date: 25/05/11
 * Time: 14:13
 * To change this template use File | Settings | File Templates.
 */
public class ClasspathDatasetBuilder {

    private Logger logger = LoggerFactory.getLogger(ClasspathDatasetBuilder.class);
    public IDataSet buildClasspathDataset(String ...files){

        FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();

        IDataSet ret = null;

        List<IDataSet> datasets = new ArrayList<IDataSet>();
        try {
            for(String s :files){

                try {
                    FlatXmlDataSet build = flatXmlDataSetBuilder.build(this.getClass().getResourceAsStream(s));
                    datasets.add(build);
                } catch (DataSetException e) {
                    logger.warn("Couldn't add dataset file ({}) into the database ",s);
                }

            }

            ret = new CompositeDataSet(datasets.toArray(new IDataSet[]{}));
        } catch (DataSetException e) {
            logger.error("An error occured", e);
        }
        return ret;
    }

}
