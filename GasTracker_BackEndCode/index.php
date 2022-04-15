<?php

require "connection.php";
require "request_methods.php";

//if (!isset($obj->type)) ReturnReponse(400, "Error", "{$_SERVER['REQUEST_METHOD']}: No type in given JSON object");
//if (!isset($obj->data)) ReturnReponse(400, "Error", "{$_SERVER['REQUEST_METHOD']}: No data in given JSON object");

switch($_SERVER['REQUEST_METHOD']){	
	case "GET": //Get values from database
		$obj = (object)$_GET;
		//echo json_encode($_GET);
	
		switch($obj->type){
			case "user": //get user 
				if (isset($obj->username)){
					$user = GetUser($connection, $obj->username);
					
					if (!$user) ReturnReponse(404, "None", "User {$obj->username} does not exist");
					
					ReturnReponse(200, "Success", [$user]);
				}
				
				ReturnReponse(400, "Error", "GET user: No username found in given data");
				break;
			
			case "verify_password":
			case "password_verify":
				if (!isset($obj->password)) ReturnReponse(400, "Error", "GET password_verify: No password for user found in given data");
				if (!isset($obj->username)) ReturnReponse(400, "Error", "GET password_verify: No username for user found in given data");
				
				$user = GetUser($connection, $obj->username);
				
				if (!$user) ReturnReponse(500, "Database Error", "User {$obj->username} does not exist");
				
				if (!IsPasswordCorrect($obj->password, $user->password)) ReturnReponse(400, "User Error", "Incorrect password");
				
				ReturnReponse(200, "Success", [$user]);
				
				break;
				
			case "vehicle":
				if (isset($obj->username)){ //get all by username
					$vehicles = GetAllVehicles($connection, $obj->username);
					
					if (!$vehicles) ReturnReponse(404, "None", "Vehicles for user {$obj->username} do not exist");
					
					ReturnReponse(200, "Vehicle", $vehicles);
				}
				else if (isset($obj->vehicleid)){ //get specific by id
					$vehicle = GetVehicle($connection, $obj->vehicleid);
					
					if (!$vehicle) ReturnReponse(404, "None", "Vehicle {$obj->vehicleid} does not exist");
					
					ReturnReponse(200, "Vehicle", [$vehicle]);
				}
				
				ReturnReponse(400, "Error", "GET vehicle: No username or vehicle id for vehicle found in given data");
				break;
			
			case "purchase":
				if (isset($obj->username)){ //get all by username
					if (isset($obj->purchasetype)){ //with type
						$purchases = GetAllPurchasesByUserAndType($connection, $obj->username, $obj->purchasetype);
					
						if (!$purchases) ReturnReponse(404, "None", "Purchases for user {$obj->username} of type {$obj->purchasetype} do not exist");
						
						ReturnReponse(200, "Purchase", $purchases);
					}
					else{ //without type
						$purchases = GetAllPurchasesByUser($connection, $obj->username);
					
						if (!$purchases) ReturnReponse(404, "None", "Purchases for user {$obj->username} do not exist");
						
						ReturnReponse(200, "Purchase", $purchases);
					}
				}
				if (isset($obj->vehicleid)){ //get all by vehicle
					if (isset($obj->purchasetype)){ //with type
						$purchases = GetAllPurchasesByVehicleAndType($connection, $obj->vehicleid, $obj->purchasetype);
					
						if (!$purchases) ReturnReponse(404, "None", "Purchases for vehicle {$obj->vehicleid} of type {$obj->purchasetype} do not exist");
						
						ReturnReponse(200, "Purchase", $purchases);
					}
					else{ //without type
						$purchases = GetAllPurchasesByVehicle($connection, $obj->vehicleid);
					
						if (!$purchases) ReturnReponse(404, "None", "Purchases for vehicle {$obj->vehicleid} do not exist");
						
						ReturnReponse(200, "Purchase", $purchases);
					}
				}
				
				ReturnReponse(400, "Error", "GET purchase: No username or vehicle id for purchase found in given data");
				break;
				
			case "stat":
				if (isset($obj->username) && isset($obj->vehicleid)){ //get all by username and vehicle id
					$stats = GetStatsForUserAndVehicle($connection, $obj->username, $obj->vehicleid);
					
					if (!$stats) ReturnReponse(404, "None", "Stats for user {$obj->username}'s vehicle {$obj->vehicleid} do not exist");
					
					ReturnReponse(200, "Stat", $stats);
				}
				else if (isset($obj->username)){ //get all by username only
					$stats = GetStatsForUser($connection, $obj->username);
					
					if (!$stats) ReturnReponse(404, "None", "Stats for user {$obj->username} do not exist");
					
					ReturnReponse(200, "Stat", $stats);
				}
				else if (isset($obj->vehicleid)){ //get all by vehicle only
					$stats = GetStatsForVehicle($connection, $obj->vehicleid);
					
					if (!$stats) ReturnReponse(404, "None", "Stats for vehicle {$obj->vehicleid} do not exist");
					
					ReturnReponse(200, "Stat", $stats);
				}
				
				ReturnReponse(400, "Error", "GET stat: No username or vehicle id for purchases found in given data");
				break;
				
			default: ReturnReponse(400, "Invalid", "Invalid GET type");
		}
		
		break;	
	case "POST": //place values into database
		$obj = (object)$_POST;
		//echo json_encode($obj);
		
		switch($obj->type){
			case "user": //add user 
				if (!isset($obj->username)) ReturnReponse(400, "Error", "POST: No username for user found in given data");
				if (!isset($obj->password)) ReturnReponse(400, "Error", "POST: No password for user found in given data");
				
				//verify username
				$usernameVerifyResponse = VerifyUsername($obj->username);
				if (!$usernameVerifyResponse->verified) ReturnReponse(400, "Username unverified", $usernameVerifyResponse->feedback);
				
				//make sure user does not already exist in database
				if (GetUser($connection, $obj->username) != null) ReturnReponse(400, "User exists", "User already exists, please choose another username!");
				
				//verify password
				$passwordVerifyResponse = VerifyPassword($obj->password);
				if (!$passwordVerifyResponse->verified) ReturnReponse(400, "Password unverified", $passwordVerifyResponse->feedback);
				
				$user = AddUser($connection, $obj->username, $obj->password);
				
				if (!$user) ReturnReponse(500, "Error", "Unknown error accessing user");
				
				ReturnReponse(200, "Success", [$user]);
				
				break;
				
			case "vehicle":
				//need vehicle name and username to add to database
				if (!isset($obj->vehiclename)) ReturnReponse(400, "Error", "POST: No vehicle name for vehicle found in given data");
				if (!isset($obj->username)) ReturnReponse(400, "Error", "POST: No username for vehicle owner found in given data");
				
				$vehicle = AddVehicle($connection, $obj->vehiclename, $obj->username);
			
				if (!$vehicle) ReturnReponse(500, "Error", "Unknown error accessing vehicle");
				
				ReturnReponse(200, "Success", [$vehicle]);
				
				break;
			
			case "purchase":
				if (!isset($obj->username)) ReturnReponse(400, "Error", "POST: No username for purchase found in given data");
				if (!isset($obj->vehicleid)) ReturnReponse(400, "Error", "POST: No vehicleid for purchase found in given data");
				if (!isset($obj->purchasetype)) ReturnReponse(400, "Error", "POST: No purchase type for purchase found in given data");
				if (!isset($obj->amountspent)) ReturnReponse(400, "Error", "POST: No purchase amount for purchase found in given data");
				
				$purchase = null;
				
				if (isset($obj->purchasedata) && isset($obj->dateofpurchase)){
					$purchase = AddPurchase4($connection, $obj->username, $obj->vehicleid, $obj->purchasetype, $obj->amountspent, $obj->purchasedata, $obj->dateofpurchase);
				}
				else if (isset($obj->purchasedata)){
					$purchase = AddPurchase3($connection, $obj->username, $obj->vehicleid, $obj->purchasetype, $obj->amountspent, $obj->purchasedata);
				}
				else if (isset($obj->dateofpurchase)){
					$purchase = AddPurchase2($connection, $obj->username, $obj->vehicleid, $obj->purchasetype, $obj->amountspent, $obj->dateofpurchase);
				}
				else{
					$purchase = AddPurchase1($connection, $obj->username, $obj->vehicleid, $obj->purchasetype, $obj->amountspent);
				}
				
				if (!$purchase) ReturnReponse(500, "Error", "Unknown error accessing purchase");
					
				ReturnReponse(200, "Success", [$purchase]);
				
				break;
				
			case "stat":
				//$username, $vehicleid, $stattype, $statvalue
				if (!isset($obj->username)) ReturnReponse(400, "Error", "POST: No username for stat found in given data");
				if (!isset($obj->vehicleid)) ReturnReponse(400, "Error", "POST: No vehicleid for stat found in given data");
				if (!isset($obj->stattype)) ReturnReponse(400, "Error", "POST: No stat type for stat found in given data");
				if (!isset($obj->value)) ReturnReponse(400, "Error", "POST: No stat value for stat found in given data");
				
				$stat = AddStat($connection, $obj->username, $obj->vehicleid, $obj->stattype, $obj->value);
				
				if (!$stat) ReturnReponse(500, "Error", "Unknown error accessing stat");
				
				ReturnReponse(200, "Success", [$stat]);
				
				break;
				
			default: ReturnReponse(400, "Invalid", "Invalid POST type");
		}
		
		break;	
	//case "PUT": break;
	case "DELETE": //delete values from database
		$obj = (object)$_GET;
		
		$deleteReturn = (object)["responseMessage" => "Deleted user data from database"];
		
		switch($obj->type){
			case "user": //delete user (must also pass through password for security)
				if (!isset($obj->password)) ReturnReponse(400, "Error", "DELETE user: No password for user found in given data");
				if (!isset($obj->username)) ReturnReponse(400, "Error", "DELETE user: No username found in given data");

				$user = GetUser($connection, $obj->username);
				
				if (!$user) ReturnReponse(500, "Database Error", "User {$obj->username} does not exist");
				//if (!IsPasswordCorrect($obj->password, $user->password)) ReturnReponse(400, "User Error", "Incorrect password");
				if ($obj->password != $user->password) ReturnReponse(400, "User Error", "Incorrect password");
				
				DeleteUser($connection, $user->username);	
				ReturnReponse(200, "Success", [$deleteReturn]);
				
				break;
				
			case "vehicle": //delete vehicle (must also pass through password for security)
				if (!isset($obj->password)) ReturnReponse(400, "Error", "DELETE vehicle: No password for user owning vehicle found in given data");
				if (!isset($obj->vehicleid)) ReturnReponse(400, "Error", "DELETE vehicle: No vehicleid found in given data");
				
				$vehicle = GetVehicle($connection, $obj->vehicleid);
				if (!$vehicle) ReturnReponse(500, "Database Error", "Vehicle {$obj->vehicleid} does not exist");
				
				$vehicleOwner = GetUser($connection, $vehicle->username);
				if (!$vehicleOwner) ReturnReponse(500, "Database Error", "Vehicle owner {$vehicle->username} does not exist");
				//if (!IsPasswordCorrect($obj->password, $vehicleOwner->password)) ReturnReponse(400, "User Error", "Incorrect password");
				if ($obj->password != $vehicleOwner->password) ReturnReponse(400, "User Error", "Incorrect password");
				
				DeleteVehicle($connection, $vehicle->vehicleid);
				ReturnReponse(200, "Success", [$deleteReturn]);
				
				break;
			
			case "purchase":
				if (!isset($obj->password)) ReturnReponse(400, "Error", "DELETE purchase: No password for user who owns purchase found in given data");
				if (!isset($obj->purchaseid)) ReturnReponse(400, "Error", "DELETE purchase: No purchaseid found in given data");
				
				$purchase = GetPurchase($connection, $obj->purchaseid);
				if (!$purchase) ReturnReponse(500, "Database Error", "Purchase {$obj->purchaseid} does not exist");
				
				$purchaseOwner = GetUser($connection, $purchase->username);
				if (!$purchaseOwner) ReturnReponse(500, "Database Error", "Purchase owner {$purchase->username} does not exist");
				//if (!IsPasswordCorrect($obj->password, $purchaseOwner->password)) ReturnReponse(400, "User Error", "Incorrect password");
				if ($obj->password != $purchaseOwner->password) ReturnReponse(400, "User Error", "Incorrect password");
				
				DeletePurchase($connection, $purchase->purchaseid);
				ReturnReponse(200, "Success", [$deleteReturn]);
				
				break;
				
			case "stat":
				if (!isset($obj->password)) ReturnReponse(400, "Error", "DELETE stat: No password for user who owns stat found in given data");
				if (!isset($obj->statid)) ReturnReponse(400, "Error", "DELETE stat: No statid found in given data");
				
				$stat = GetStat($connection, $obj->statid);
				if (!$stat) ReturnReponse(500, "Database Error", "Stat {$obj->statid} does not exist");
				
				$statOwner = GetUser($connection, $stat->username);
				if (!$statOwner) ReturnReponse(500, "Database Error", "Stat owner {$stat->username} does not exist");
				//if (!IsPasswordCorrect($obj->password, $statOwner->password)) ReturnReponse(400, "User Error", "Incorrect password");
				if ($obj->password != $statOwner->password) ReturnReponse(400, "User Error", "Incorrect password");
				
				DeleteStat($connection, $stat->statid);
				ReturnReponse(200, "Success", [$deleteReturn]);
				
				break;
				
			default: ReturnReponse(400, "Invalid", "Invalid DELETE type");
		}
		
		break;
}

?>