<?php 

declare(ticks = 1);

pcntl_signal(SIGTERM, "signal_handler");
pcntl_signal(SIGINT, "signal_handler");

$host=$argv[1];


echo "Getting contents from base host ".$host."\n";

function get_json($json_url){
	// Initializing curl
	$ch = curl_init( $json_url );

	// Configuring curl options
	$options = array(
	CURLOPT_RETURNTRANSFER => true,
	CURLOPT_HTTPHEADER => array('Accept: application/json') 
	);

	// Setting curl options
	curl_setopt_array( $ch, $options );

	// Getting results
	$result =  curl_exec($ch); // Getting jSON result string

	curl_close($ch);
	return json_decode($result,true);
}

function pretty_counts($count_array){
	$str="";
	
	//get the longest string
	$pad= 10;
	foreach($count_array as $topic=>$count){
		$pad = max($pad,strlen($topic));
	}
	
	$pad +=2;
	foreach($count_array as $topic=>$count){
		$str.=sprintf(" %s:\t%s\n",
			ucfirst(str_pad($topic,$pad)),$count);
	}
	return $str;
}

function signal_handler($signal) {
     switch($signal) {
        case SIGTERM:
            print "Caught SIGTERM\n";
            exit;
        case SIGKILL:
            print "Caught SIGKILL\n";
            exit;
        case SIGINT:
            print "Caught SIGINT\n";
            exit;
    }
}

function replaceOut($str)
{
    $numNewLines = substr_count($str, "\n");
    echo chr(27) . "[0G"; // Set cursor to first column
    echo $str;
    echo chr(27) . "[" . $numNewLines ."A"; // Set cursor up x lines
}

while(1) {

	$stardom = get_json($host."/stardom-connector/ws/status");

replaceOut(sprintf(<<<EOF
************************* STARDOM ***************************

%s

*************************************************************

EOF
, 
	pretty_counts($stardom)));

	sleep(1);

}
