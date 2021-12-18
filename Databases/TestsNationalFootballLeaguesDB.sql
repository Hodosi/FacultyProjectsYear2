USE NationalFootballLeagues;
GO

DELETE FROM Goals;
GO

--crearea view-urilor
--view pe un tabel
CREATE VIEW vw_OnOneTable
AS
SELECT id_referee, last_name_referee FROM Referees;
GO

--view pe doua tabele care extrage doar arbitrii care au cel putin un meci
CREATE VIEW vw_OnTwoTable
AS
SELECT DISTINCT R.id_referee, R.last_name_referee FROM Referees R INNER JOIN RefereesGames RG
ON R.id_referee=RG.id_referee;
GO

--view pe doua tabele care numara cate meciuri are fiecare arbitru
CREATE VIEW vw_OnTwoTableWithGroupBy
AS
SELECT R.id_referee, R.last_name_referee, COUNT(RG.id_referee) AS nr_games FROM Referees R LEFT JOIN RefereesGames RG
ON R.id_referee=RG.id_referee GROUP BY R.id_referee, R.last_name_referee;
GO

-- stabilire tabele pentru test
INSERT INTO Tables (Name) VALUES ('Games'),('Referees'),('RefereesGames');
-- stabilire view-uri pentru test
INSERT INTO Views (Name) VALUES ('vw_OnOneTable'), ('vw_OnTwoTable'), ('vw_OnTwoTableWithGroupBy');
SELECT * FROM Tables;
SELECT * FROM Views;
--crearea unui test
INSERT INTO Tests (Name) VALUES ('test_1');
SELECT * FROM Tests;
--stabilirea view-urilor testate in primul test
INSERT INTO TestViews (TestID,ViewID) VALUES (1,1), (1,2), (1,3);
SELECT * FROM TestViews;
--stabilirea tabelelor testate in primul test
INSERT INTO TestTables (TestID, TableID, NoOfRows, Position) VALUES
(1,3,1000,1),(1,2,1000,2), (1,1,1000,3);
SELECT * FROM TestTables;
GO

CREATE OR ALTER PROCEDURE delete_all(@table_name varchar(64))
AS
BEGIN
	declare @query varchar(128)
	set @query = 'Delete from ' + @table_name
	exec(@query)
IF(@@ERROR<>0)
PRINT 'Error occurred';
END
GO


CREATE OR ALTER PROCEDURE insert_elements(@test_id INT, @table_name varchar(64))
AS
	DECLARE @table_id INT;
	SELECT @table_id = TableID FROM Tables WHERE Name = @table_name;

	DECLARE @nr_rows INT;
	SELECT @nr_rows = NoOfRows FROM TestTables WHERE TestID = @test_id AND TableID = @table_id;
	
	DECLARE @id_game INT;
	DECLARE @max_id_game INT;
	DECLARE @min_id_game INT;
	DECLARE @id_league INT;
	DECLARE @max_id_league INT;
	DECLARE @id_team_host INT;
	DECLARE @id_team_guest INT;
	DECLARE @max_id_team INT;
	DECLARE @min_id_team INT;
	DECLARE @date_time DATETIME;
	DECLARE @id_referee INT;
	DECLARE @max_id_referee INT;
	DECLARE @min_id_referee INT;
	DECLARE @last_name_referee varchar(64);
	DECLARE @first_name_referee varchar(64);


	DECLARE @i INT;
	SET @i = 0;

	WHILE @i < @nr_rows
	BEGIN
		IF @table_name = 'Games'
		BEGIN
		    --https://www.w3schools.com/sql/func_sqlserver_rand.asp
			SELECT @max_id_league = MAX(id_league) FROM Leagues;
			SELECT @id_league = FLOOR(RAND()*(@max_id_league-1+1)+1);
			SELECT @max_id_team = MAX(id_team) FROM Teams;
			SELECT @min_id_team = MIN(id_team) FROM Teams;
			SELECT @id_team_host = FLOOR(RAND()*(FLOOR(@max_id_team/2 + 1) - @min_id_team + 1) + @min_id_team);
			SELECT @id_team_guest = FLOOR(RAND()*(@max_id_team-FLOOR(@max_id_team/2 + 1) +1) + FLOOR(@max_id_team/2 + 1));
			SET @date_time = GETDATE();
			INSERT INTO Games(id_league, id_team_host, id_team_guest, date_time) VALUES
                       (@id_league, @id_team_host, @id_team_guest, @date_time);
		END

		ELSE IF @table_name = 'Referees'
		BEGIN
		    SET @last_name_referee = 'LastName ' + CONVERT(varchar(32), @i);
			SET @first_name_referee = 'FirstName ' + CONVERT(varchar(32), @i);
			INSERT INTO Referees(last_name_referee, first_name_referee) VALUES
			(@last_name_referee, @first_name_referee);
		END

		ELSE IF @table_name = 'RefereesGames'
		BEGIN
			SELECT @max_id_referee = MAX(id_referee) FROM Referees;
			SELECT @max_id_game = MAX(id_game) FROM Games;
			SELECT @min_id_referee = MIN(id_referee) FROM Referees;
			SELECT @min_id_game = MIN(id_game) FROM Games;
		    --https://www.w3schools.com/sql/func_sqlserver_rand.asp
			SELECT @id_referee = FLOOR(RAND()*(@max_id_referee-@min_id_referee+1)+@min_id_referee);
			SELECT @id_game = FLOOR(RAND()*(@max_id_game-@min_id_game+1)+@min_id_game);
			IF NOT EXISTS(SELECT * FROM RefereesGames WHERE id_referee = @id_referee AND id_game = @id_game)
			BEGIN
				INSERT INTO RefereesGames(id_referee, id_game) VALUES
				(@id_referee, @id_game);
			END
		END

		SET @i = @i + 1;
	END

