<?php

require_once 'connection.php';
header('Content-Type: application/json ');
	class Signature {
		private $db;
		private $connection;

		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}

		public function insert_signature($user, $sig){
			$query = "Insert into signatures(user, signature) values('$user', '$sig')";
			$is_inserted = mysqli_query($this->connection, $query);
			if($is_inserted == 1) {
				$json['success'] = 'Signature insertion success';		
			}else{
				$json['error'] = 'Signature insertion error';
			}
			echo json_encode($json);
			mysqli_close($this->connection);
		}
	}

	$signature = new Signature();
	if(isset($_POST['user'], $_POST['sig'])) {
		$user = $_POST['user'];
		$sig = $_POST['sig'];

		if(!empty($user)&&!empty($sig)) {
		
			$signature -> insert_signature($user, $sig);
		}else {
			$json['error'] = 'At least one field is empty';
			echo json_encode($json);
		}
	}

 	
 ?>