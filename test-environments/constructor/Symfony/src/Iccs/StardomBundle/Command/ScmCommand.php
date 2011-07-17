<?php

namespace Iccs\StardomBundle\Command;

use Doctrine\ORM\Mapping\MappingException;
use Symfony\Component\Console\Input\InputOption;
use Symfony\Component\Console\Input\InputInterface;
use Symfony\Component\Console\Output\OutputInterface;
use Symfony\Component\Console\Command\Command;
use Symfony\Bundle\DoctrineBundle\Command\DoctrineCommand;
use Symfony\Component\Console\Input\InputDefinition;
use Symfony\Component\Console\Input\InputArgument;
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
        ->setDefinition(array(
                new InputArgument('from', InputArgument::REQUIRED, 'The entry in the database to start from'),
                new InputArgument('max', InputArgument::REQUIRED, 'The number of entries to examine'),
                new InputOption("dryrun","null",InputOption::VALUE_NONE,"Whether to ommit posting to the webservice")
            ));

    }

    protected function execute(InputInterface $input, OutputInterface $output) {

        /* @var $entityManager \Doctrine\ORM\EntityManager */
        $entityManager= $this->getEntityManager("default");

        /*
         * Using the following method for optimization reasons
         *
         * http://www.doctrine-project.org/blog/doctrine2-batch-processing
         *
         */
        if($input->getOption("dryrun")){
            $output->writeln("Dryrun enabled, events will not be posted");
        }

//        echo sprintf("%s\t%s\t%s", "From","To","KB").PHP_EOL;
//        echo "-----------------------------".PHP_EOL;


        /** @var $query \Doctrine\ORM\Query */
        $query = $entityManager->createQuery("SELECT l FROM StardomBundle:Scmlog l ORDER BY l.id");
        $query->setFirstResult($input->getArgument("from"));
        $query->setMaxResults($input->getArgument("max"));

        $result = $query->iterate();

        $commits = array();
        foreach($result AS $row){

            array_push($commits,
            array(
                "authorId"=>$row[0]->getAuthorId(),
                "commitId"=>$row[0]->getId(),
                "revission"=>$row[0]->getRev(),
                "commitDate"=>$row[0]->getDate()
            ));

            $entityManager->detach($row[0]);
        }


        //get the files
        //http://stackoverflow.com/questions/2461762/force-freeing-memory-in-php
        foreach($commits as &$commit){

//
//
            /** @var $person \Iccs\StardomBundle\Entity\People */
            $person = $entityManager->find('StardomBundle:People', $commit['authorId']);

            $matches=array();
            preg_match("/(\\w+)([ \\w]+)/",$person->getName(),$matches);
            $profile=array(
                "id"=>"",
                "name"=>$matches[1],
                "lastname"=>(sizeof($matches) >=3 ? trim($matches[2]) :""),
                "username"=>"",
                "email"=>$person->getEmail()
            );

            unset($person);

            $action_query = $entityManager->createQuery(
                                        "SELECT a FROM StardomBundle:Actions a  WHERE a.commitId=:id");

            $action_query->setParameter("id",$commit['commitId']);

            $actions = $action_query->iterate();
//
            //get the files
            $files = array();
            foreach($actions as $action_row){

                $fileId= $action_row[0]->getFileId();
                $entityManager->detach($action_row[0]);

                //get the functions
                $functions_query = $entityManager->createQuery("SELECT m FROM StardomBundle:FunctionsSrc m WHERE m.fileId=:file_id AND m.commitId=:commit_id");
                $functions_query->setParameter("file_id",$fileId);
                $functions_query->setParameter("commit_id",$commit['commitId']);

                $functions = $functions_query->iterate();

                $func = array();

                /** @var $f \Iccs\StardomBundle\Entity\FunctionsSrc */
                foreach($functions as $f_row){
                    $func[]= $f_row[0]->getHeader();
                    $entityManager->detach($f_row[0]);
                }

                /** @var $file \Iccs\StardomBundle\Entity\Files */
                $file = $entityManager->find("StardomBundle:Files",$fileId);
                array_push($files,array(
                                       "name"=>$file->getFileName(),
                                       "functions"=>$func
                                  ));

                unset($file);
                unset($func);
                unset($functions);
            }
//
            unset($actions);
//
            $action=array(
                "date"=>date("Y-m-d",$commit['commitDate']->getTimestamp()),
                "type"=>"Svn",
                "uid"=>$commit['commitId'],
                "revission"=>$commit['revission'],
                "files"=>$files
            );

            $payload = array(
                "profile"=>$profile,
                "action"=>$action
            );

            if(!$input->getOption("dryrun")){
                $this->postPayload($payload);
            }
            unset($profile);
            unset($action);
            unset($files);
            unset($commit);
        }


        $entityManager->clear();
        unset($commits);

        echo "\t".(memory_get_usage() / 1024).PHP_EOL;
        gc_collect_cycles();
    }


    private function postPayload($payload){


        $values = json_encode($payload);

        echo $values.PHP_EOL;

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