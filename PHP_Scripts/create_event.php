<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	

	if (mysqli_connect_errno()) {
    	echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }

   	$location_id = $_POST['location_id'];
	$event_header = $_POST['event_header'];
	$start_time = $_POST['start_time'];
	$end_time = $_POST['end_time'];
	$event_date = $_POST['event_date'];
	$event_description = $_POST['event_description'];
	$user_id = $_POST['user_id'];

   $query = "INSERT INTO events(location_id, event_header, start_time, end_time, event_date, event_description, user_id, creation_date, creation_time) VALUES ('$location_id', '$event_header', TIME('$start_time'), TIME('$end_time'), DATE('$event_date'), '$event_description', '$user_id', CURDATE(), CURTIME())";

	if(mysqli_query($con, $query) === TRUE){
		echo "Success!";
	} else {
		echo "Error " . $query . " " . mysqli_error($con);
	}

	mysqli_close($con);
?>