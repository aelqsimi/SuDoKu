<?php
    session_start();
    $_SESSION = array();
    session_destroy();
    $page = "../index.php";
    header("Location: $page");
?>
