<?php

class Opdracht4 {
	const UNSAFE = true;
	private static $db;

	public static function init() {
		spl_autoload_register('Opdracht4::autoLoad');
		static::createDatabase();
	}

	private static function createDatabase($db = 'opdracht4', $host = '127.0.0.1', $username = 'root', $password = '') {
		self::$db = new PDO('mysql:dbname='.$db.';host='.$host, $username, $password);
		self::$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	}

	public static function getDatabase() {
		return self::$db;
	}

	public static function autoLoad($className) {
		$file = './inc/'.strtolower($className).'.php';
		if(file_exists($file)) {
			include $file;
		}
	}
}

Opdracht4::init();