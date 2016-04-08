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

		public function does_user_exist($username, $password, $first, $last, $email, $year){
			$query = "Select * from users where username = '$username'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)>0){
				$json['error'] = 'User already exists.';
				echo json_encode($json);
				mysqli_close($this->connection);
			}else{
				$graduatingYear = intval($year);
				$query = "Insert into users(first, last, email, graduatingYear, username, password, hours) values ('$first', '$last', '$email', '$graduatingYear', '$username', '$password', '0')";
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
	if(isset($_POST['password'], $_POST['username'], $_POST['first'], $_POST['last'], $_POST['email'], $_POST['year'])) {
		$password = $_POST['password'];
		$username = $_POST['username'];
		$first = $_POST['first'];
		$last = $_POST['last'];
		$email = $_POST['email'];
		$year = $_POST['year'];

		if(!empty($password)&&!empty($username)&&!empty($first)&&!empty($last)&&!empty($email)&&!empty($year)) {
			$encrypted_password = md5($password);
			$encrypted_first = md5($first);
			$encrypted_last = md5($last);
			$encrypted_email = md5($email);
			$user -> does_user_exist($username, $encrypted_password, $first, $last, $email, $year);
		}else {
			$json['error'] = 'You must fill in all fields';
			echo json_encode($json);
		}
	}

 	
 ?>