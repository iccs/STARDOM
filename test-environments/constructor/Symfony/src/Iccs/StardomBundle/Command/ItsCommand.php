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
use Tidy;
use DOMDocument;
use DOMNodeList;
use DOMNode;





class ItsCommand extends Command {
    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure() {
        $this
        ->setName('iccs:its')
        ->setDescription('Loads the database cvsanaly, and simulates REST api calls into the stardom component')
        ->setDefinition(
            array(
                new InputArgument("xml_file",InputArgument::REQUIRED,"This is the xml file exported from the KDE bugzilla"),
                new InputOption("dryrun","null",InputOption::VALUE_NONE,"Whether to ommit posting to the webservice")
            ));

    }

    protected function execute(InputInterface $input, OutputInterface $output) {

        $output->writeln("Simulating ITS events");


        $xml_file = $input->getArgument("xml_file");
        $output->writeln("Correcint Malformed XML".$xml_file);

        // Specify configuration
        $config = array(
                   'input-encoding'=>'utf8',
                   'output-encoding'=>'utf8',
                   'indent'     => true,
                   'input-xml'  => true,
                   'output-xml' => true,
                   'wrap'       => false);
        // Tidy
        $tidy = new tidy;
        $tidy->parseFile($xml_file, $config);
        $tidy->cleanRepair();

        /** @var $doc DOMDocument */
        $doc = new DOMDocument(null,"utf8");
        $doc->loadXML($tidy);

        /** @var $nodeList DOMNodeList */
        $nodeList = $doc->getElementsByTagName('bug');

        $output->writeLn("About to process ".$nodeList->length);

        $names = array();
        $emails= array();
        /** @var $node DOMDocument */
        foreach($nodeList as $node){


            $reporter = $node->getElementsByTagName("reporter")->item(0);
            $reporter_email= $reporter->nodeValue;
            $reporter_name = $reporter->getAttribute("name");

            $bug_id = $node->getElementsByTagName("bug_id")->item(0)->nodeValue;
            $bug_status = $node->getElementsByTagName("bug_status")->item(0)->nodeValue;
            $bug_severity = $node->getElementsByTagName("bug_severity")->item(0)->nodeValue;

            $bug_create=$node->getElementsByTagName("creation_ts")->item(0)->nodeValue;
            $date = date("c",strtotime($bug_create));



            $assigned_to = $node->getElementsByTagName("assigned_to")->item(0);
            $assigned_to_name = $assigned_to->nodeValue;
            $assigned_to_email = $assigned_to->getAttribute("name");





            //get cc reporters
            $ccList = $node->getElementsByTagName("cc");
//            $output->writeln("Numbe of CCs ".$ccList->length);

            $ccs = array();
            foreach($ccList as $cc){

                array_push(
                    $ccs,
                    array(
                        "email"=>$cc->nodeValue
                    )
                );

            }

//            $output->writeln("CC ",print_r($ccs));


            //create a payload for the bugs
            //bug comment
            $payload= array(
                "profile"=>array(
                        "id"=>"",
                        "name"=>"",
                        "lastname"=>"",
                        "username"=>"",
                        "email"=>"",
                ),
                "action" => array(
                    "bugId"=>$bug_id,
                    "bugStatus"=>$bug_status,
                    "severity"=>$bug_severity,
                    "date"=>$date,
                    "assigned"=>array(
                        "name" => $assigned_to_name,
                        "email"=> $assigned_to_email
                    ),
                    "reporter"=>array(
                        "name" => $reporter_name,
                        "email"=> $reporter_email
                    ),
                    "cc"=>$ccs
                )
            );

            if (!$input->getOption("dryrun")) {
                $this->postPayload($payload, "add");
            }



            //get comments
            $comments = $node->getElementsByTagName("long_desc");
            foreach($comments as $comment){

                //do a payload

                $who = $comment->getElementsByTagName("who")->item(0);
                $commenter_email = $who->nodeValue;
                $commenter_name = $who->getAttribute("name");


                if(!in_array($who->getAttribute("name"),$names)){ array_push($names, $who->getAttribute("name"));}
                if(!in_array($commenter_email,$emails)){ array_push($emails, $commenter_email);}


                $bug_when = $comment->getElementsByTagName("bug_when")->item(0)->nodeValue;
                $date = date("c",strtotime($bug_when));
//                $output->writeln($bug_when." ->".date("c",$date));


                $text = $comment->getElementsByTagName("thetext")->item(0)->nodeValue;


                //bug comment
                $payload= array(
                    "profile"=>array(
                        "id"=>"",
                        "name"=>"",
                        "lastname"=>"",
                        "username"=>"",
                        "email"=>"",
                    ),
                    "action" => array(
                        "name"=>$commenter_name,
                        "email"=>$commenter_email,
                        "date"=>$date,
                        "text"=>$text
                    )
                );

                if(!$input->getOption("dryrun")){
                    $this->postPayload($payload,"comment");
                }


//              $output->writeln("Comment payload ".print_r($payload,true));

            }


        }

    }

    private function postPayload($payload,$action)
    {


        $values = json_encode($payload);

        echo "********************".PHP_EOL.PHP_EOL;
        echo $values . PHP_EOL;
        echo "********************".PHP_EOL.PHP_EOL;

        $session = curl_init("http://localhost:9090/ws/constructor/action/its/".$action);
        curl_setopt($session, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
        curl_setopt($session, CURLOPT_POST, 1);
        curl_setopt($session, CURLOPT_POSTFIELDS, $values);

        // Tell curl not to return headers, but do return the response
        curl_setopt($session, CURLOPT_HEADER, false);
        curl_setopt($session, CURLOPT_RETURNTRANSFER, true);

        $response = curl_exec($session);
        curl_close($session);

        return $response;
    }

}