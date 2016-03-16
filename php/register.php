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
			$query = "Select * from users where username = '$username'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$json['error'] = 'User already exists.';
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$query = "Insert into users(username, password) values ('$username', '$password')";
				$is_inserted = mysqli_query($this->connection, $query);
				if($is_inserted == 1) {
					$json['success'] = 'Account created';
				}else{
					$json['error'] = ' Wrong password ';
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