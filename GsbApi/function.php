<?php

/** Permet de verifier si un utilisateur existe bien dans la base de donnees (utilisateur de type visiteur)
 * @param object $conn : l'objet permettant d'executer la requete vers la base de donnees
 * @param String $username : l'identifiant de l'utilisateur
 * @param String $password : le mot de passe de l'utilisateur
 */
function connexion($conn, $username, $password)
{
    $arr = array();
    $requetePrepare = $conn->prepare(
        'SELECT visiteur.id AS id, visiteur.nom AS nom, '
        . 'visiteur.prenom AS prenom '
        . 'FROM visiteur '
        . 'WHERE visiteur.login = :unLogin AND visiteur.mdp = :unMdp'
        );
    $requetePrepare->bindParam(':unLogin', $username, PDO::PARAM_STR);
    $requetePrepare->bindParam(':unMdp', $password, PDO::PARAM_STR);
    $requetePrepare->execute();
    $nb_result = $requetePrepare->rowCount();
    if ($nb_result > 0) {
        $req = $requetePrepare->fetchAll();
        $userID = $req[0]['id'];
        // $result = "Authentification réussie%" . (string)$userID . "%";
        $result = array('Function'=>'connexion', 'State' => "Succes", 'UserId' => (string)$userID);
    } else {
        $result = array('Function'=>'connexion', 'State' => "Error", 'Error' => "Erreur d'authentification");
       // $result = 'Erreur d\'authentification';
    }
    return json_encode($result);
}


function checkIfFraisLigneExist($conn, $userId, $date, $idFraisForfait) {
    $requetePrepare = $conn->prepare(
        'SELECT lignefraisforfait.idvisiteur, lignefraisforfait.mois '
        . 'FROM lignefraisforfait '
        . 'WHERE lignefraisforfait.idvisiteur = :unIdVisiteur AND lignefraisforfait.mois = :unMois AND lignefraisforfait.idfraisforfait = :unIdFrais'
        );
    $requetePrepare->bindParam(':unIdVisiteur', $userId, PDO::PARAM_STR);
    $requetePrepare->bindParam(':unMois', $date, PDO::PARAM_STR);
    $requetePrepare->bindParam(':unIdFrais', $idFraisForfait, PDO::PARAM_STR);
    $requetePrepare->execute();
    $nb_result = $requetePrepare->rowCount();
    if ($nb_result > 0) { 
        return true;
    }
    return false;
}


/**
 * Fonction qui permet de se connecter a la base de donnees
 * @return PDO : Object qui permet d'acceder a la base de donnees
 */
function connectionDabatase(){
    $serveur = 'mysql:host=localhost';
    $bdd = "dbname=gsb_frais";
    $user = "userGsb";
    $mdp = "secret";
    //$bdd = new PDO('mysql:host=localhost;dbname=test;charset=utf8', 'root', '');
    $conn = new PDO("$serveur;$bdd;charset=utf8", $user, $mdp);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION); 
    return $conn;
}



/** Permet d'ajouter une nouvelle ligne dans la base de donnees pour un frais de type hors forfait
 * @param object $conn : l'objet permettant d'executer la requete vers la base de donnees
 * @param String $userId : l'identifiant de l'utilisateur concerner
 * @param String $mois : le mois a ajouter
 * @param String $libelle : le motif
 * @param Integer $montant : le montant
 * @param Integer $index : l'index du frais hors forfait dans le tableaux
 */
function ajoutFraisHorsForfait($conn, $userId, $mois, $libelle, $montant, $date) {
    try {
        $req = $conn->prepare("INSERT INTO lignefraishorsforfait(idvisiteur,mois,libelle,date,montant) VALUES (:idvisiteur,:mois,:libelle,:date,:montant)");
        $req->bindParam(':idvisiteur', $userId, PDO::PARAM_STR, 255);
        $req->bindParam(':mois', $mois, PDO::PARAM_STR, 255);
        $req->bindParam(':libelle', $libelle, PDO::PARAM_STR, 255);
        $req->bindParam(':date', $date, PDO::PARAM_STR, 255);
        $req->bindParam(':montant', $montant, PDO::PARAM_INT);
        $req->execute();
        return array("State"=>1, "date"=>$date);
    } catch (PDOException $e) {
        return array("State"=>0, "Error"=>$e->getMessage(), "date"=>$date);
    }
}



/** Permet d'inserer un frais de type forfaitaire dans la base de donnes 
 * 
 * @param object $conn l'object qui permet l'acces a la base de donnees
 * @param String $userId l'id de l'utilisateur
 * @param String $date la date a ajouter
 * @param String $idFraisForfait le type de frais
 * @param String $quantite la quantité voulu
 * @return array : Un tableau qui contient les valeur qui on ete ajouter ou l'erreur qui ses produite
 */

