<!DOCTYPE html>
<html lang="nl">
<head>
	<meta charset="utf-8" />
	<title>Login</title>
	<script src="./js/jquery-2.1.3.min.js"></script>
	<script src="./js/bootstrap.min.js"></script>
	<link href="./css/bootstrap.min.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div id="main" class="container">
	<header>
		<h1>Login noob</h1>
	</header>
	<section>
		<form method="post" action="./klassen.php">
			<input type="number" name="id" placeholder="ID" />
			<input type="password" name="password" placeholder="Wachtwoord" />
			<input type="submit" value="Inloggen" />
		</form>
	</section>
	<footer>
		<hr />
		<p>&copy; 2015</p>
	</footer>
</div>
</body>
</html>