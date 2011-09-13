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
use Iccs\StardomBundle\Parser\MimeMailParser;
use RecursiveDirectoryIterator;



class MailingListCommand extends DoctrineCommand
{
    public $payloadAmount = 0;


    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure()
    {
        $this
                ->setName('iccs:mailing')
                ->setDescription('Crawls the Mailing list dumpt')
                ->setDefinition(array(
                                    new InputArgument("dump_directory", InputArgument::REQUIRED, "This is KDE dump provided by Dario"),
                                    new InputOption("dryrun","null",InputOption::VALUE_NONE,"Whether to ommit posting to the webservice")
                                ));

    }

    protected function execute(InputInterface $input, OutputInterface $output)
    {


        $output->writeln("Getting files from the dump directory");


        $directory_iterator = new RecursiveDirectoryIterator($input->getArgument("dump_directory"));

        // instantiate
        $Parser = new MimeMailParser();

        $addresses = array();
        foreach ($directory_iterator as $path) {
            if (!$path->isDir()) {
//                $output->writeln("Processing " . $path);
                $contents = file_get_contents($path);


                //extract information



                // read the email from stdin
                $Parser->setText($contents);


                // get the email parts
                $date = $Parser->getHeader('date');
                $from = $Parser->getHeader('from');
                $subject = $Parser->getHeader('subject');
                $text = $Parser->getMessageBody('text');

                //handle digest
                if(preg_match("/Kde-hardware-devel Digest, Vol (\\d)+, Issue (\\d)+/",$subject)){
                    //we have a digest
//                    $output->writeln("Processing Digest ");

                    $messages = array();

                    $read = false;
                    $content="";

                    foreach(preg_split("/(\r?\n)/", $text) as $line){
//                        $output->writeln("Line : ".$line);

                        if(preg_match('/Message: \\d+/',$line)){
                            //message start
                            $read = true;
                        }

                        if($read){
                            $content.=$line."\r\n";
                        }

                        if($line == '------------------------------' && $read){
                            //message end
                            $read=false;

                            $Parser->setText($content);

                            // get the email parts
                            $mail_message=array(
                                "action"=>array(
                                    "from" => $Parser->getHeader('from'),
                                    "subject" => $Parser->getHeader('subject'),
                                    "date" => $Parser->getHeader('date'),
                                    "text" => $Parser->getMessageBody('text')
                                )
                            );


                            array_push($messages,$mail_message);
                        }
                    }


                    foreach($messages as $message){

                        //post payload
                        if(!in_array($message['action']['from'],$addresses)){
                            array_push($addresses,$message['action']['from']);
                        }

                        $this->postPayload($message,$input->getOption("dryrun"));


                    }

                }else{

                    $payload=   array(
                        "action"=>array(
                             "from"=>$from,
                             "subject"=>$subject,
                             "date"=>$date,
                             "text"=>$text
                        )
                    );

                    if(!in_array($payload['action']['from'],$addresses)){
                        array_push($addresses,$payload['action']['from']);
                    }


                    $this->postPayload($payload,$input->getOption("dryrun"));


                }
            }
        }

        foreach($addresses as $adr){
            $output->writeLn($adr);
        }


        echo "Number of messages processed ".$this->payloadAmount;

    }


    private function postPayload($payload,$dryrun)
    {

        $this->payloadAmount++;

        if($dryrun){
            return;
        }



        $values = json_encode($payload);
        echo $values;

        echo $values . PHP_EOL;

        $session = curl_init($this->getContainer()->getParameter("stardom.mail_action"));
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