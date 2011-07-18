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
use DOMDocument;

    
class ForumsCommand extends DoctrineCommand{


    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure() {
        $this
        ->setName('iccs:forums')
        ->setDescription('Loads the database cvsanaly, and simulates REST api calls into the stardom command')
        ->setDefinition(array(
                new InputArgument('from', InputArgument::REQUIRED, 'The entry in the database to start from'),
                new InputArgument('max', InputArgument::REQUIRED, 'The number of entries to examine'),
                new InputOption("dryrun","null",InputOption::VALUE_NONE,"Whether to ommit posting to the webservice")
            ));

    }

    protected function execute(InputInterface $input, OutputInterface $output) {



        $output->writeln("Fetching rss feeds every 60 seconds");


        $request=sprintf(
            "http://forum.kde.org/search.php?keywords=&terms=all&author=&tags=&sv=0&sc=1&sf=all&sk=t&sd=d&feed_type=RSS2.0&feed_style=BASIC&countlimit=%s&submit=Search&start=%s"
            ,$input->getArgument("max"),$input->getArgument("from"));


        $output->writeln($request);

        $doc = new DOMDocument();
        $doc->load($request);


        if($input->getOption("dryrun")){
            $output->writeln("Dryrun enabled, events will not be posted");
        }

        /** @var $nodeList DOMNodeList */
        $nodeList = $doc->getElementsByTagName('item');

        $output->writeln("Node list ".$nodeList->length);

        /** @var $node DOMDocument */
        foreach ($doc->getElementsByTagName('item') as $node) {


            $payload = array(
                "profile"=>array(
                    "name"=>"",
                    "lastname"=>"",
                    "username"=>$node->getElementsByTagName('author')->item(0)->nodeValue,
                    "email"=>""
                ),
                "action"=>array(
                    "title"=>$node->getElementsByTagName('title')->item(0)->nodeValue,
                    "category"=>$node->getElementsByTagName('category')->item(0)->nodeValue,
                    "description"=>$node->getElementsByTagName('description')->item(0)->nodeValue,
                    "date"=>$node->getElementsByTagName('pubDate')->item(0)->nodeValue
                )

            );



            if(!$input->getOption("dryrun")){
                $output->write("Posting payload".print_r($payload,true));
                $this->postPayload($payload);
            }

        }


        echo "\t".(memory_get_usage() / 1024).PHP_EOL;
        gc_collect_cycles();
    }


    private function postPayload($payload){


        $values = json_encode($payload);

        echo $values.PHP_EOL;

        $session = curl_init("http://localhost:9090/ws/constructor/action/forums");
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