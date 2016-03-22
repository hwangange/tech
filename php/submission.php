<?php
require_once 'connection.php';
header('Content-Type: application/json ');
	class Submission {
		private $db;
		private $connection;
		function __construct(){
			$this->db = new DB_Connection();
			$this->connection = $this->db->get_connection();
		}
		public function does_user_exist($first, $last, $id, $class, $teacher, $servicedate, $hours, $log, $description, $paid, $studentsig, $orgname, $phonenum, $address, $website, $conname, $conemail, $consig, $date, $parsig){
				$query = "Select * from users where first = '$first' and last = '$last'";
				$result = mysqli_query($this-> connection, $query);
				if(mysqli_num_rows($result)>0){
					$json['success1'] = 'User found.';
				}else{
					$id = intval($id);
					$class = intval($class);
					$hours = intval($hours);
					$phonenum = intval($phonenum);

					$query = "Insert into submission (first, last, id, class, teacher, servicedate, hours, log, description, paid, studentsig, orgname, phonenum, website, address, conname, conemail, consig, date, parsig) values ('$first', '$last', '$id', '$class', '$teacher', '$servicedate', '$hours', '$log', '$description', '$paid', '$studentsig', '$orgname', '$phonenum', '$website', '$address', '$conname', '$conemail', '$consig', '$date','$parsig')";
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
	}
	$submission = new Submission();
	if(isset($_POST['first'], $_POST['last'], $_POST['id'], $_POST['class'], $_POST['teacher'], $_POST['servicedate'], $_POST['hours'], $_POST['log'], $_POST['description'], $_POST['paid'], $_POST['studentsig'], $_POST['orgname'], $_POST['phonenum'], $_POST['website'], $_POST['address'], $_POST['conname'], $_POST['conemail'], $_POST['consig'], $_POST['date'], $_POST['parsig'])) {
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
	if(!empty($first)&&!empty($last)&&!empty($id)&&!empty($class)&&!empty($teacher)&&!empty($servicedate)&&!empty($hours)&&!empty($log)&&!empty($description)&&!empty($paid)&&!empty($studentsig)&&!empty($orgname)&&!empty($phonenum)&&!empty($website)&&!empty($address)&&!empty($conname)&&!empty($conemail)&&!empty($consig)&&!empty($date)&&!empty($parsig)){
			$encrypted_first = md5($first);
			$encrypted_last = md5($last);
			$encrypted_email = md5($conemail);
			$submission -> does_user_exist($first, $last, $id, $class, $teacher, $servicedate, $hours, $log, $description, $paid, $studentsig, $orgname, $phonenum, $address, $website, $conname, $conemail, $consig, $date, $parsig);
		}else {
			$json['error'] = 'You must fill in all fields';
			echo json_encode($json);
		}
	
?>