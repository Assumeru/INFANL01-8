<?php
	require './inc/main.php';
	$user = User::checkLogin();
	if(!$user) {
		header('Location: ./index.php');
		die;
	}
?>
<!DOCTYPE html>
<html lang="nl">
<head>
	<meta charset="utf-8" />
	<title>Klassen</title>
	<script src="./js/jquery-2.1.3.min.js"></script>
	<script src="./js/bootstrap.min.js"></script>
	<link href="./css/bootstrap.min.css" type="text/css" rel="stylesheet" />
</head>
<body>
<div id="main" class="container">
	<header>
		<h1>Klassen</h1>
		<p>Welkom, <?php echo htmlentities($user->getName()); ?>!</p>
	</header>
	<section>
		<form method="post">
			<input type="hidden" name="id" value="<?php echo htmlentities($_POST['id']); ?>" />
			<input type="hidden" name="password" value="<?php echo htmlentities($_POST['password']); ?>" />
			<input type="text" placeholder="Klas" name="class" value="<?php echo (isset($_POST['class']) ? htmlentities($_POST['class']) : ''); ?>" />
			<input type="submit" />
		</form>
	</section>
<?php
	if(isset($_POST['class'])) {
?>
	<section>
<?php
		$students = Klas::getStudents($_POST['class']);
		if(!empty($students)) {
			echo '<table class="table">
				<tr>
					<th>ID</th><th>Naam</th><th>Klas</th><th>Ingeschreven</th>
				</tr>';
			foreach($students as $student) {
				echo '<tr>
					<td>',$student->getId(),'</td>
					<td>',htmlentities($student->getName()),'</td>
					<td>',htmlentities($student->getClass()),'</td>
					<td><input type="checkbox" disabled ',($student->isEnrolled() ? 'checked' : ''),' /></td>
				</tr>';
			}
			echo '</table>';
		} else {
			echo '<p>Er zijn geen studenten gevonden voor deze klas.</p>';
		}
?>
	</section>
<?php
	}
?>
	<footer>
		<hr />
		<p>&copy; 2015</p>
	</footer>
</div>
</body>
</html>