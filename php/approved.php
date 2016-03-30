<?php
	require_once 'connection.php';
	header('Content-Type: application/json ');

	class pending {

		private $db;
		private $connection;

		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}

		public function find_pending($user){

				$query = "Select * from approved where username = '$user'";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)==0){
					$data = array('empty' => 'No results found.');
							//$json['empty'] = 'No results found.';
				}else{
					$count = 0;
					$length = mysqli_num_rows($result);
					$data = array('success' => 'Results found.', 'length' => $length);

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

	$pending = new pending();
	$data = array();
	if(isset($_REQUEST['user'])) {
		$user = $_REQUEST['user'];

		if(!empty($user)) {
		
			$pending -> find_pending($user);
		}else {
			$data = array('error'=>'At least one field is empty');
			//$json['error'] = 'At least one field is empty';
			echo json_encode($data);
		}
	}
?>