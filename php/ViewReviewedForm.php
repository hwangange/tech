<?php
	require_once 'connection.php';
	header('Content-Type: application/json ');

	class View {

		private $db;
		private $connection;

		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}

		public function find_Form($uniqueIDmessage){

				$query = "Select * from submission where uniqueid= '$uniqueIDmessage'";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)==0){
					$data = array('empty' => 'No results found.');
							//$json['empty'] = 'No results found.';
				}else{
					$count = 0;
					$length = mysqli_num_rows($result);
					$data = array('success' => 'Results found.');

					while ($row = $result->fetch_assoc()) {
						$data[] = $row;
					}
				} 

				/*if(mysql_num_rows($result)) {
					while($row = mysql_fetch_assoc($result)) {
						$data['emp_info'][] = $row;
					}
				}*/

				echo json_encode($data);
				mysqli_close($this->connection);
		}

	}

	$view = new View();
	$data = array();
	if(isset($_REQUEST['uniqueIDmessage'])) {
		$uniqueIDmessage = $_REQUEST['uniqueIDmessage'];

		if(!empty($uniqueIDmessage)) {
		
			$view -> find_Form($uniqueIDmessage);
		}else {
			$data = array('error'=>'At least one field is empty');
			//$json['error'] = 'At least one field is empty';
			echo json_encode($data);
		}
	}
?>