package eu.alertproject.iccs.stardom.testdata.api;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 25/05/11
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public abstract class SpringDbUnitJpaTest implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(SpringDbUnitJpaTest.class);
    private IDataSet dataset;

    @Autowired
    private DataSource dataSource;
    private ApplicationContext applicationContext;


    public abstract void postConstruct();
    protected abstract String[] getDatasetFiles();


    @PostConstruct
    public void initialize(){
        ClasspathDatasetBuilder datasetBuilder = new ClasspathDatasetBuilder();
        dataset = datasetBuilder.buildClasspathDataset(getDatasetFiles());

        postConstruct();
    }



    @Before
    public void init(){
        try {
            DatabaseOperation.CLEAN_INSERT.execute(new DatabaseDataSourceConnection(dataSource), getDataSet());
        } catch (DatabaseUnitException e) {
            logger.error("An error occured", e);
            Assert.fail();
        } catch (SQLException e) {
            logger.error("An error occured", e);
            Assert.fail();
        }
    }

    @After
    public void reset(){

        try {
            DatabaseOperation.DELETE_ALL.execute(new DatabaseDataSourceConnection(dataSource), getDataSet());
        } catch (DatabaseUnitException e) {
            logger.error("An error occured", e);
            Assert.fail();
        } catch (SQLException e) {
            logger.error("An error occured", e);
            Assert.fail();
        }
    }



    protected IDataSet getDataSet(){
        return dataset;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
