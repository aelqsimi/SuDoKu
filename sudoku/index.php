<?php
header('Content-Type: text/html; charset=UTF-8');
session_start(); 
include 'connexion.php';
include 'inscription_fonctions.php';
echo "<script language='JavaScript' src='fonctions.js' type='text/javascript'></script>"; 
 if (isset($_POST['submit'])){
	//On récupère les valeurs entrées par l'utilisateur :
	$prenom=$_POST['firstname'];
	$nom=$_POST['lastname'];
	$login=$_POST['login'];
	$mdp=$_POST['password'];
	 //On se connecte
	 $con = connex();
	 
	 if(mysqli_connect_errno())
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
		if(isUserExists($con,$login)==NULL){
			$id=Ajouter_utilisateur($con,$login,$mdp,$nom,$prenom);
		}
		else {
		echo '<script language="javascript">';
		echo 'alert("Vous avez déjà un compte, si vous avez oublié votre mot de passe veuillez contacter l\'admin")';
		echo '</script>';
		}
	mysqli_close ();
}//fin if submit
?>
<!DOCTYPE html>
<html>
<head>   

<title>Inscription</title>
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE9">
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"/>
    <link rel="stylesheet" type="text/css" href="css/style-inscrption.css" media="all" />
    <link rel="stylesheet" type="text/css" href="css/style-inscrption2.css" media="all" />
</head>
<body>
<div class="container">

			
      <div  class="form">
	              <div id="logo" >
	              
				<img src="../images/logo.png" alt=""   width="200"/>
				
			</div>
			
    		<form id="contactform" method="post" accept-charset="utf-8" >
				<h1 >Inscription :</h1><br><br>
	
    			<p class="contact"><label for="lastname">Nom </label></p> 
    			<input id="lastname" name="lastname" placeholder="Votre nom ici..." required="" tabindex="1" type="text" 
			pattern="^([ \u00c0-\u01ffa-zA-Z'\-])+$"> 
    			 
    			<p class="contact"><label for="firstname">Prénom</label></p> 
    			<input id="firstname" name="firstname" placeholder="Votre prénom..." required="" tabindex="2" type="text"
			pattern="^([ \u00c0-\u01ffa-zA-Z'\-])+$"> 
    			 
    			<p class="contact"><label for="login">Login</label></p> 
    			<input id="login" name="login" required="" type="text" placeholder="Votre login ici..."> 
                
               		 <p class="contact"><label for="password">Mot de passe</label></p> 
    			<input type="password" id="password" name="password" placeholder="Mot de passe" required=""
			pattern="([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])*"> 
               		
			 <p class="contact"><label for="repassword">Confirmer le mot de passe</label></p> 
    			<input type="password" id="repassword" name="" placeholder="Retaper votre mot de passe" required=""
			pattern="([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])([A-z]|[0-9])*"
			oninvalid="InvalidMsg(this);" oninput="InvalidMsg(this);"> 
			 
            <input type="submit" name="submit" class="buttom"  tabindex="5" value=" S'inscrire !" > 	 
   </form> 

</div>      
</div>
</body>
</html>