IF(@@ERROR<>0)
PRINT 'Error occurred';
GO


CREATE OR ALTER PROCEDURE run_view(@nr_view INT)
as
    if @nr_view = 1
	    select * from vw_OnOneTable
    else if @nr_view = 2
		select * from vw_OnTwoTable
    else if @nr_view = 3
		select * from vw_OnTwoTableWithGroupBy
go

CREATE OR ALTER PROCEDURE run_tests
AS
	DECLARE @crt_test INT;
	SET @crt_test = 1;

	DECLARE @nr_tests INT;
	SELECT @nr_tests = MAX(TestID) FROM Tests;

	WHILE @crt_test <= @nr_tests
	BEGIN
		DECLARE @max_position INT;
		SELECT @max_position = MAX(Position) FROM TestTables;

		DECLARE @delete_start DATETIME;
		DECLARE @delete_end DATETIME;

		DECLARE @insert_start DATETIME;
		DECLARE @insert_end DATETIME;

		DECLARE @crt_position INT;
		SET @crt_position = 1;

		DECLARE @table_id INT;
		DECLARE @table_name VARCHAR(64);

		WHILE @crt_position <= @max_position
		BEGIN
			SELECT @table_id = TableID FROM TestTables WHERE Position = @crt_position;
			SELECT @table_name = Name FROM Tables WHERE TableID = @table_id;

			SET @delete_start = GETDATE();
			EXEC delete_all @table_name;
			SET @delete_end = GETDATE();
			
			DECLARE @last_test_run_id INT;
			SELECT @last_test_run_id = MAX(TestRunID) FROM TestRunTables;

			IF @last_test_run_id IS NULL
				SET @last_test_run_id = 0

			INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
			(@table_name, @delete_start, @delete_end);
			INSERT INTO TestRunTables(TestRunID, TableID, StartAt, EndAt) VALUES
			(@last_test_run_id + 1, @table_id, @delete_start, @delete_end);

			SET @crt_position = @crt_position + 1;
		END

		SET @crt_position = @max_position
		WHILE @crt_position >= 1
		BEGIN
			SELECT @table_id = TableID FROM TestTables WHERE Position = @crt_position;
			SELECT @table_name = Name FROM Tables WHERE TableID = @table_id;
			PRINT @table_name;
			SET @insert_start = GETDATE();
			EXEC insert_elements @crt_test, @table_name;
			SET @insert_end = GETDATE();
			
			INSERT INTO TestRuns(Description, StartAt, EndAt) VALUES
			(@table_name, @insert_start, @insert_end);

			SET @crt_position = @crt_position - 1;
		END

		DECLARE @nr_views INT;
		SELECT @nr_views = MAX(ViewID) FROM Views;

		DECLARE @crt_view INT;
		SET @crt_view = 1;

		WHILE @crt_view <= @nr_views
		BEGIN
			DECLARE @view_start DATETIME;
			DECLARE @view_end DATETIME;

			SET @view_start = GETDATE();
			EXEC run_view @crt_view;
			SET @view_end = GETDATE();

			DECLARE @last_test_view_id INT;
			SELECT @last_test_view_id = MAX(TestRunID) FROM TestRunViews;

			IF @last_test_view_id IS NULL
				SET @last_test_view_id = 0

			INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt) VALUES
			(@last_test_view_id + 1, @crt_view, @view_start, @view_end);
		    
			SET @crt_view = @crt_view + 1;
		END

		SET @crt_test = @crt_test + 1;
	END

IF(@@ERROR<>0)
BEGIN
PRINT 'Error occurred';
END
GO

EXEC run_tests
GO

DELETE FROM RefereesGames;
DELETE FROM Games;
DELETE FROM Referees;
DELETE FROM TestRunTables;
DELETE FROM TestRuns;
DELETE FROM TestRunViews;
GO

DBCC CHECKIDENT ('[Games]', RESEED, 0);
GO

DBCC CHECKIDENT ('[Referees]', RESEED, 0);
GO

DBCC CHECKIDENT ('[TestRuns]', RESEED, 0);
GO

DELETE FROM TestRuns
DELETE FROM TestRunTables
DELETE FROM TestRunViews
GO

SELECT * From TestRuns
Select * from TestRunTables
Select * from TestRunViews