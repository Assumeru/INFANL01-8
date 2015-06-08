<?php

class Klas {
	public static function getStudents($class, $unsafe = Opdracht4::UNSAFE) {
		$students = array();
		$db = Opdracht4::getDatabase();
		if($unsafe) {
			$query = $db->query('SELECT * FROM studenten WHERE ingeschreven = 1 AND klas = "'.$class.'"');
		} else {
			$query = $db->prepare('SELECT * FROM studenten WHERE ingeschreven = 1 AND klas = ?');
			$query->execute(array($class));
		}
		while($row = $query->fetch()) {
			$students[] = new User($row['id'], $row['naam'], $row['klas'], $row['ingeschreven']);
		}
		return $students;
	}
}