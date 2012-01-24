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
use Iccs\BichoDbBundle\Entity\People;
use Symfony\Bundle\FrameworkBundle\Command\ContainerAwareCommand;
use Tidy;
use DOMDocument;
use DOMNodeList;
use DOMNode;





class ItsBichoCommand extends DoctrineCommand{

    const NEW_ISSUE_EVENT="NewIssue";
    const NEW_COMMENT_EVENT="NewComment";
    const NEW_CHANGE_EVENT="History";
    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure() {
        $this
        ->setName('iccs:itsbicho')
        ->setDescription('Loads the database cvsanaly, and simulates active mq topic events calls into the stardom component')
        ->setDefinition(
            array(
                new InputOption("all","a",InputOption::VALUE_NONE,"Whether to send post all issues, changes and comments"),
                new InputOption("issues","i",InputOption::VALUE_NONE,"Whether to send issues ( Overriden by --all)"),
                new InputOption("changes","ch",InputOption::VALUE_NONE,"Whether to send changes ( Overriden by --all)"),
                new InputOption("comments","cm",InputOption::VALUE_NONE,"Whether to send comments ( Overriden by --all)"),
                new InputOption("dryrun","dd",InputOption::VALUE_NONE,"Whether to ommit posting to the webservice")
            ));

    }

    protected function execute(InputInterface $input, OutputInterface $output) {


        $enableAll        = $input->getOption("all");
        $enableIssues     = $input->getOption("issues");
        $enableChanges    = $input->getOption("changes");
        $enableComments   = $input->getOption("comments");

        $numberOfIssues=0;
        $numberOfChanges = 0;
        $numberOfComments = 0;



        $producer = new \Stomp_Stomp(
            $this->getContainer()->getParameter("stardom.stomp.url")
        );

        // connect
        $producer->connect(
            $this->getContainer()->getParameter("stardom.stomp.username"),
            $this->getContainer()->getParameter("stardom.stomp.password")
        );

        $output->writeln("Simulating ITS events");

        /* @var $entityManager \Doctrine\ORM\EntityManager */
        $entityManager= $this->getEntityManager("bicho");


        if($enableAll || $enableIssues ){

            /** @var $query \Doctrine\ORM\Query */
            $query = $entityManager->createQuery("SELECT b FROM BichoDbBundle:Issues b ORDER BY b.id");
            $result = $query->iterate();
            /** @var $bug \Iccs\BichoDbBundle\Entity\Issues */
            foreach($result as $row){


                $bug =$row[0];


                $submittedBy =$this->getPerson($entityManager,$bug->getSubmittedBy());
                $assignedTo = $this->getPerson($entityManager,$bug->getAssignedTo());



                //create a payload for the bugs
                $payload= array(
                    "profile"=>array(
                            "id"=>"",
                            "name"=>"",
                            "lastname"=>"",
                            "username"=>"",
                            "email"=>"",
                    ),
                    "action" => array(
                        "bugId"=>$bug->getIssue(),
                        "bugStatus"=>$bug->getStatus(),
                        "resolution"=>$bug->getResolution(),
                        "severity"=>$bug->getResolution(),
                        "date"=>$bug->getSubmittedOn()->format('c'),
                        "assigned"=>array(
                            "name" => $assignedTo->getName(),
                            "username" => $assignedTo->getUserId(),
                            "email"=> $assignedTo->getEmail()
                        ),
                        "reporter"=>array(
                            "id"=>$submittedBy->getId(),
                            "name"=>$submittedBy->getName(),
                            "username"=>$submittedBy->getUserId(),
                            "email"=>$submittedBy->getEmail(),
                        )
                    )
                );

                $this->postPayload($producer,$payload, self::NEW_ISSUE_EVENT,$input->getOption("dryrun"));

                $numberOfIssues++;
            }

        }



        if($enableAll || $enableChanges ){
            /** @var $changesQuery \Doctrine\ORM\Query */
            $changesQuery  = $entityManager->createQuery("SELECT p FROM BichoDbBundle:Changes p ");
            $changes = $changesQuery->iterate();

            foreach($changes as $changeRow){

                /** @var $change \Iccs\BichoDbBundle\Entity\Changes */
                $change = $changeRow[0];

                $changedBy = $this->getPerson($entityManager,$change->getChangedBy());

                //bug comment
                $payload= array(
                    "profile"=>array(
                        "sourceId"=>$changedBy->getId(),
                        "name"=>$changedBy->getName(),
                        "lastname"=>"",
                        "username"=>$changedBy->getUserId(),
                        "email"=>$changedBy->getEmail(),
                    ),
                    "action" => array(
                        "bugId"=>$change->getIssueId(),
                        "date"=>$change->getChangedOn()->format("c"),
                        "what"=>$change->getField(),
                        "added"=>$change->getNewValue(),
                        "removed"=>$change->getOldValue()
                    )
                );


                $this->postPayload($producer,$payload,self::NEW_CHANGE_EVENT,$input->getOption("dryrun"));

                $numberOfChanges++;
            }
        }



        if($enableAll || $enableComments){

            /** @var $commentQuery \Doctrine\ORM\Query */
            $commentQuery  = $entityManager->createQuery("SELECT c FROM BichoDbBundle:Comments c ");
            $comments = $commentQuery->iterate();

            foreach($comments as $commentRow){




                /**@var $comment \Iccs\BichoDbBundle\Entity\Comments */
                $comment= $commentRow[0];

                $who = $this->getPerson($entityManager,$comment->getSubmittedBy());


                //bug comment
                $payload= array(
                    "profile"=>array(
                        "sourceId"=>$who->getId(),
                        "name"=>$who->getName(),
                        "lastname"=>"",
                        "username"=>$who->getUserId(),
                        "email"=>$who->getEmail(),
                    ),
                    "action" => array(
                        "date"=>$comment->getSubmittedOn()->format('c'),
                        "text"=>$comment->getText()
                    )
                );

                $this->postPayload($producer,$payload,self::NEW_COMMENT_EVENT,$input->getOption("dryrun"));
                $numberOfComments++;

            }
        }



        $entityManager->close();
        $producer->disconnect();

        $output->writeln("Processed ".$numberOfIssues." ".self::NEW_ISSUE_EVENT);
        $output->writeln("Processed ".$numberOfChanges." ".self::NEW_CHANGE_EVENT);
        $output->writeln("Processed ".$numberOfComments." ".self::NEW_COMMENT_EVENT);


    }

    private function postPayload($producer,$payload,$action,$dryrun = false){

        $values = json_encode($payload);

        echo sprintf("*********  %s  %s ***********",$action, "(".$dryrun.")").PHP_EOL.PHP_EOL;
        echo $values . PHP_EOL;
        echo PHP_EOL."********************".PHP_EOL.PHP_EOL;

        if($dryrun){

           return;

        }


        $topic=$this->getContainer()->getParameter("stardom.stomp.its.topicPrefix").$action;
        $values = json_encode($payload);
        $producer->send("/topic/".$topic,$values);

    }


    /**
     * @param \Doctrine\ORM\EntityManager $manager
     * @param int $id
     * @return \Iccs\BichoDbBundle\Entity\People
     */
    private function getPerson(\Doctrine\ORM\EntityManager $manager, $id=0){

        /** @var $personQuery \Doctrine\ORM\Query */
        $personQuery  = $manager->createQuery("SELECT p FROM BichoDbBundle:People p WHERE p.id = :id");
        $personQuery->setParameter("id",$id);

        return $personQuery->getSingleResult();


    }

}