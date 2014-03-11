<?php
  header("Access-Control-Allow-Origin: *");  // allow loading from other domains

  include 'config.php';
	$route=$_GET["routeid"];
	$run=$_GET["runid"];
  /* Insert SQL to extract appropriate fields from Runs table where
     the route_id matches an id parameter value */
	if ($route && $run){
  $sql = "select * from Stops where routeid=$route and runid='$run'";
	}else{
	$sql = "select * from Stops";
	}
  try {
	/* Initialize a data-access abstraction-layer object
	   (a PHP Data Object: PDO) for a mysql database with
	   values taken from the config.sql file */
	$dbh = new PDO("mysql:host=$dbhost;dbname=$dbname", $dbuser, $dbpass);	
	// throw an exception if something goes wrong (see catch block below)
	$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	// create a SQL statement
	$stmt = $dbh->query($sql);  
	/* execute the statement against the database and assign the
	   resulting array to variable runs */
	$routes = $stmt->fetchAll(PDO::FETCH_OBJ);
	// close database connection by destroying the object that ref's it
	$dbh = null;
	/* return results to client, encoded as JSON and wrapped in a
	   dictionary with key "items" */
	echo '{"items":'. json_encode($routes) .'}'; 
  } catch(PDOException $e) {
	/* return exceptions as a dictionary with key "error".  If an 
	   exception is thrown when creating a DB connection, and not
	   caught, a full "backtrace" log may be shown -- not good */
	echo '{"error":{"text":'. $e->getMessage() .'}}'; 
  }

?>
