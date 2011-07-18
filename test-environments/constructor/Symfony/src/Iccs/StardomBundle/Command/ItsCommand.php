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
                new InputArgument("xml_file",InputArgument::REQUIRED,"This is the xml file exported from the KDE bugzilla")
            ));

    }

    protected function execute(InputInterface $input, OutputInterface $output) {

        $output->writeln("Simulating ITS events");


        $xml_file = $input->getArgument("xml_file");
        $output->writeln("Reading ".$xml_file);

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

        /** @var $node DOMDocument */
        foreach($nodeList as $node){

            //get reporter

            $reporter = $node->getElementsByTagName("reporter")->item(0);
            $reporter_email= $reporter->nodeValue;
            $reporter_name = $reporter->getAttribute("name");

            $bug_status = $node->getElementsByTagName("bug_status")->item(0);
            $bug_severity = $node->getElementsByTagName("bug_severity")->item(0);

            $assigned_to = $node->getElementsByTagName("assigned_to")->item(0)->nodeValue;


            $output->writeln("Reporter ".$reporter_email);
            $output->writeln("Reporter ".$reporter_name);


            //get cc reporters
            $ccList = $node->getElementsByTagName("cc");
            $output->writeln("Numbe of CCs ".$ccList->length);

            $ccs = array();
            foreach($ccList as $cc){

                array_push(
                    $ccs,
                    $cc->nodeValue
                );

            }

            $output->writeln("CC ",print_r($ccs));


            //get comments
            $comments = $node->getElementsByTagName("long_desc");
            foreach($comments as $comment){

                //do a payload

                $who = $comment->getElementsByTagName("who")->item(0);
                $commenter_email = $this->fixemail($who->nodeValue);

                $name = explode(" ",$who->getAttribute("name"));
                $commenter_name=array("","");
                $commenter_username= "";

                if(sizeof($name) > 1){
                    $commenter_name = $name;
                }else if(sizeof($name) > 0 ){
                    $commenter_username = $name[0];
                }





                $date = $comment->getElementsByTagName("bug_when")->item(0)->nodeValue;
                $text = $comment->getElementsByTagName("thetext")->item(0)->nodeValue;


                //bug comment
                $payload= array(
                    "profile"=>array(
                        "name"=>$commenter_name[0],
                        "lastname"=>$commenter_name[1],
                        "username"=>$commenter_username,
                        "email"=>$commenter_email
                    )
                );

                $output->writeln("Comment payload ".print_r($payload,true));


            }


        }



    }


    private function fixemail($mail){

        $parts = explode(" ",$mail);


        return $parts[0]."@".$parts[1].".".$parts[2];

    }

}