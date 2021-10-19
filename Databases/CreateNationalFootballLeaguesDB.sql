CREATE DATABASE NationalFootballLeagues;
GO

USE NationalFootballLeagues;

Create Table Leagues (
    id_league INT PRIMARY KEY IDENTITY,
	name_league VARCHAR(64),
	country_league VARCHAR(64)
);

Create Table Coaches(
    id_coach INT PRIMARY KEY IDENTITY,
	last_name_coach VARCHAR(64),
	first_name_coach VARCHAR(64)
);

Create Table Referees(
    id_referee INT PRIMARY KEY IDENTITY,
	last_name_referee VARCHAR(64),
	first_name_referee VARCHAR(64)
);

Create Table Rankings(
	id_ranking INT PRIMARY KEY IDENTITY,
	id_league INT FOREIGN KEY REFERENCES Leagues(id_league),
	name_ranking VARCHAR(64),
	Constraint unique_id_league UNIQUE (id_league)
)

Alter Table Rankings
DROP COLUMN name_ranking;

Create Table Teams(
	id_team INT PRIMARY KEY IDENTITY,
	id_ranking INT FOREIGN KEY REFERENCES Rankings(id_ranking),
	id_coach INT FOREIGN KEY REFERENCES Coaches(id_coach),
	name_team VARCHAR(64),
	Constraint unique_id_coach UNIQUE (id_coach)
);

Create Table Players(
	id_player INT PRIMARY KEY IDENTITY,
	id_team INT FOREIGN KEY REFERENCES Teams(id_team),
	last_name_player VARCHAR(64),
	first_name_player VARCHAR(64),
	position VARCHAR(64),
	salary MONEY
);

Create Table Games(
	id_game INT PRIMARY KEY IDENTITY,
	id_league INT FOREIGN KEY REFERENCES Leagues(id_league),
	id_team_host INT FOREIGN KEY REFERENCES Teams(id_team),
	id_team_guest INT FOREIGN KEY REFERENCES Teams(id_team),
	date_time DATETIME
);

Create Table Goals(
	id_goal INT PRIMARY KEY IDENTITY,
	id_player INT FOREIGN KEY REFERENCES Players(id_player),
	id_team INT FOREIGN KEY REFERENCES Teams(id_team)
);

Drop Table Goals

Create Table Goals(
	id_goal INT PRIMARY KEY IDENTITY,
	id_player INT FOREIGN KEY REFERENCES Players(id_player),
	id_game INT FOREIGN KEY REFERENCES Games(id_game)
);

Create Table Transfers(
	id_transfer INT PRIMARY KEY IDENTITY,
	id_player INT FOREIGN KEY REFERENCES Players(id_player),
	id_team_from INT FOREIGN KEY REFERENCES Teams(id_team),
	id_team_to INT FOREIGN KEY REFERENCES Teams(id_team),
	value_transfer MONEY
);

Create Table RefereesGames(
	id_referee INT FOREIGN KEY REFERENCES Referees(id_referee),
	id_game INT FOREIGN KEY REFERENCES Games(id_game),
	CONSTRAINT pk_RefereesGames PRIMARY KEY (id_referee, id_game)
);
