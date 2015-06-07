--Extension voor EXCLUDE USING gist daterange vergelijken
CREATE EXTENSION IF NOT EXISTS btree_gist;

--Een functie die een string uitvoerd als SQL en een daterange returnt
CREATE OR REPLACE FUNCTION daterange_eval(expression text) RETURNS daterange AS $$
DECLARE
	result daterange;
BEGIN
	EXECUTE expression INTO result;
	return result;
END
$$ LANGUAGE plpgsql IMMUTABLE;

--Algemene personen table om studenten en docenten te normaliseren
--Tussenvoegsel, straat en woonplaats zijn niet verplicht
CREATE TABLE personen (
	id								SERIAL						PRIMARY KEY,
	voornaam					text							NOT NULL,
	achternaam				text							NOT NULL,
	tussenvoegsel			text,
	geboortedatum			date							NOT NULL,
	geslacht					text							NOT NULL,
	straat						text,
	postcode					text							NOT NULL,
	woonplaats				text,
	telefoonnummer		text							NOT NULL,
--Telefoonnummers mogen alleen maar '0123456789 ()-.' bevatten met minimaal 1 teken
	CONSTRAINT telefoonnummer CHECK(telefoonnummer ~ '^[0-9\(\)\-\. ]+$'),
--Geslacht is case insensitive man/vrouw/onbepaald/onbekend
	CONSTRAINT geslacht CHECK(geslacht ~* '^(man|vrouw|onbepaald|onbekend)$'),
--Postcode moeten 4 cijfers gevolgd door 2 letters zijn
	CONSTRAINT postcode CHECK(postcode ~* '^[0-9]{4}[A-Z]{2}$')
);

CREATE TABLE studenten (
	id								integer					PRIMARY KEY,
	nummer						text							UNIQUE NOT NULL,
--Studentnummer moet 7 willekeurige tekens zijn
	CONSTRAINT zeven_tekens CHECK(nummer ~ '^.{7}$'),
--Id komt uit personen
	CONSTRAINT studenten_id_fkey FOREIGN KEY (id) REFERENCES personen(id) ON DELETE CASCADE
);

CREATE TABLE docenten (
	id								integer					PRIMARY KEY,
	code							text							UNIQUE NOT NULL,
--Medewerkerscode mag geen spaties bevatten
	CONSTRAINT geen_spaties CHECK(code !~ '( )'),
--Id komt uit personen
	CONSTRAINT docenten_id_fkey FOREIGN KEY (id) REFERENCES personen(id) ON DELETE CASCADE
);

CREATE TABLE klassen (
--Klassen hebben een door de database toegewezen id om te voorkomen dat start- en einddatum nodig zijn in tabellen die klassen referencen
--een klas kan namelijk dezelfde naam hebben in een ander jaar
	id								SERIAL						PRIMARY KEY,
	naam							text							NOT NULL,
	startdatum				date							NOT NULL,
	einddatum					date							CHECK(einddatum >= startdatum)
);

CREATE TABLE modules (
	code							text,
	naam							text							NOT NULL,
	beheerder					text,
	invoerdatum				date							NOT NULL,
	einddatum					date							CHECK(einddatum >= invoerdatum),
--Modulecodes kunnen worden hergebruikt in andere jaren
	PRIMARY KEY(code, einddatum),
--Modulecode mag alleen hoofdletters bevatten
	CONSTRAINT all_caps CHECK(code ~ '^[A-Z]+$'),
--Beheerder komt uit docenten
	CONSTRAINT modules_beheerder_fkey FOREIGN KEY (beheerder) REFERENCES docenten(code) ON DELETE CASCADE
);

CREATE TABLE roosters (
	klas							integer,
	lokaal						text							NOT NULL,
	startdatum				timestamp				NOT NULL,
	einddatum					timestamp				CHECK(einddatum >= startdatum),
	docent						text,
	PRIMARY KEY(lokaal, startdatum, einddatum),
--Klas komt uit klassen
	CONSTRAINT roosters_klas_fkey FOREIGN KEY (klas) REFERENCES klassen(id) ON DELETE CASCADE,
--Docent komt uit docenten
	CONSTRAINT roosters_docent_fkey FOREIGN KEY (docent) REFERENCES docenten(code) ON DELETE CASCADE,
--1 klas kan maar 1 les tegelijk volgen
--Dit vergelijkt de nieuwe row met alle bestaande rows
--Column klas wordt met klas vergeleken
--Timestamp range [startdatum, einddatum] wordt gecheckt op overlap
	CONSTRAINT een_klas EXCLUDE USING gist (
		klas WITH =,
		tsrange(startdatum, einddatum) WITH &&
	),
--1 lokaal kan maar 1 les tegelijk bevatten
	CONSTRAINT een_lokaal EXCLUDE USING gist (
		lokaal WITH =,
		tsrange(startdatum, einddatum) WITH &&
	),
--1 docent kan maar 1 les tegelijk geven
	CONSTRAINT een_docent EXCLUDE USING gist (
		docent WITH =,
		tsrange(startdatum, einddatum) WITH &&
	)
);

CREATE TABLE perioden (
	student						text,
	klas							integer,
	PRIMARY KEY(student, klas),
--Student komt uit studenten
	CONSTRAINT perioden_student_fkey FOREIGN KEY (student) REFERENCES studenten(nummer) ON DELETE CASCADE,
--Klas komt uit klassen
	CONSTRAINT perioden_klas_fkey FOREIGN KEY (klas) REFERENCES klassen(id) ON DELETE CASCADE,
--Een student kan maar in 1 klas tegelijk zitten
	CONSTRAINT een_jaar EXCLUDE USING gist (
		student WITH =,
--Dit zou toch beter moeten kunnen
--Daterange_eval(text) is de zelfgemaakte functie aan het begin van het bestand
--Replace(text1, text2, text3) vervangt text2 met text3 in text 1 om een text te returnen
--De klas van deze row wordt naar text gecast en in een soort prepared statement gestopt die wordt uitgevoerd door daterange_eval
--De resulterende daterange wordt gecheckt op overlap
		daterange_eval(replace(text 'SELECT daterange(startdatum, einddatum) FROM klassen WHERE id = ?', text '?', text(klas))) WITH &&
	)
);

CREATE TABLE planning (
	klas							integer,
	module						text,
	PRIMARY KEY(klas, module),
	CONSTRAINT planning_klas_fkey FOREIGN KEY(klas) REFERENCES klassen(id),
	CONSTRAINT planning_module_fkey FOREIGN KEY(module) REFERENCES modules(code)
);