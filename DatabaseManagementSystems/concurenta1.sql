USE NationalFootballLeagues
GO

SELECT * FROM Meciuri

--dirty reads
BEGIN
    BEGIN
	    PRINT 'start dirty 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		BEGIN TRAN;
		INSERT INTO Meciuri(echipa_1, echipa_2) VALUES ('CFR', 'FCSB')
		WAITFOR DELAY '00:00:07';
		ROLLBACK TRAN;
		PRINT 'finish dirty 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
    END
	
	--rau
    BEGIN
		PRINT 'start dirty 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish dirty 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
    END

	--bun
	BEGIN
		PRINT 'start dirty 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish dirty 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
    END


END




SELECT * FROM Meciuri
UPDATE Meciuri SET echipa_1 = 'CFR' WHERE echipa_2 = 'FCSB'
SELECT * FROM Meciuri

--non-repeatable reads
BEGIN 
    
	-- rau
	BEGIN
		PRINT 'start non-repeatable reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL READ COMMITTED
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		WAITFOR DELAY '00:00:07';
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish non-repeatable reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	-- bun
	BEGIN
		PRINT 'start non-repeatable reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		WAITFOR DELAY '00:00:07';
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish non-repeatable reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	BEGIN
		PRINT 'start non-repeatable reads 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		BEGIN TRAN;
		WAITFOR DELAY '00:00:01';
		UPDATE Meciuri SET echipa_1 = 'Dinamo' WHERE echipa_2 = 'FCSB'
		COMMIT TRAN;		PRINT 'finish non-repeatable reads 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)	END
END


SELECT * FROM Meciuri
DELETE FROM Meciuri WHERE echipa_1 = 'Rapid'
SELECT * FROM Meciuri
--phantom reads
BEGIN 
    
	-- rau
	BEGIN
		PRINT 'start phantom reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		WAITFOR DELAY '00:00:07';
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish phantom reads reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	-- bun
	BEGIN
		PRINT 'start phantom reads reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
		BEGIN TRAN;
		SELECT * FROM Meciuri;
		WAITFOR DELAY '00:00:07';
		SELECT * FROM Meciuri;
		COMMIT TRAN;
		PRINT 'finish phantom reads reads 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	BEGIN
		PRINT 'start phantom reads reads 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		BEGIN TRAN;
		WAITFOR DELAY '00:00:01';
		INSERT INTO Meciuri(echipa_1, echipa_2) VALUES ('Rapid', 'Incet')
		COMMIT TRAN;		PRINT 'finish phantom reads reads 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)	END
END

SELECT * FROM Meciuri
SELECT * FROM Fani
UPDATE FANI SET nume = 'Alex' WHERE nume = 'Andrei';
UPDATE Meciuri SET echipa_1='CFR' WHERE echipa_2 = 'FCSB';
SELECT * FROM Meciuri
SELECT * FROM Fani
--deadlock
BEGIN
	BEGIN
		PRINT 'start deadlock 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
		BEGIN TRAN;
		UPDATE Meciuri SET echipa_1='Dinamo' WHERE echipa_2 = 'FCSB';
		WAITFOR DELAY '00:00:05';
		UPDATE FANI SET nume = 'Andrei' WHERE nume = 'Alex';
		COMMIT TRAN;
		PRINT 'finish deadlock 1'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	--rau
	BEGIN
		PRINT 'start deadlock 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
		BEGIN TRAN;
		UPDATE FANI SET nume = 'Ion' WHERE nume = 'Alex';
		WAITFOR DELAY '00:00:05';
		UPDATE Meciuri SET echipa_1='Craiova' WHERE echipa_2 = 'FCSB';
		COMMIT TRAN;
		PRINT 'finish deadlock 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END

	--bun
	BEGIN
		PRINT 'start deadlock 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
		BEGIN TRAN;
		UPDATE Meciuri SET echipa_1='Craiova' WHERE echipa_2 = 'FCSB';
		WAITFOR DELAY '00:00:05';
		UPDATE FANI SET nume = 'Ion' WHERE nume = 'Alex';
		COMMIT TRAN;
		PRINT 'finish deadlock 2'
		PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END
END


INSERT INTO Fani(nume) VALUES('Alex')
SELECT * FROM Meciuri
SELECT * FROM Fani
UPDATE FANI SET nume = 'Alex' WHERE nume = 'Andrei' or nume = 'Ion';
UPDATE Meciuri SET echipa_1='CFR' WHERE echipa_2 = 'FCSB';
SELECT * FROM Meciuri
SELECT * FROM Fani

GO
CREATE OR ALTER PROCEDURE th1
AS
	BEGIN
		--PRINT 'start deadlock 1'
		--PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
		BEGIN TRAN;
		UPDATE Meciuri SET echipa_1='Dinamo' WHERE echipa_2 = 'FCSB';
		WAITFOR DELAY '00:00:05';
		UPDATE FANI SET nume = 'Andrei' WHERE nume = 'Alex';
		COMMIT TRAN;
		--PRINT 'finish deadlock 1'
		--PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END
GO

CREATE OR ALTER PROCEDURE th2
AS
	BEGIN
		--PRINT 'start deadlock 2'
		--PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
		SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
		BEGIN TRAN;
		UPDATE FANI SET nume = 'Ion' WHERE nume = 'Alex';
		WAITFOR DELAY '00:00:05';
		UPDATE Meciuri SET echipa_1='Craiova' WHERE echipa_2 = 'FCSB';
		COMMIT TRAN;
		--PRINT 'finish deadlock 2'
		--PRINT CONVERT(TIME, CURRENT_TIMESTAMP)
	END