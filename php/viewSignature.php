<?php

require_once 'connection.php';
header('Content-Type: application/json ');
	class viewSignature {
		private $db;
		private $connection;

		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}

		public function find_signature($user){
			$query = "Select * from signatures where user = '$user'";
			$result = mysqli_query($this->connection, $query);
			if(mysqli_num_rows($result)==0){
				$data = array('empty' => 'No results found.');
				//$json['empty'] = 'No results found.';
			}else{
				$count = 0;
				$length = mysqli_num_rows($result);
				$data = array('success' => 'Results found.', 'length' => $length);
				while ($row = $result->fetch_assoc()) {
					$str = $row['signature'];
					$data [$count] = $str;
					$count = $count + 1; 
				}
				echo json_encode($data);
				mysqli_close($this->connection);
			}
			
		}
	}

	$viewSignature = new viewSignature();
	$data = array();
	if(isset($_REQUEST['user'])) {
		$user = $_REQUEST['user'];

		if(!empty($user)) {
		
			$viewSignature -> find_signature($user);
		}else {
			$data = array('error'=>'At least one field is empty');
			//$json['error'] = 'At least one field is empty';
			echo json_encode($data);
		}
	}

 	
 ?>