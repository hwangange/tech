<?php
require_once 'connection.php';
header('Content-Type: application/json ');
	class Draft {
		private $db;
		private $connection;
		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}
		public function does_user_exist($username, $first, $last, $id, $class, $teacher, $servicedate, $hours, $log, $description, $paid, $studentsig, $orgname, $phonenum, $address, $website, $conname, $conemail, $consig, $date, $parsig){
					$uniqueid = uniqid($username);
					$id = intval($id);
					$class = intval($class);
					$hours = intval($hours);
					$phonenum = intval($phonenum);
					$query = "Insert into drafts (uniqueid, username, first, last, id, class, teacher, servicedate, hours, log, description, paid, studentsig, orgname, phonenum, website, address, conname, conemail, consig, date, parsig) values ('$uniqueid', '$username', '$first', '$last', '$id', '$class', '$teacher', '$servicedate', '$hours', '$log', '$description', '$paid', '$studentsig', '$orgname', '$phonenum', '$website', '$address', '$conname', '$conemail', '$consig', '$date', '$parsig')";
					$is_inserted = mysqli_query($this-> connection, $query);
					if($is_inserted == 1) {
						$json['success'] = 'Form created';
					}else{
						$json['error'] = ' Unable to create form ';
					}
					echo json_encode($json);
					mysqli_close($this-> connection);
				}
				
	}
	$draft = new Draft();
	if(isset($_POST['username'], $_POST['first'], $_POST['last'], $_POST['id'], $_POST['class'], $_POST['teacher'], $_POST['servicedate'], $_POST['hours'], $_POST['log'], $_POST['description'], $_POST['paid'], $_POST['studentsig'], $_POST['orgname'], $_POST['phonenum'], $_POST['website'], $_POST['address'], $_POST['conname'], $_POST['conemail'], $_POST['consig'], $_POST['date'], $_POST['parsig'])) {
		$username = $_POST['username'];
		$first = $_POST['first'];
		$last = $_POST['last'];
		$id = $_POST['id'];
		$class = $_POST['class'];
		$teacher = $_POST['teacher'];
		$servicedate = $_POST['servicedate'];
		$hours = $_POST['hours'];
		$log = $_POST['log'];
		$description = $_POST['description'];
		$paid = $_POST['paid'];
		$studentsig = $_POST['studentsig'];
		$orgname = $_POST['orgname'];
		$phonenum = $_POST['phonenum'];
		$website = $_POST['website'];
		$address = $_POST['address'];
		$conname = $_POST['conname'];
		$conemail = $_POST['conemail'];
		$consig = $_POST['consig'];
		$date = $_POST['date'];
		$parsig = $_POST['parsig'];
	}
	$draft -> does_user_exist($username, $first, $last, $id, $class, $teacher, $servicedate, $hours, $log, $description, $paid, $studentsig, $orgname, $phonenum, $website, $address, $conname, $conemail, $consig, $date, $parsig);
?>