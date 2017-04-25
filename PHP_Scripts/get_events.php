<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	
	if(mysqli_connect_errno()){
		echo "Failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$location_id = $_POST["location_id"];

	$query = "SELECT events.event_header, events.start_time, events.end_time, events.event_date, events.event_description, users.user_id, users.user_reputation FROM events INNER JOIN users ON events.user_id = users.id WHERE location_id='$location_id'";

	$result = mysqli_query($con, $query);

	if(mysqli_num_rows($result)){

		$rows = array();

		while($r = mysqli_fetch_assoc($result)){
			$rows[] = $r;
		}
	
		$data = array("success" => 1, "events" => $rows);
		echo json_encode($data);
	} else {
		$data = array("success" => 0);
		echo json_encode($data);
	}

	mysqli_close($con);
?>