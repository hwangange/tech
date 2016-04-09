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
				$query = "INSERT INTO denied select * from submission where uniqueid = '$uniqueIDmessage'";
				$result = mysqli_query($this->connection, $query);
				if($result== 1) {
					$data = array('successInsert' => 'Inserted into denied table.');
				}

				else {
					$data = array('errorInsert' => 'Couldn\'t insert into denied table.');
				}

				$query = "DELETE FROM submission where uniqueid = '$uniqueIDmessage'";
				$result = mysqli_query($this->connection, $query);
				if($result== 1) {
					$data['successDelete'] = 'Deleted from submission table.';
				}

				else {
					$data['errorInsert'] = 'Couldn\'t delete from submission table.';
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