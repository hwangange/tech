<?php
	require_once 'connection.php';
	header('Content-Type: application/json ');
	class checkHours {
		private $db;
		private $connection;
		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}
		public function check($username){
				$query = "Select * from users where username = '$username'";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)==0){
					$data['submissionSelectEmpty'] = 'Could\'t find form in submission table using unique ID.';
					
							
				}else{
					$data['submissionSelectSuccess'] = 'Results found.';
					$submitHours = 0;

					while ($row = $result->fetch_assoc()) {
						$submitHours = $row['hours'];
					}

					$data['submitHours'] = "submitHours = " . $submitHours;

					$query = "Select * from approved where username = '$username'";
					$result = mysqli_query($this->connection, $query);

					$totalHours = 0;

					while ($row = $result->fetch_assoc()) {
						$totalHours = $totalHours + $row['hours'];
					}

					$data['totalHours'] = $totalHours;

					if($submitHours == $totalHours)
						$data['equal'] = "Hours are up to date.";
					else {
						$query = "UPDATE users SET hours = " . $totalHours . " WHERE username = '$username'";
						$result = mysqli_query($this->connection, $query);
						if($result== TRUE) {
							$data['successUpdate'] = 'Successfully updated hours.';
						} else {
							$data['errorUpdate'] = "Couldn't update hours.";
						}
					} 
					echo json_encode($data);
					mysqli_close($this->connection);
			}
		}
	}
	$check = new checkHours();
	$data = array();
	if($_POST['username']) {
		$username = $_POST['username'];
		if(!empty($username)) {
 			$check -> check($username);
		}else {
			$data = array('error'=>'Could not process request.');
			echo json_encode($data);
		}
	}
?>