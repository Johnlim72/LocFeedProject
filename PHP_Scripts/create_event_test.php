<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	

	if (mysqli_connect_errno()) {
    	echo "Failed to connect to MySQL: " . mysqli_connect_error();
   	}

      $location_id = 1;
   	$event_header = "Header 1";
   	$start_time = "08:00:00";
   	$end_time = "09:00:00";
   	$event_date = "2017-04-06";
   	$event_description = "Description 1";
      $user_id = 2;

   	$query = "INSERT INTO events(location_id, event_header, start_time, end_time, event_date, event_description, user_id, creation_date, creation_time) VALUES ('$location_id', '$event_header', TIME('$start_time'), TIME('$end_time'), DATE('$event_date'), '$event_description', '$user_id', CURDATE(), CURTIME())";

   	if(mysqli_query($con, $query) === TRUE){
   		echo "Success!";
   	} else {
   		echo "Error " . $query . " " . mysqli_error($con);
   	}

   	mysqli_close($con);
?>