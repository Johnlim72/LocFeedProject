<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");
	
	if(mysqli_connect_errno()){
		echo "Failed to connect to MySQL: " . mysqli_connect_errno();
	}

	//$location_id = $_POST["location_id"];
	$location_id = 4;


	//$query = "SELECT * FROM questions WHERE location_id='$location_id'";

	$query = "SELECT comments.comment_details, users.user_id, users.user_reputation FROM comments INNER JOIN users ON comments.user_id = users.id WHERE location_id='$location_id'";

	$result = mysqli_query($con, $query);

	if(mysqli_num_rows($result)){

		$rows = array();

		while($r = mysqli_fetch_assoc($result)){
			$rows[] = $r;
		}
	
		$data = array("questions" => $rows);
		echo json_encode($data);
	}

	mysqli_close($con);
?>