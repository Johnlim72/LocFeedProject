<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	

	if (mysqli_connect_errno()) {
    	echo "Failed to connect to MySQL: " . mysqli_connect_error();
   	}

      //$comment_details = $_POST["comment_details"];
      //$location_id = $_POST["location_id"];
      //$user_id = $_POST["user_id"];

      $comment_details = "Comment 1";
      $location_id = 1;
      $user_id = 2;

   	$query = "INSERT INTO comments(location_id, comment_details, user_id, creation_date, creation_time) VALUES ('$location_id', '$comment_details', '$user_id', CURDATE(), CURTIME())";

   	if(mysqli_query($con, $query) === TRUE){
   		echo "Success!";
   	} else {
   		echo "Error " . $query . " " . mysqli_error($con);
   	}

   	mysqli_close($con);
?>