function addFrais($conn, $userId, $date, $idFraisForfait, $quantite) {
    try {
        // insertion des donnees
        // si le frais n'existe pas on le creer
        if (!checkIfFraisLigneExist($conn, $userId, $date, $idFraisForfait)) {
            $req = $conn->prepare("INSERT INTO lignefraisforfait(idvisiteur,mois,idfraisforfait,quantite) VALUES (:idvisiteur,:mois,:idfraisforfait,:quantite)");
            $req->bindParam(':idvisiteur', $userId, PDO::PARAM_STR, 255);
            $req->bindParam(':mois', $date, PDO::PARAM_STR, 255);
            $req->bindParam(':idfraisforfait', $idFraisForfait, PDO::PARAM_STR, 255);
            $req->bindParam(':quantite', $quantite, PDO::PARAM_INT);
        } else {
            $req = $conn->prepare(
                'UPDATE lignefraisforfait '
                . 'SET lignefraisforfait.quantite = :uneQte '
                . 'WHERE lignefraisforfait.idvisiteur = :unIdVisiteur '
                . 'AND lignefraisforfait.mois = :unMois '
                . 'AND lignefraisforfait.idfraisforfait = :idFrais'
            );
            $req->bindParam(':uneQte', $quantite, PDO::PARAM_INT);
            $req->bindParam(':unIdVisiteur', $userId, PDO::PARAM_STR, 255);
            $req->bindParam(':unMois', $date, PDO::PARAM_STR, 255);
            $req->bindParam(':idFrais', $idFraisForfait, PDO::PARAM_STR, 255);
            
        }
        $req->execute();
        return array("State"=>1);
    } catch (PDOException $e) {
        return array("State"=>0, "Error"=>$e->getMessage());
    }
}


/** Permet de genere une date au bon format
* 
* @param String $year l'annee sous forme de chaine (ex: 2020)
* @param String $month le mois sous forme de chaine (ex: 03)
* @param String $days la jours sous forme de chaine (ex: 11)
* @return String : La date au bon format (annee-mois-jours)
*/
function generateDate($year, $month, $days) {
    if (strlen($month) == 1) {
        $month = "0". $month;
    }
    $date = $year. "-" .$month. "-" .$days;
    return $date;
}

/** Permet de creer une nouvelle fiche de frais si elle n'existe pas
* @param object $conn l'object qui permet l'acces a la base de donnees
* @param String $userId l'id de l'utilisateur
* @param String $date la date a ajouter
*/
function creerNouvelleFicheFrais($conn, $userId, $date) {
    try {
     $requetePrepare = $conn->prepare(
            'INSERT INTO fichefrais (idvisiteur,mois,nbjustificatifs,'
            . 'montantvalide,datemodif,idetat) '
            . "VALUES (:unIdVisiteur,:unMois,0,0,now(),'CR')"
        );
        $requetePrepare->bindParam(':unIdVisiteur', $userId, PDO::PARAM_STR);
        $requetePrepare->bindParam(':unMois', $date, PDO::PARAM_STR);
        $requetePrepare->execute();
    } catch (PDOException $e) { 
        return false;
    }
    return true;
}

/** Permet de verifier si une fiche de frais existe pour un utilisateur a une date donnees
* @param object $conn l'object qui permet l'acces a la base de donnees
* @param String $userId l'id de l'utilisateur
* @param String $date la date a ajouter
*/
function checkIfFraisExist($conn, $userId, $date)
{
    $requetePrepare = $conn->prepare(
        'SELECT fichefrais.idvisiteur, fichefrais.mois '
        . 'FROM fichefrais '
        . 'WHERE fichefrais.idvisiteur = :unIdVisiteur AND fichefrais.mois = :unMois'
        );
    $requetePrepare->bindParam(':unIdVisiteur', $userId, PDO::PARAM_STR);
    $requetePrepare->bindParam(':unMois', $date, PDO::PARAM_STR);
    $requetePrepare->execute();
    $nb_result = $requetePrepare->rowCount();
    if ($nb_result > 0) { 
        return true;
    }
    return false;
}

/** Permet d'inserer les frais hors forfait et forfaitaire d'un object recu par le serveur (permet d'inserer dans la base de donnees les donnees)
* @param object $conn l'object qui permet l'acces a la base de donnee
* @param array $data les donnees a ajouter a la base de donnees sous forme de tableaux
* @param String $userId l'id de l'utilisateur
*/
function insertNewFrais($conn, $data, $userId) {
    $mois = $data->Month;
    $annee = $data->Year;
    $date = $data->Date;
    $km = $data->KM;
    $repas = $data->REP;
    $etape = $data->ETP;
    $nuit = $data->NUI;
    $fraisF = array("KM"=>$km, "REP"=>$repas, "ETP"=>$etape, "NUI"=>$nuit);
    $ficheFraisBool = false;
    // on insert les donnees concernant les frais forfaitaire
    foreach ($fraisF as $key => $value) {
        if (!checkIfFraisExist($conn, $userId, $date)) {
            $ficheFraisBool = creerNouvelleFicheFrais($conn, $userId, $date);
        }
        $fraisF[$key] = addFrais($conn, $userId, $date, $key, $value);   
    }
    // permet au client d'identifier le retour serveur et de retrouver l'object en local grace a KeyObject et a Function
    $fraisF["Function"] = "insertNewFrais";
    $fraisF["KeyObject"] = $date;
    $fraisF["Month"] = $mois;
    $fraisF["Year"] = $annee;
    // on detruit $value
    unset($value);
    $fraisF['fraisHorsForfait'] = array();
    // on verifie que tous c'est bien passer
    $hfRestant = true;
    $index = 0;
    do {
        $HFindex = "Hf-" . $index;
        if (!empty($data->$HFindex)) {
            // recuperation des donnees
            $hf = $data->$HFindex;
            $hfJour = $hf->Jour;
            $hfMotif = $hf->Motif;
            $hfMontant = $hf->Montant;
            $hfDate = generateDate($annee, $mois, $hfJour);
            // on ajoute les donnees dans la base de donnees
            $fraisF['fraisHorsForfait'][$HFindex] = ajoutFraisHorsForfait($conn, $userId, $date, $hfMotif, $hfMontant, $hfDate);
            $index++;
        } else {
            // une erreur on stop la
            $hfRestant = false;
        }
        } while ($hfRestant);
        return $fraisF;    
}
