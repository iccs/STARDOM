<?php

namespace Iccs\StardomBundle\Command;

use Doctrine\ORM\Mapping\MappingException;
use Symfony\Component\Console\Input\InputOption;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Command\Command;
use Symfony\Bundle\DoctrineBundle\Command\DoctrineCommand;
use Iccs\StardomBundle\Entity\Scmlog;
use Iccs\StardomBundle\Entity\People;

    
class ScmCommand extends DoctrineCommand{


    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure() {
        $this
        ->setName('iccs:scm')
        ->setDescription('Loads the database cvsanaly, and simulates REST api calls into the stardom command')
        ->setDefinition(array());

    }

    protected function execute(InputInterface $input, OutputInterface $output) {

        /* @var $entityManager \Doctrine\ORM\EntityManager */
        $entityManager= $this->getEntityManager("default");

        $entityClassNames = $entityManager->getConfiguration()
                                          ->getMetadataDriverImpl()
                                          ->getAllClassNames();

        if (!$entityClassNames) {
            throw new \Exception(
                'You do not have any mapped Doctrine ORM entities according to the current configuration. '.
                'If you have entities or mapping files you should check your mapping configuration for errors.'
            );
        }

        $output->writeln(sprintf("Found <info>%d</info> mapped entities:", count($entityClassNames)));

        foreach ($entityClassNames as $entityClassName) {
            try {
                $cm = $entityManager->getClassMetadata($entityClassName);
                $output->writeln(sprintf("<info>[OK]</info>   %s", $entityClassName));
            } catch (MappingException $e) {
                $output->writeln("<error>[FAIL]</error> ".$entityClassName);
                $output->writeln(sprintf("<comment>%s</comment>", $e->getMessage()));
                $output->writeln('');
            }
        }


        $query = $entityManager->createQuery('SELECT COUNT(l.id) FROM StardomBundle:Scmlog l');
        $count = $query->getSingleScalarResult();

        $processed=0;
        $max=1000;

        while($processed < $count){

            /** @var $query \Doctrine\ORM\Query */
            $query = $entityManager->createQuery("SELECT l FROM StardomBundle:Scmlog l ORDER BY l.id");

            $output->writeln("Getting from (".$processed.") to (".($processed+$max).")");
            $query->setFirstResult($processed);
            $query->setMaxResults($max);

            $result = $query->getResult();
            $entityManager->clear();

            /** @var $scmlog \Iccs\StardomBundle\Entity\Scmlog */
            foreach($result as $scmlog){

//                $id = $scmlog->getId();
//                $rev = $scmlog->getRev();

                $output->writeln("id: ".$scmlog->getId());
                $output->writeln("rev: ".$scmlog->getRev());
                $output->writeln("author: ".$scmlog->getAuthorId());


                /** @var $person \Iccs\StardomBundle\Entity\People */
                $person = $entityManager->find('StardomBundle:People', $scmlog->getAuthorId());
                $entityManager->clear();



                $split = explode(" ",$person->getName());

                $output->writeln(print_r($split,true));

                $profile=array(
                    "id"=>"",
                    "name"=>$split[0],
                    "lastname"=>(sizeof($split) >=2 ? $split[1] :""),
                    "username"=>"",
                    "email"=>$person->getEmail()
                );







                $query = $entityManager->createQuery(
                                            "SELECT a FROM StardomBundle:Actions a ".
                                            " WHERE a.commitId=:id");

                $query->setParameter("id",$scmlog->getId());

                $actions = $query->getResult();
                $entityManager->clear();

                //get the files
                $files = array();

                /** @var $action \Iccs\StardomBundle\Entity\Actions*/
                foreach($actions as $action){

                    $fileId = $action->getFileId();

                    /** @var $file \Iccs\StardomBundle\Entity\Files */
                    $file = $entityManager->find("StardomBundle:Files",$fileId);


                    //get the functions
                    $query = $entityManager->createQuery("SELECT m FROM StardomBundle:FunctionsSrc m ".
                                                         " WHERE m.fileId=:file_id AND m.commitId=:commit_id");

                    $query->setParameter("file_id",$fileId);
                    $query->setParameter("commit_id",$action->getCommitId());


                    $functions = $query->getResult();
                    $entityManager->clear();


                    $func = array();

                    /** @var $f \Iccs\StardomBundle\Entity\FunctionsSrc */
                    foreach($functions as $f){

                         $func[]= $f->getHeader();
                    }

                    array_push($files,array(
                                           "name"=>$file->getFileName(),
                                           "functions"=>$func
                                      ));
                }

                $d = $scmlog->getDate();
                $action=array(
                    "date"=>date("Y-m-d",$d->getTimestamp()),
                    "type"=>"Svn",
                    "uid"=>$scmlog->getId(),
                    "revission"=>$scmlog->getRev(),
                    "files"=>$files
                );


                $payload = array(
                    "profile"=>$profile,
                    "action"=>$action
                );


                $output->writeln(json_encode($payload,true));


                $output->writeln($this->postPayload($payload));

            }





            $processed+=$max;

            $entityManager->clear();

        }


    }


    private function postPayload($payload){


        $values = json_encode($payload);


        $session = curl_init("http://localhost:9090/ws/constructor/action/scm");
        curl_setopt($session, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        curl_setopt ($session, CURLOPT_POST,1);
        curl_setopt ($session, CURLOPT_POSTFIELDS, $values);

        // Tell curl not to return headers, but do return the response
        curl_setopt($session, CURLOPT_HEADER, false);
        curl_setopt($session, CURLOPT_RETURNTRANSFER, true);

        $response = curl_exec($session);
        curl_close($session);

        return $response;
    }
}