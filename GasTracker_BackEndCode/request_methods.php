<?php

///////////////////////////////////////////////////////////////////////////
						/*		Users		*/
///////////////////////////////////////////////////////////////////////////

/*
	Given database connection, and username,
	gets user of username from USER table
*/
function GetUser($conn, $username){
	
	$query = "SELECT * FROM USER WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}	

	return mysqli_fetch_object($result);
}

/*
	Given database connection, username and password, function hashes password
	and inputs user into database. Returns new user
*/
function AddUser($conn, $username, $password){
	$hashed_password = GetEncryptedPassword($password);
	
	$query = "INSERT INTO `USER` (`username`, `password`) VALUES ('{$username}','{$hashed_password}');";
	if (mysqli_query($conn, $query)){
		$user = GetUser($conn, $username);
		return $user;
	}
	else{
		ReturnReponse(500, "Database Error", "Unknown database error when inserting user {$username} into database" . mysqli_error($connection));
	}
	
	return null;
}

/* Deletes all presence of given user from database */
function DeleteUser($conn, $username){	
	$query = "DELETE FROM VEHICLE WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	$query = "DELETE FROM PURCHASE WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	$query = "DELETE FROM STAT WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	$query = "DELETE FROM USER WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
}

///////////////////////////////////////////////////////////////////////////
						/*		Vehicles		*/
///////////////////////////////////////////////////////////////////////////

/* Given the vehicle id, gets vehicle */
function GetVehicle($conn, $vehicleid){
	$query = "SELECT * FROM VEHICLE WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_object($result);
}

