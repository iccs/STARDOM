package eu.alertproject.iccs.stardom.processing.services;

import eu.alertproject.iccs.events.api.EventFactory;
import eu.alertproject.iccs.events.stardom.IdentityPersons;
import eu.alertproject.iccs.events.stardom.StardomIdentityNewPayload;
import eu.alertproject.iccs.stardom.datastore.api.dao.IdentityDao;
import eu.alertproject.iccs.stardom.domain.api.Identity;
import eu.alertproject.iccs.stardom.domain.api.Profile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 10/27/12
 * Time: 12:39 AM
 */
@Service("identityService")
public class IdentityServiceImpl implements IdentityService{

    private Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);

    @Autowired
    IdentityDao identityDao;

    private Integer eventId =0;

    @Override
    @Transactional(readOnly = true)
    public Integer buildXmlEvents(String outputPath) throws IOException {


        File f = new File(outputPath);

        if(f.exists()){
            //delete
            FileUtils.deleteDirectory(f);
        }


        if(!f.mkdir()){
            throw new IllegalArgumentException("Couldn't create output directory");
        }


        List<Identity> all = identityDao.findAll();



        long start = System.currentTimeMillis();
        for(Identity identity : all){

            Set<Profile> profiles = identity.getProfiles();

            List<IdentityPersons.Persons.Person> persons = new ArrayList<IdentityPersons.Persons.Person>();
            List<String> processedUris = new ArrayList<String>();

            for(Profile p : profiles){

                if(!StringUtils.isEmpty(p.getUri()) && !processedUris.contains(p.getUri())){

                    IdentityPersons.Persons.Person person = new IdentityPersons.Persons.Person();
                    person.setUri(p.getUri());
                    persons.add(person);
                    processedUris.add(p.getUri());

                }
            }


            if(persons.size() >0){


                String stardomIdentityNew = EventFactory.createStardomIdentityNew(
                        eventId++,
                        start,
                        System.currentTimeMillis(),
                        eventId,
                        new StardomIdentityNewPayload.EventData.Identity(
                                identity.getUuid(),
                                new IdentityPersons(
                                        persons,
                                        null
                                )

                        )


                );



                try {
                    IOUtils.write(
                            stardomIdentityNew,
                            new FileOutputStream(
                                    new File(f, "ALERT.STARDOM.IdentityNew-"+StringUtils.leftPad(eventId+"",5,"0")+".xml"))
                    );
                } catch (IOException e) {
                    logger.warn("Couldn't create event into {} ",f.getAbsolutePath());
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }


        }


        return eventId;

    }
}
