<?php

require_once 'connection.php';
header('Content-Type: application/json ');
	class User {
		private $db;
		private $connection;

		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}

		public function does_user_exist($username, $password){
			$query = "Select * from users where username = '$username' and password = '$password' ";
			$result = mysqli_query($this->connection, $query);
			$name = "";
			$email = "";
			$year = "";
			if(mysqli_num_rows($result)>0){
				while ($row = $result->fetch_assoc()) {
					$name = $row['first'] . " " . $row['last']; 
					$email = $row['email'];
					$year = $row['graduatingYear'];
				}
				$json['success'] = 'Welcome '.$username;
				$json['user'] = $username;
				$json['name'] = $name;
				$json['email'] = $email;
				$json['year'] = $year;
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$query = "Select * from users where username = '$username'";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)>0){
				$json['error'] = 'Wrong password';	
			}else{
					$json['error'] = 'User does not exist';
			}

				echo json_encode($json);
				mysqli_close($this->connection);
			}
		}
	}

	$user = new User();
	if(isset($_POST['password'],$_POST['username'])) {
		$password = $_POST['password'];
		$username = $_POST['username'];

		if(!empty($password)&&!empty($username)) {
			$encrypted_password = md5($password);
			$user -> does_user_exist($username, $encrypted_password);
		}else {
			$json['error'] = 'You must fill both fields';
			echo json_encode($json);
		}
	}

 	
 ?>