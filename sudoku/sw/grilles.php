<?php

include '../connexion.php';
include '../inscription_fonctions.php';

if(strcasecmp($_SERVER['REQUEST_METHOD'], 'POST') != 0){
    throw new Exception('Request method must be POST!');
}
 
$contentType = isset($_SERVER["CONTENT_TYPE"]) ? trim($_SERVER["CONTENT_TYPE"]) : '';
if(strcasecmp($contentType, 'application/json') != 0){
    throw new Exception('Passe moi du JSON ou je gueule encore');
}
 

$content = trim(file_get_contents("php://input"));

$con = connex();
	 
if(mysqli_connect_errno())
{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

if(($ligne=getGrilles($con))!=NULL){
	echo '[';
	for($i = 0; $i < count($ligne); ++$i) {
		echo '{"grille":"'.$ligne[$i][1].'"}';
		if($i != count($ligne) -1)
			echo ',';
	}
	echo ']';
}
else {
	echo '{"resp":"0"}';
}
mysqli_close ();
?>
