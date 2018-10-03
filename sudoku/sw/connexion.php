<?php
include '../connexion.php';
include '../inscription_fonctions.php';
if(strcasecmp($_SERVER['REQUEST_METHOD'], 'POST') != 0){
    throw new Exception('Request method must be POST!');
}
 
$contentType = isset($_SERVER["CONTENT_TYPE"]) ? trim($_SERVER["CONTENT_TYPE"]) : '';
if(strcasecmp($contentType, 'application/json') != 0){
    throw new Exception('Content type must be: application/json');
}
 
$content = trim(file_get_contents("php://input"));
 
$decoded = json_decode($content, true);
 
if(!is_array($decoded)){
    throw new Exception('Received content contained invalid JSON!');
}

$login = $decoded['login'];
$mdp = $decoded['passe'];

$con = connex();
	 
if(mysqli_connect_errno())
{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
if(($ligne=userConnexion($con,$login,$mdp))!=NULL){
	if($ligne['nom']=="")
		echo '{"Erreur": "Mot de passe erroné"}';
	else
		echo '{"nom":"'.$ligne['nom'].'","prenom":"'.$ligne['prenom'].'" }';
}
else {
	echo '{"resp":"0"}';
}
mysqli_close ();
?>
