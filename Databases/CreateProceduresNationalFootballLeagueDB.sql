USE NationalFootballLeagues
GO

CREATE TABLE Versions
(id_v INT PRIMARY KEY IDENTITY,
 current_version INT
);
GO

INSERT INTO Versions (current_version) VALUES (0);
GO

SELECT * FROM Versions
GO

CREATE OR ALTER PROCEDURE add_table_v1
AS
BEGIN
CREATE TABLE Fans(
    id_fan INT PRIMARY KEY IDENTITY,
	id_team INT FOREIGN KEY REFERENCES Teams(id_team),
	last_name VARCHAR(64),
	first_name VARCHAR(64),
	rank_fan INT
);
IF(@@ERROR=0)
UPDATE Versions SET current_version=1 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC add_table_v1
GO

CREATE OR ALTER PROCEDURE alter_column_type_v2
AS
BEGIN
ALTER TABLE Fans
ALTER COLUMN rank_fan VARCHAR(64);
IF(@@ERROR=0)
UPDATE Versions SET current_version=2 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC alter_column_type_v2
GO

CREATE OR ALTER PROCEDURE add_default_constraint_v3
AS
BEGIN
ALTER TABLE Fans
ADD CONSTRAINT df_rank_fan DEFAULT 'Beginner' FOR rank_fan;
IF(@@ERROR=0)
UPDATE Versions SET current_version=3 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC add_default_constraint_v3
GO

CREATE OR ALTER PROCEDURE add_new_column_v4
AS
BEGIN
ALTER TABLE Fans
ADD id_league INT;
IF(@@ERROR=0)
UPDATE Versions SET current_version=4 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC add_new_column_v4
GO

CREATE OR ALTER PROCEDURE add_foreign_key_v5
AS
BEGIN
ALTER TABLE Fans
ADD CONSTRAINT fk_id_league FOREIGN KEY (id_league) REFERENCES Leagues(id_league);
IF(@@ERROR=0)
UPDATE Versions SET current_version=5 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC add_foreign_key_v5
GO


CREATE OR ALTER PROCEDURE undo_foreign_key_v5
AS
BEGIN
ALTER TABLE Fans
DROP CONSTRAINT fk_id_league;
IF(@@ERROR=0)
UPDATE Versions SET current_version=4 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC undo_foreign_key_v5
GO

CREATE OR ALTER PROCEDURE undo_new_column_v4
AS
BEGIN
ALTER TABLE Fans
DROP COLUMN id_league;
IF(@@ERROR=0)
UPDATE Versions SET current_version=3 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC undo_new_column_v4
GO

CREATE OR ALTER PROCEDURE undo_default_constraint_v3
AS
BEGIN
ALTER TABLE Fans
DROP CONSTRAINT df_rank_fan;
IF(@@ERROR=0)
UPDATE Versions SET current_version=2 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC undo_default_constraint_v3
GO

CREATE OR ALTER PROCEDURE undo_alter_column_type_v2
AS
BEGIN
ALTER TABLE Fans
ALTER COLUMN rank_fan INT;
IF(@@ERROR=0)
UPDATE Versions SET current_version=1 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC undo_alter_column_type_v2
GO

CREATE OR ALTER PROCEDURE undo_add_table_v1
AS
BEGIN
DROP TABLE Fans;
IF(@@ERROR=0)
UPDATE Versions SET current_version=0 WHERE id_v =1;
ELSE
PRINT 'Error occurred';
END
GO

EXEC undo_add_table_v1
GO

CREATE OR ALTER PROCEDURE get_next_version(@version INT)
as
    if @version = 1
		exec add_table_v1
    else if @version = 2
		exec alter_column_type_v2
    else if @version = 3
		exec add_default_constraint_v3
    else if @version = 4
		exec add_new_column_v4
    else if @version = 5
		exec add_foreign_key_v5
go

CREATE OR ALTER PROCEDURE get_prev_version(@version INT)
as
    if @version = 5
		exec undo_foreign_key_v5
    else if @version = 4
		exec undo_new_column_v4
    else if @version = 3
		exec undo_default_constraint_v3
    else if @version = 2
		exec undo_alter_column_type_v2
    else if @version = 1
		exec undo_add_table_v1
go

CREATE OR ALTER PROCEDURE change_version(@table_version INT)
AS
BEGIN
    IF @table_version < 0 or @table_version > 5
	    RAISERROR('INVALID VERSION', 11, 1);
		IF(@@ERROR<>0)
		    RETURN
		DECLARE @crt_version INT = 1;
		SELECT @crt_version = current_version FROM Versions WHERE id_v = 1;
		if @crt_version < @table_version
		    while @crt_version < @table_version
			begin
			    set @crt_version = @crt_version + 1;
			    exec get_next_version @version = @crt_version;
            end
		else if @crt_version > @table_version
		    while @crt_version > @table_version
			begin
			    exec get_prev_version @version = @crt_version;
				set @crt_version = @crt_version - 1;
            end
END
GO

EXEC change_version @table_version = 0
GO