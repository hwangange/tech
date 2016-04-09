<?php
	require_once 'connection.php';
	header('Content-Type: application/json ');
	class Adminapprove {
		private $db;
		private $connection;
		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}
		public function insert_Form($uniqueIDmessage){

				$query = "SELECT * from submission WHERE uniqueid = '$uniqueIDmessage'";
				$result = mysqli_query($this->connection, $query);

				if(mysqli_num_rows($result)==0){
					$data['submissionSelectEmpty'] = 'Could\'t find form in submission table using unique ID.';
					
							
				}else{
					$data['submissionSelectSuccess'] = 'Results found.';
                                        $username = "";

					while ($row = $result->fetch_assoc()) {
						$submitHours = $row['hours'];
						$username = $row['username'];
					}

                                        $data['username'] = "Username = " . $username;

					$query = "SELECT * from users WHERE username = '$username'";
					$result = mysqli_query($this->connection, $query);

					if(mysqli_num_rows($result)==0){
						$data['userSelectEmpty'] = 'User not found in users table.';
						echo json_encode($data);
						mysqli_close($this->connection);
								
					}else{
						$count = 0;
						$length = mysqli_num_rows($result);
						$data['userSelectSuccess'] = 'User found in users table.';

						while ($row = $result->fetch_assoc()) {
							$currentHours = $row['hours'];
						}

						$newHours = $submitHours + $currentHours;
                                                $data['hours'] = $newHours;

						$query = "UPDATE users SET hours = " . $newHours . " WHERE username = '$username'";
						$result = mysqli_query($this->connection, $query);
						if($result== TRUE) {
							$data['successUpdate'] = 'Successfully updated hours.';

							$query = "INSERT INTO approved select * from submission where uniqueid = '$uniqueIDmessage'";
							$result = mysqli_query($this->connection, $query);
							if($result== 1) {
								$data['successInsert'] = 'Inserted into approved table.';

								$query = "DELETE FROM submission where uniqueid = '$uniqueIDmessage'";
								$result = mysqli_query($this->connection, $query);
								if($result== 1) {
									$data['successDelete'] = 'Deleted from submission table.';
								}

								else {
									$data['errorInsert'] = 'Couldn\'t delete from submission table.';
								}
							}

							else {
								$data['errorInsert']= 'Couldn\'t insert into approved table.';
							}
						}

						else {
							$data['errorUpdate']= 'Couldn\'t update hours.';
						}
					} 
				} 
 
				echo json_encode($data);
				mysqli_close($this->connection);
		}
	}
	$approve = new Adminapprove();
	$data = array();
	if($_POST['uniqueIDmessage']) {
		$uniqueIDmessage = $_POST['uniqueIDmessage'];
		if(!empty($uniqueIDmessage)) {
 			$approve -> insert_Form($uniqueIDmessage);
		}else {
			$data = array('error'=>'Could not process request.');
			echo json_encode($data);
		}
	}
?>