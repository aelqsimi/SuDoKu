<?php

// fonction de connexion
function connex()
{
	$c = mysqli_connect("localhost", "root", "root","sudoku");
	return $c;
}
?>
