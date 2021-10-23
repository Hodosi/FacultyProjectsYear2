USE NationalFootballLeagues;

INSERT INTO Coaches(last_name_coach, first_name_coach) VALUES
('Stefano', 'Pioli'),
('Luciano', 'Spalletti'),
('Simone', 'Inzaghi'),
('Jose', 'Mourinho'),
('Massimiliano', 'Allegri'),
('Petrescu', 'Dan'),
('Iordanescu', 'Edward'),
('Bonetti', 'Dario'),
('Iosif', 'Mihai'),
('Hagi', 'Gheorghe');


INSERT INTO Referees(last_name_referee, first_name_referee) VALUES
('Mariani','Maurizio'),
('Di Bello','Marco'),
('Doveri','Daniele'),
('Massa','Davide'),
('Sozza','Simone'),
('Kovacs','Istvan'),
('Calin','Iulian'),
('Cojocaru','Adrian'),
('Coltescu','Sebastian'),
('Hategan','Ovidiu');

INSERT INTO Leagues(name_league, country_league) VALUES
('Serie A','ITALY'),
('Liga 1','ROMANIA');

INSERT INTO Rankings(id_league) VALUES
(1),
(2);

INSERT INTO Teams(id_ranking, id_coach, name_team) VALUES
(1, 1, 'AC Milan'),
(1, 2, 'Napoli'),
(1, 3, 'Inter'),
(1, 4, 'AS Roma'),
(1, 5, 'Juventus'),
(2, 6, 'CFR Cluj'),
(2, 7, 'FCSB'),
(2, 8, 'Dinamo'),
(2, 9, 'FC Rapid'),
(2, 10, 'Farul Constanta');

INSERT INTO Players(id_team, last_name_player, first_name_player, position, salary) VALUES
(1, 'Tatarusanu', 'Ciprian', 'Goalkeeper', 2.2),
(1, 'Hernandez', 'Theo', 'Defender', 4.5),
(1, 'Tonali', 'Sandro', 'Midfielder', 5.0),
(1, 'Bennacer', 'Ismael', 'Midfielder', 1.8),
(1, 'Ibrahimovic', 'Zlatan', 'Forward', 10.5),
(1, 'Leao', 'Rafael', 'Forward', 3.0),
(2, 'Ospina', 'David', 'Goalkeeper', 2.3),
(2, 'Rui', 'Mario', 'Defender', 0.7),
(2, 'Ruiz', 'Fabian', 'Midfielder', 3.1),
(2, 'Zielinski', 'Piotr', 'Midfielder', 2.9),
(2, 'Insigne', 'Lorenzo', 'Forward', 4.5),
(2, 'Mertens', 'Dries', 'Forward', 3.5),
(3, 'Handanovic', 'Samir', 'Goalkeeper', 1.4),
(3, 'Darmian', 'Matteo', 'Defender', 2.6),
(3, 'Calhanoglu', 'Hakan', 'Midfielder', 0.1),
(3, 'Vidal', 'Arturo', 'Midfielder', 0.9),
(3, 'Perisic', 'Ivan', 'Forward', 2.2),
(3, 'Sanchez', 'Alexis', 'Forward', 3.8),
(4, 'Patricio', 'Rui', 'Goalkeeper', 3.0),
(4, 'Smalling', 'Chris', 'Defender', 1.4),
(4, 'Cristante', 'Bryan', 'Midfielder', 1.6),
(4, 'El Shaarawy', 'Stephan', 'Midfielder', 4.7),
(4, 'Abraham', 'Tammy', 'Forward', 2.3),
(4, 'Perez', 'Carles', 'Forward', 4.0),
(5, 'Szczesny', 'Wojciech', 'Goalkeeper', 2.4),
(5, 'Bonucci', 'Leonardo', 'Defender', 4.6),
(5, 'Locatelli', 'Manuel', 'Midfielder', 5.1),
(5, 'Rabiot', 'Adrien', 'Midfielder', 4.9),
(5, 'Dybala', 'Paulo', 'Forward', 3.5),
(5, 'Morata', 'Alvaro', 'Forward', 2.5),
(6, 'Arlauskis', 'Giedrius', 'Goalkeeper', 2.1),
(6, 'Boateng', 'Nana', 'Defender', 0.9),
(6, 'Deac', 'Ciprian', 'Midfielder', 3.4),
(6, 'Culio', 'Emmanuel', 'Midfielder', 2.6),
(6, 'Paun', 'Alexandru', 'Forward', 7.8),
(6, 'Omrani', 'Billel', 'Forward', 6.2),
(7, 'Ter', 'Stegen', 'Goalkeeper', 3.1),
(7, 'Alba', 'Jordi', 'Defender', 0.9),
(7, 'Tanase', 'Florin', 'Midfielder', 9.5),
(7, 'Kroos', 'Toni', 'Midfielder', 2.5),
(7, 'Keseru', 'Claudiu', 'Forward', 5.5),
(7, 'Benzema', 'Karim', 'Forward', 2.5),
(8, 'Oblak', 'Jan', 'Goalkeeper', 9.5),
(8, 'Kimpembe', 'Presnel', 'Defender', 0.5),
(8, 'Torje', 'Gabriel', 'Midfielder', 12.5),
(8, 'Modric', 'Luka', 'Midfielder', 5.5),
(8, 'Suarez', 'Luis', 'Forward', 8.2),
(8, 'Messi', 'Lionel', 'Forward', 0.8),
(9, 'De Gea', 'David', 'Goalkeeper', 2.1),
(9, 'Varane', 'Raphael', 'Defender', 2.9),
(9, 'Pogba', 'Paul', 'Midfielder', 3.4),
(9, 'Reus', 'Marco', 'Midfielder', 4.6),
(9, 'Ronaldo', 'Cristiano', 'Forward', 5.5),
(9, 'Haaland', 'Erling', 'Forward', 15.5),
(10, 'Navas', 'Keylor', 'Goalkeeper', 5.3),
(10, 'Sergio', 'Ramos', 'Defender', 0.7),
(10, 'Ciobanu', 'Andrei', 'Midfielder', 4.0),
(10, 'Di Maria', 'Angel', 'Midfielder', 6.0),
(10, 'Mbappe', 'Kylian', 'Forward', 13.3),
(10, 'Neymar', 'da Silva Santos', 'Forward', 17.7);

INSERT INTO Games(id_league, id_team_host, id_team_guest, date_time) VALUES
(1,1,3,'2021-02-12 16:00:00')