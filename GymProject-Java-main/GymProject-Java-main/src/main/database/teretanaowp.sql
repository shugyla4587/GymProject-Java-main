DROP SCHEMA IF EXISTS teretanaowp;
CREATE SCHEMA teretanaowp DEFAULT CHARACTER SET utf8;
USE teretanaowp;

CREATE TABLE tipoviTreninga (
	id BIGINT AUTO_INCREMENT,
	ime VARCHAR(20) NOT NULL,
    opis VARCHAR(100) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE treninzi (
	id BIGINT AUTO_INCREMENT,
	naziv VARCHAR(20) NOT NULL,
	trener VARCHAR(20) NOT NULL,
	opis VARCHAR(100) NOT NULL,
	slika VARCHAR(150) NOT NULL,
	cena DOUBLE NOT NULL,
	vrstaTreninga ENUM('pojedinacni', 'grupni') DEFAULT 'pojedinacni',
	nivoTreninga ENUM('amaterski', 'srednji', 'napredni') DEFAULT 'amaterski',
    trajanje TIME NOT NULL,
    prosecnaOcena FLOAT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE treningTipTreninga (
    treningId BIGINT,
    tipTreningaId BIGINT,
    PRIMARY KEY(treningId, tipTreningaId),
    FOREIGN KEY(treningId) REFERENCES treninzi(id)
		ON DELETE CASCADE,
    FOREIGN KEY(tipTreningaId) REFERENCES tipoviTreninga(id)
		ON DELETE CASCADE
);

CREATE TABLE korisnici (
	id BIGINT AUTO_INCREMENT,
	korisnickoIme VARCHAR(20) NOT NULL,
	lozinka VARCHAR(20) NOT NULL,
	email VARCHAR(20) NOT NULL,
	ime VARCHAR(10) NOT NULL,
	prezime VARCHAR(20) NOT NULL,
	datumRodjenja DATE NOT NULL,
	adresa VARCHAR(30) NOT NULL,
	brojTelefona VARCHAR(20) NOT NULL,
	datumIVremeRegistracije DATETIME NOT NULL,
	uloga ENUM('administrator', 'korisnik') DEFAULT 'korisnik',
    blokiran BOOL DEFAULT false,
	PRIMARY KEY(id)
);

CREATE TABLE sale (
	id BIGINT AUTO_INCREMENT,
    oznakaSale VARCHAR(20) NOT NULL,
    kapacitet INT NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE termini (
	id BIGINT AUTO_INCREMENT,
    salaId BIGINT NOT NULL,
    treningId BIGINT NOT NULL,
	datum DATETIME NOT NULL,
    kapacitet INT NOT NULL,
	PRIMARY KEY(id),
    FOREIGN KEY(salaId) REFERENCES sale(id)
		ON DELETE CASCADE,
	FOREIGN KEY(treningId) REFERENCES treninzi(id)
		ON DELETE CASCADE
);

CREATE TABLE korpa (
	id BIGINT AUTO_INCREMENT,
    terminId BIGINT NOT NULL,
	korisnikId BIGINT NOT NULL,
	cena DOUBLE NOT NULL,
    aktivna BOOL DEFAULT true,
	PRIMARY KEY(id),
	FOREIGN KEY(terminId) REFERENCES termini(id)
		ON DELETE CASCADE,
	FOREIGN KEY(korisnikId) REFERENCES korisnici(id)
		ON DELETE CASCADE
);

CREATE TABLE zeljeniTreninzi (
	id BIGINT AUTO_INCREMENT,
    korisnikId BIGINT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(korisnikId) REFERENCES korisnici(id)
		ON DELETE CASCADE
);

CREATE TABLE zeljeniTreninziTrening (
    zeljeniTreningId BIGINT,
    treningId BIGINT,
    PRIMARY KEY(zeljeniTreningId, treningId),
    FOREIGN KEY(zeljeniTreningId) REFERENCES zeljeniTreninzi(id)
		ON DELETE CASCADE,
    FOREIGN KEY(treningId) REFERENCES treninzi(id)
		ON DELETE CASCADE
);

CREATE TABLE kartice (
	id BIGINT AUTO_INCREMENT,
    popust INT NOT NULL,
    brojPoena INT NOT NULL,
    korisnikId BIGINT NOT NULL,
    odobrena BOOL DEFAULT false,
	PRIMARY KEY(id),
	FOREIGN KEY(korisnikId) REFERENCES korisnici(id)
		ON DELETE CASCADE
);

CREATE TABLE zakazani (
	id BIGINT AUTO_INCREMENT,
    ukupnaCena double NOT NULL,
    datumZakazivanja DATETIME NOT NULL,
    korisnikId BIGINT NOT NULL,
	ukupanBroj INT NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(korisnikId) REFERENCES korisnici(id)
		ON DELETE CASCADE
);

CREATE TABLE zakazaniKorpa (
    zakazaniId BIGINT,
    korpaId BIGINT,
    PRIMARY KEY(zakazaniId, korpaId),
    FOREIGN KEY(zakazaniId) REFERENCES zakazani(id)
		ON DELETE CASCADE,
    FOREIGN KEY(korpaId) REFERENCES korpa(id)
		ON DELETE CASCADE
);

CREATE TABLE komentari (
	id BIGINT AUTO_INCREMENT,
    tekst VARCHAR(200) NOT NULL,
    ocena INT NOT NULL,
    datum DATE NOT NULL,
    korisnikId BIGINT NOT NULL,
    treningId BIGINT NOT NULL,
    statusKomentara ENUM('naCekanju', 'odobren', 'nijeOdobren') DEFAULT 'naCekanju',
	PRIMARY KEY(id),
    FOREIGN KEY(treningId) REFERENCES treninzi(id)
		ON DELETE CASCADE,
	FOREIGN KEY(korisnikId) REFERENCES korisnici(id)
		ON DELETE CASCADE
);


INSERT INTO tipoviTreninga (id, ime, opis) VALUES (1, 'Fitness', 'Funkcionalni trening.');
INSERT INTO tipoviTreninga (id, ime, opis) VALUES (2, 'Cardio', 'Trening za postizanje i odrzavanje kondicije.');
INSERT INTO tipoviTreninga (id, ime, opis) VALUES (3, 'Yoga', 'Trening koji opusta telo i misli.');


INSERT INTO treninzi (id, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena) VALUES (1, 'CardioBox trening', 'Milos Mikic', 'Trening je inspirisan borilackim vestinama idealan za svakog pojedinca.', 'images/box.jpg', 2000.0, 'pojedinacni', 'srednji', '01:00:00', 4.8);
INSERT INTO treninzi (id, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena) VALUES (2, 'Pilates trening', 'Ema Emic', 'Trening je idealan za svakog pojedinca i za svaki fitness nivo.', 'images/pilates.jpg', 1500.0, 'grupni', 'amaterski', '01:15:00', 4.6);
INSERT INTO treninzi (id, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena) VALUES (3, 'Yoga trening', 'Tea Todorovic', 'Trening je idealan za svakog pojedinca i za svaki fitness nivo.', 'images/yoga.jpg', 2000.0, 'grupni', 'napredni', '01:00:00', 5.0);
INSERT INTO treninzi (id, naziv, trener, opis, slika, cena, vrstaTreninga, nivoTreninga, trajanje, prosecnaOcena) VALUES (4, 'Zumba trening', 'Nina Nikolic', 'Trening je idealan za svakog pojedinca i za svaki fitness nivo.', 'images/zumba.jpg', 1900.0, 'grupni', 'srednji', '01:00:00', 5.0);

INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (1, 2);
INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (2, 1);
INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (3, 3);
INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (4, 1);
INSERT INTO treningTipTreninga (treningId, tipTreningaId) VALUES (4, 2);

INSERT INTO korisnici(id, korisnickoIme, lozinka, email, ime, prezime, datumRodjenja, adresa, brojTelefona, datumIVremeRegistracije, uloga, blokiran) VALUES (1, 'pera', 'pera', 'pera@gmail.com', 'Pera', 'Peric', '1990-02-12', 'Bulevar Evrope 12, Novi Sad', '0620000000', '2022-01-01 17:00', 'administrator', false);
INSERT INTO korisnici(id, korisnickoIme, lozinka, email, ime, prezime, datumRodjenja, adresa, brojTelefona, datumIVremeRegistracije, uloga, blokiran) VALUES (2, 'ana', 'ana', 'ana@gmail.com', 'Ana', 'Ana', '1998-05-07', 'Jevrejska 25, Novi Sad', '0630000000', '2022-01-01 19:00', 'korisnik', false);
INSERT INTO korisnici(id, korisnickoIme, lozinka, email, ime, prezime, datumRodjenja, adresa, brojTelefona, datumIVremeRegistracije, uloga, blokiran) VALUES (3, 'nikola', 'nikola', 'nikola@gmail.com', 'Nikola', 'Nikolic', '1997-05-09', 'Heroja Pinkija 55, Novi Sad', '0630000001', '2022-01-01 20:00', 'korisnik', true);

INSERT INTO sale (id, oznakaSale, kapacitet) VALUES (1, 'Sala 1', 5);
INSERT INTO sale (id, oznakaSale, kapacitet) VALUES (2, 'Sala 2', 4);
INSERT INTO sale (id, oznakaSale, kapacitet) VALUES (3, 'Sala 3', 3);
INSERT INTO sale (id, oznakaSale, kapacitet) VALUES (4, 'Sala 4', 5);

INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (1, 1, 2, '2022-02-22 08:00', 5);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (2, 1, 2, '2022-02-22 18:00', 5);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (3, 4, 2, '2022-02-23 08:00', 5);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (4, 3, 1, '2022-02-22 20:00', 1);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (5, 2, 1, '2022-02-27 15:00', 1);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (6, 2, 3, '2022-02-22 08:00', 4);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (7, 2, 3, '2022-02-24 20:00', 4);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (8, 4, 4, '2022-02-22 20:00', 5);
INSERT INTO termini (id, salaId, treningId, datum, kapacitet) VALUES (9, 3, 4, '2022-02-22 15:00', 3);


INSERT INTO korpa (id, terminId, korisnikId, cena, aktivna) VALUES (1, 3, 2, 2000.0, true);

INSERT INTO zeljeniTreninzi (id, korisnikId) VALUES (1, 2);

INSERT INTO zeljeniTreninziTrening (zeljeniTreningId, treningId) VALUES (1, 3);

INSERT INTO kartice (popust, brojPoena, korisnikId, odobrena) VALUES (50, 10, 2, true);

INSERT INTO komentari (tekst, ocena, datum, korisnikId, treningId, statusKomentara) VALUES ('Vrlo dinamican trening, sve preporuke.', 5, '2021-01-10', 2, 4, 'odobren');
INSERT INTO komentari (tekst, ocena, datum, korisnikId, treningId, statusKomentara) VALUES ('Jako prijatna atmosfera.', 4, '2021-01-11', 3, 3, 'odobren');


