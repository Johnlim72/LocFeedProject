<?php
	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");

	if (mysqli_connect_errno()) {
    	echo "Failed to connect to MySQL: " . mysqli_connect_error();
   	}

   	$user_id = $_POST['user_id'];
   	$u_password = $_POST['u_password'];
	
	$query = "SELECT * FROM users WHERE user_id='$user_id'";

	$result = $con->query($query);

	if($result = mysqli_query($con, $query)){
		if($num_rows = mysqli_num_rows($result) > 0){
			$row = mysqli_fetch_assoc($result);
			$password = $row["u_password"];
			if(strcmp($u_password, $password) == 0){
				$data = array("success" => 1, "id" => $row["id"]);
				echo json_encode($data);
			} else{
				$data = array("success" => 0);
				echo json_encode($data);
			}
		} else{
			$data = array("success" => 0);
				echo json_encode($data);
		}
	} else {
		$data = array("success" => 0);
		echo json_encode($data);
	}

	mysqli_close($con);
?>