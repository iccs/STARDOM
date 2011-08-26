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
use Symfony\Bundle\FrameworkBundle\Command\ContainerAwareCommand;
use Tidy;
use DOMDocument;
use DOMNodeList;
use DOMNode;





class ItsCommand extends ContainerAwareCommand{
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


            //get the activity of the bug
            $python_script = __DIR__."/t.py";

            $activity_xml="";
            $command = sprintf('/usr/bin/python %s %s',$python_script,$bug_id);
            $temp = exec($command, $activity_xml);


            $output->writeln("\n\n***************************");
            $output->writeln("Activity for bug id ".$bug_id);

            /**
             * Adding root node because of the Entity error
             * http://www.phphaven.com/article.php?id=14
             */
            $activity_xml= sprintf("<activity>%s</activity>",trim(implode($activity_xml)));

//            $output->writeln($activity_xml,true);

            $tidy->parseString($activity_xml);
            $tidy->cleanRepair();

            /** @var $doc DOMDocument */
            $activityDoc = new DOMDocument(null,"utf8");
            $activityDoc->loadXML($tidy);


            /**
               <activity>
                <change>
                  <who>asraniel@fryx.ch</who>
                  <when>2009-12-17 11:25:11</when>
                  <changeitem>
                    <what>CC</what>
                    <removed></removed>
                    <added>asraniel@fryx.ch</added>
                  </changeitem>
                  <changeitem>
                    <what>Component</what>
                    <removed>widget-kickoff</removed>
                    <added>general</added>
                  </changeitem>
                  <changeitem>
                    <what>Product</what>
                    <removed>plasma</removed>
                    <added>solid</added>
                  </changeitem>
                </change>
                </activity>
             */

            $activities = $doc->getElementsByTagName('activity');


            foreach($activities as $activity){

                $changes= $activity->getElementsByTagName("change");

                foreach($changes as $change){

                    $what=$change->getElementsByTagName("what")->item(0)->nodeValue;
                    $activityDate = $change->getElementsByTagName("when")->item(0)->nodeValue;

                    $output->writeln($what);

                }

            }

            $output->writeln("***************************\n\n");




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

        $session = curl_init($this->getContainer()->getParameter("stardom.its_action")."/".$action);
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