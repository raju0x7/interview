<?php
    $file = $_GET[‘file’];
    if(isset($file)) {
        include(“pages/$file”);
    }
    else {
        include(“index.php”);
    }
?>