<?php

//fonction Ajout d'un utilisateur
function Ajouter_utilisateur($con,$login,$mdp,$nom,$prenom)
{

	require_once 'PassHash.php';
	$password_hash = PassHash::hash($mdp);
	$sql = mysqli_query($con,'INSERT INTO joueur VALUES(DEFAULT,"'.$login.'","'.$password_hash.'","'.$nom.'","'.$prenom.'")')
							or die('Erreur SQL !<br>'.$sql.'<br>'.mysqli_error());  
	

	$dr=mysqli_query($con,"select id from joueur order by id DESC limit 1") 
		or die('Erreur SQL !<br>'.$sql.'<br>'.mysqli_error());
	$ligne=mysqli_fetch_assoc($dr);
	if($ligne!=0){
		return $ligne["id"];
		}
	else
		return NULL;
	
}

function isUserExists($con,$login) {
        $dr = mysqli_query($con,"SELECT * from joueur WHERE login ='".$login."'");
        $ligne=mysqli_fetch_assoc($dr);
		return $ligne;
    }
function userConnexion($con,$login,$mdp) {
        if(isUserExists($con,$login)){
        	require_once 'PassHash.php';
        	$dr = mysqli_query($con,"SELECT * from joueur WHERE login ='".$login."'");
        	$ligne=mysqli_fetch_assoc($dr);
        	if(!PassHash::check_password($ligne['passe'],$mdp))
        		$ligne = ["nom"=>""];
			return $ligne;
		}
		else{
			return NULL;
			}
        	
    }
    
function getGrilleAuPif($con) {
        $dr = mysqli_query($con,"SELECT * from grille ORDER BY RAND() LIMIT 1");
        $ligne=mysqli_fetch_assoc($dr);
		return $ligne;
    }
function getGrilles($con) {
    $dr = mysqli_query($con,"SELECT * from grille");
    $lignes=mysqli_fetch_all($dr);
	return $lignes;
}
?>
