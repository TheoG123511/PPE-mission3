<?php

include 'function.php';


// connection a la base de donnees
$db = connectionDabatase();


if (isset($_POST["username"]) && isset($_POST["password"])) {
    // permet de se connecter a l'api
    // verification que l'utilisateur existe bien
   $resultat = connexion($db, $_POST['username'], $_POST['password']);
   // on traite les action que l'utilisateur peut effectuer
   $userId = json_decode($resultat, true);
   // on verifie que l'utilisateur existe bien
   if (strpos($userId["State"], "Succes") !== false) {
       $userId = $userId["UserId"];
   }else {
       echo $resultat;
   }
   if (isset($_POST["AddFrais"])) {
       // on recupere les donnees
        $data = json_decode($_POST["AddFrais"]);
        // on ajoute l'object
        $data = insertNewFrais($db, $data, $userId);
        // on renvoie au client le retours
        echo json_encode($data);
    }
    else {
           echo $resultat;
       }
}
