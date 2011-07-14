<?php

namespace Iccs\StardomBundle\Command;

use Symfony\Component\Console\Input\InputArgument,
    Symfony\Component\Console\Input\InputOption,
    Symfony\Component\Console;
    
class ItsCommand extends Console\Command\Command {
    /**
     * Configure command, set parameters definition and help.
     */
    protected function configure() {
        $this
        ->setName('iccs:its')
        ->setDescription('Loads the database cvsanaly, and simulates REST api calls into the stardom component')
        ->setDefinition(array());

    }

    /**
     * Calculates the sum of two numbers.
     */
    protected function execute(Console\Input\InputInterface $input, Console\Output\OutputInterface $output) {



    }
}