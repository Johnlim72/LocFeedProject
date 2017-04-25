<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	
	if(mysqli_connect_errno()){
		echo "Failed to connect to MySQL: " . mysqli_connect_errno();
	}

	$query = "SELECT * FROM locations";

	$result = mysqli_query($con, $query);

	if(mysqli_num_rows($result)){

		$rows = array();

		while($r = mysqli_fetch_assoc($result)){
			$rows[] = $r;
		}
	
		$data = array("locations" => $rows);
		echo json_encode($data);
	}

	mysqli_close($con);
?>