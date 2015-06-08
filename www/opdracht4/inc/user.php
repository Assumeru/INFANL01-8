<?php

class User {
	private $id;
	private $name;
	private $class;
	private $enrolled;

	public function __construct($id, $name, $class, $enrolled) {
		$this->id = (int)$id;
		$this->name = $name;
		$this->class = $class;
		$this->enrolled = (bool)$enrolled;
	}

	public function getId() {
		return $this->id;
	}

	public function getName() {
		return $this->name;
	}

	public function getClass() {
		return $this->class;
	}

	public function isEnrolled() {
		return $this->enrolled;
	}

	public static function checkLogin() {
		if(!empty($_POST['id']) && !empty($_POST['password'])) {
			return static::login($_POST['id'], static::hashPassword($_POST['password']));
		}
		return false;
	}

	private static function hashPassword($password) {
		return sha1($password);
	}

	private static function login($id, $password, $unsafe = Opdracht4::UNSAFE) {
		$db = Opdracht4::getDatabase();
		if($unsafe) {
			$query = $db->query('SELECT * FROM studenten WHERE id = "'.$id.'" AND wachtwoord = "'.$password.'"');
		} else {
			$query = $db->prepare('SELECT * FROM studenten WHERE id = ? AND wachtwoord = ?');
			$query->execute(array($id, $password));
		}
		if($row = $query->fetch()) {
			return new User($row['id'], $row['naam'], $row['klas'], $row['ingeschreven']);
		}
		return null;
	}
}