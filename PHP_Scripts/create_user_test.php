<?php

	$con = mysqli_connect("localhost", "id1135692_monil", "password", "id1135692_locfeed");

   	if (mysqli_connect_errno()) {
    	echo "Failed to connect to MySQL: " . mysqli_connect_error();
   	}

	//$f_name = $_POST['f_name'];
	//$l_name = $_POST['l_name'];
	//$u_password = $_POST['u_password'];
	//$user_id = $_POST['user_id'];

	$f_name = "Monil"
	$l_name = "Bid";
	$u_password = "Password";
	$user_id = "monilbid";

   	$test_user_id_query = "SELECT user_id FROM users WHERE user_id='$user_id'";

	if($result = mysqli_query($con, $test_user_id_query)){
		$num_rows = mysqli_num_rows($result);
		mysqli_free_result($result);

		if($num_rows > 0){
			echo "User Exists";
		} else {
			$query = "INSERT INTO users(f_name, l_name, u_password, user_id, creation_date, creation_time) VALUES ('$f_name', '$l_name', '$u_password', '$user_id', CURDATE(), CURTIME())";

			if($con->query($query) === TRUE){
				echo "Success!"; 
			} else {
				echo "Error " . $query . " " . mysqli_error($con);
			}
		}
	} else {
		echo "Error in Select: " . mysqli_error($con);
	}

	$con->close();
	
?>