/* Given username, gets all vehicles under that name */
function GetAllVehicles($conn, $username){
	$query = "SELECT * FROM VEHICLE WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/*
	Given vehicle name and username, adds vehicle for user in table.
	Returns new vehicle added
*/
function AddVehicle($conn, $vehicleName, $username){
	$query = "INSERT INTO `VEHICLE` (`username`, `vehiclename`) VALUES ('{$username}','{$vehicleName}');";
	if (mysqli_query($conn, $query)){
		$allVehicles = GetAllVehicles($conn, $username);
		$vehicle = $allVehicles[0];
		foreach($allVehicles as $v){
			if ($v->vehicleid > $vehicle->vehicleid) $vehicle = $v;
		}
		return $vehicle;
	}
	else{
		ReturnReponse(500, "Database Error", "Unknown database error when inserting vehicle {$vehicleName} into database" . mysqli_error($connection));
	}
	
	return null;
}

/* Deletes all presence of given vehicle from database */
function DeleteVehicle($conn, $vehicleid){	
	$query = "DELETE FROM PURCHASE WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	$query = "DELETE FROM STAT WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	$query = "DELETE FROM VEHICLE WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
}

///////////////////////////////////////////////////////////////////////////
						/*		Purchase		*/
///////////////////////////////////////////////////////////////////////////

/* Given purchase id, gets purchase under that id */
function GetPurchase($conn, $purchaseid){
	$query = "SELECT * FROM PURCHASE WHERE purchaseid = {$purchaseid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_object($result);
}

/* Given username, gets all purchases under that name */
function GetAllPurchasesByUser($conn, $username){
	$query = "SELECT * FROM PURCHASE WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Given username, gets all purchases under that name and given purchase type */
function GetAllPurchasesByUserAndType($conn, $username, $type){
	$query = "SELECT * FROM PURCHASE WHERE username = '{$username}' AND purchasetype = '{$type}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Given vehicle id, gets all purchases under that vehicle */
function GetAllPurchasesByVehicle($conn, $vehicleid){
	$query = "SELECT * FROM PURCHASE WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Given vehicle id, gets all purchases under that vehicle and given purchase type */
function GetAllPurchasesByVehicleAndType($conn, $vehicleid, $type){
	$query = "SELECT * FROM PURCHASE WHERE vehicleid = {$vehicleid} AND purchasetype = '{$type}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/*
	Functions to add to purchases given user, vehicle and purchase data
	Returns newly added purchase
*/
function AddPurchase1($conn, $username, $vehicleid, $type, $amount){
	return AddPurchaseHelper($conn, "INSERT INTO `PURCHASE`(`username`, `vehicleid`, `purchasetype`, `amountspent`, `purchasedata`, `dateofpurchase`) 
									VALUES ('{$username}', {$vehicleid}, '{$type}', {$amount}, NULL, NULL);", $vehicleid);
}
function AddPurchase2($conn, $username, $vehicleid, $type, $amount, $date){
	return AddPurchaseHelper($conn, "INSERT INTO `PURCHASE`(`username`, `vehicleid`, `purchasetype`, `amountspent`, `purchasedata`, `dateofpurchase`) 
									VALUES ('{$username}', {$vehicleid}, '{$type}', {$amount}, NULL, '{$date}');", $vehicleid);
}
function AddPurchase3($conn, $username, $vehicleid, $type, $amount, $data){
	return AddPurchaseHelper($conn, "INSERT INTO `PURCHASE`(`username`, `vehicleid`, `purchasetype`, `amountspent`, `purchasedata`, `dateofpurchase`) 
									VALUES ('{$username}', {$vehicleid}, '{$type}', {$amount}, {$data}, NULL);", $vehicleid);
}
function AddPurchase4($conn, $username, $vehicleid, $type, $amount, $data, $date){
	return AddPurchaseHelper($conn, "INSERT INTO `PURCHASE`(`username`, `vehicleid`, `purchasetype`, `amountspent`, `purchasedata`, `dateofpurchase`) 
									VALUES ('{$username}', {$vehicleid}, '{$type}', {$amount}, {$data}, '{$date}');", $vehicleid);
}

function AddPurchaseHelper($conn, $query, $vehicleid){
	if (mysqli_query($conn, $query)){
		$allPurchases = GetAllPurchasesByVehicle($conn, $vehicleid);
		$purchase = $allPurchases[0];
		foreach($allPurchases as $p){
			if ($p->purchaseid > $purchase->purchaseid) $purchase = $p;
		}
		return $purchase;
	}
	else{
		ReturnReponse(500, "Database Error", "Unknown database error when inserting purchase into database" . mysqli_error($connection));
	}
	
	return null;
}

/* Deletes given purchase*/
function DeletePurchase($conn, $purchaseid){	
	$query = "DELETE FROM PURCHASE WHERE purchaseid = {$purchaseid};";
	$result = mysqli_query($conn, $query);
}

///////////////////////////////////////////////////////////////////////////
						/*		Stat		*/
///////////////////////////////////////////////////////////////////////////

/* Given purchase id, gets purchase under that id */
function GetStat($conn, $statid){
	$query = "SELECT * FROM STAT WHERE statid = {$statid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_object($result);
}

/* Gets all stats for given user */
function GetStatsForUser($conn, $username){
	$query = "SELECT * FROM STAT WHERE username = '{$username}';";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Gets all stats for given vehicle */
function GetStatsForVehicle($conn, $vehicleid){
	$query = "SELECT * FROM STAT WHERE vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Gets all stats for given user and user's vehicle */
function GetStatsForUserAndVehicle($conn, $username, $vehicleid){
	$query = "SELECT * FROM STAT WHERE username = '{$username}' AND vehicleid = {$vehicleid};";
	$result = mysqli_query($conn, $query);
	
	if (mysqli_num_rows($result) == 0){ return null; }
	if (!$result){
		ReturnReponse(500, "Database Error", "Error with query '{$query}' while consulting database: " . mysqli_error($connection));
	}
	
	return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

/* Adds statistic to database for given user's vehicle */
function AddStat($conn, $username, $vehicleid, $stattype, $statvalue){
	$query = "INSERT INTO `STAT` (`username`, `vehicleid`, `stattype`, `statvalue`) 
						  VALUES ('{$username}', {$vehicleid}, '{$stattype}', {$statvalue});";
						  
	if (mysqli_query($conn, $query)){
		$allVehicleStats = GetStatsForUserAndVehicle($conn, $username, $vehicleid);
		$stat = $allVehicleStats[0];
		foreach($allVehicleStats as $s){
			if ($s->statid > $stat->statid) $stat = $s;
		}
		return $stat;
	}
	else{
		ReturnReponse(500, "Database Error", "Unknown database error when inserting statistic '{$stattype}' - {$statvalue} into database" . mysqli_error($connection));
	}
	
	return null;
}

/* removes stat by stat id */
function DeleteStat($conn, $statid){
	$query = "DELETE FROM STAT WHERE statid = {$statid};";
	$result = mysqli_query($conn, $query);
}


?>