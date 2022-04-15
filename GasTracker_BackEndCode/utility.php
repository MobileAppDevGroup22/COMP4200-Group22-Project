<?php

/*
	Given a status code and data in JSON format (or a regular string), 
	the function will echo the status, response description,
	and data in JSON format and exit the script
*/
function ReturnReponse($status, $description, $data){
	$response = (object)[
		'status' => $status,
		'description' => $description,
		'data' => $data
	];
	echo json_encode($response);
	exit();
}
//is_string($data) ? $data : json_encode($data)

function GetInputContentsAsObject(){
	$receivedObject = null;

	try{ 
		$receivedObject = file_get_contents('php://input'); 
		echo json_encode($receivedObject);
	}
	catch( Exception $e ) { 
		ReturnReponse(500, "Error", "Error parsing given JSON object: " . $e->getMessage());
	}

	if ($receivedObject == null || strlen($receivedObject) <= 0 || !isValidJSON($receivedObject)){
		ReturnReponse(400, "Error", "Given JSON object is empty");
	}

	$obj = json_decode($receivedObject);
}

/*
	Given a username, will return an object that states wether username is verified or not
	and if not, gives reason for rejection
*/
function VerifyUsername($usr){
	$isVerified = true;
	$feedback = "";
	
	//username length (4-31)
	if (strlen($usr) <= 3 || strlen($usr) >= 32){
		$isVerified = false;
		$feedback .= "Username should be between 4-31 characters\n";
	}
	
	//username should contain at least one letter
	if(!preg_match('/^.*[a-zA-Z]+/', $usr)) {
		$isVerified = false;
		$feedback .= "Username should contain at least one letter\n";
	}
	
	//passed all tests, return true
	if ($isVerified) { return (object)["verified" => true, "feedback" => "success"]; }
	else{ return (object)["verified" => false, "feedback" => $feedback]; }//at least one test not passed
}

/*
	Given a password, will return an object that states wether password is verified or not
	and if not, gives reason for rejection
*/
function VerifyPassword($pwd){
	$isVerified = true;
	$feedback = "";
	
	//password length (6-31)
	if (strlen($pwd) <= 5 || strlen($pwd) >= 32){
		$isVerified = false;
		$feedback .= "Password should be between 6-31 characters\n";
	}
	
	//password should contain at least one number
	
	//password should contain at least one letter
	
	//password should contain at least one capital letter
	
	
	
	//passed all tests, return true
	if ($isVerified) { return (object)["verified" => true, "feedback" => "success"]; }
	
	//at least one test not passed
	else{ return (object)["verified" => false, "feedback" => $feedback]; }
	
}

function GetEncryptedPassword($pwd){
	return password_hash($pwd, PASSWORD_DEFAULT);
}

function IsPasswordCorrect($pwd, $encryptedPwd){
	return password_verify($pwd, $encryptedPwd);
}

?>