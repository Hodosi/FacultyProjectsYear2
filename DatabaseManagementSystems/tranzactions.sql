USE NationalFootballLeagues
GO

CREATE TABLE Meciuri (
	id_meci INT PRIMARY KEY IDENTITY,
	echipa_1 VARCHAR(64),
	echipa_2 VARCHAR(64)
);

CREATE TABLE Fani (
	id_fan INT PRIMARY KEY IDENTITY,
	nume VARCHAR(64)
);

CREATE TABLE Pariu (
	alegere VARCHAR(64),
	id_meci INT FOREIGN KEY REFERENCES Meciuri(id_meci),
	id_fan INT FOREIGN KEY REFERENCES Fani(id_fan),
	CONSTRAINT pk_Pariu PRIMARY KEY (id_meci, id_fan),
);
GO

CREATE or ALTER FUNCTION valideazaMeci(@echipa_1 varchar(64), @echipa_2 varchar(64))
returns int
as
begin
    if @echipa_1 = '' or @echipa_2 = ''
		return 0
	return 1
end
go

CREATE or ALTER FUNCTION valideazaFan(@nume varchar(64))
returns int
as
begin
    if @nume = ''
		return 0
	return 1
end
go

CREATE or ALTER FUNCTION valideazaPariu(@alegere varchar(64))
returns int
as
begin
    if @alegere = '1' or @alegere = 'x' or @alegere = '2' 
		return 1
	return 0
end
go


CREATE OR ALTER PROCEDURE adaugaPariu1(@nume varchar(64), @echipa_1 varchar(64), @echipa_2 varchar(64), @alegere varchar(64))
AS 
BEGIN

	BEGIN TRAN
		BEGIN TRY
			IF(dbo.valideazaFan(@nume) = 0) 
				BEGIN 
			        PRINT 'fan invalid'
					RAISERROR('Fan invalid!',11,1)
				END
			IF(dbo.valideazaMeci(@echipa_1, @echipa_2) = 0)
				BEGIN 
					PRINT 'meci invalid'
					RAISERROR('Meci invalid!',11,1);
				END
			IF(dbo.valideazaPariu(@alegere) = 0)
				BEGIN
					PRINT 'pariu invalid'
					RAISERROR('Pariu invalid',11,1);
				END

			INSERT INTO Fani(nume) VALUES(@nume);
			DECLARE @id_fan INT = SCOPE_IDENTITY();
			INSERT INTO Meciuri(echipa_1, echipa_2) VALUES (@echipa_1, @echipa_2);
			DECLARE @id_meci INT = SCOPE_IDENTITY();
			INSERT INTO Pariu(alegere, id_meci, id_fan) VALUES(@alegere, @id_meci, @id_fan);

			COMMIT TRAN
			PRINT 'adaugaPariu1 commit'
		END TRY
		BEGIN CATCH
			ROLLBACK TRAN
			PRINT 'adaugaPariu1 rollback'
		END CATCH

END
GO

SELECT * FROM Fani;
SELECT * FROM Meciuri;
SELECT * FROM Pariu;
GO
/*EXEC adaugaPariu1 @nume = 'Alex', @echipa_1 = 'CFR', @echipa_2 = 'FCSB', @alegere = '1';*/
EXEC adaugaPariu1 @nume = '', @echipa_1 = 'CFR', @echipa_2 = 'FCSB', @alegere = '1'
EXEC adaugaPariu1 @nume = 'Alex', @echipa_1 = '', @echipa_2 = 'FCSB', @alegere = '1'
EXEC adaugaPariu1 @nume = 'Alex', @echipa_1 = 'CFR', @echipa_2 = '', @alegere = '1'
EXEC adaugaPariu1 @nume = 'Alex', @echipa_1 = 'CFR', @echipa_2 = 'FCSB', @alegere = '3'
EXEC adaugaPariu1 @nume = 'Alex', @echipa_1 = 'CFR', @echipa_2 = 'FCSB', @alegere = '1'
GO


CREATE OR ALTER PROCEDURE adaugaPariu2(@nume varchar(64), @echipa_1 varchar(64), @echipa_2 varchar(64), @alegere varchar(64))
AS 
BEGIN

	BEGIN TRAN
		BEGIN TRY

			IF(dbo.valideazaFan(@nume) = 0) 
				BEGIN 
			        PRINT 'fan invalid'
					RAISERROR('Fan invalid!',11,1)
				END
			INSERT INTO Fani(nume) VALUES(@nume);
			DECLARE @id_fan INT = SCOPE_IDENTITY();
			COMMIT TRAN
			PRINT 'adaugaPariu2 commit fan'
			-------------------------------------------------------------------------
			BEGIN TRAN
				BEGIN TRY
					IF(dbo.valideazaMeci(@echipa_1, @echipa_2) = 0)
						BEGIN 
							PRINT 'meci invalid'
							RAISERROR('Meci invalid!',11,1);
						END		
					INSERT INTO Meciuri(echipa_1, echipa_2) VALUES (@echipa_1, @echipa_2);
					DECLARE @id_meci INT = SCOPE_IDENTITY();
					COMMIT TRAN
					PRINT 'adaugaPariu2 commit meci'
					-------------------------------------------------------------------------
					BEGIN TRAN
						BEGIN TRY
							IF(dbo.valideazaPariu(@alegere) = 0)
								BEGIN
									PRINT 'pariu invalid'
									RAISERROR('Pariu invalid',11,1);
								END
							INSERT INTO Pariu(alegere, id_meci, id_fan) VALUES(@alegere, @id_meci, @id_fan);

							COMMIT TRAN
							PRINT 'adaugaPariu2 commit pariu'
                        END TRY
						BEGIN CATCH
							ROLLBACK TRAN
							PRINT 'adaugaPariu2 rollback PARIU'
						END CATCH
					
				    -------------------------------------------------------------------------

				END TRY
				BEGIN CATCH
					ROLLBACK TRAN
					PRINT 'adaugaPariu2 rollback meci'
				END CATCH

			------------------------------------------------------------------------


		END TRY
		BEGIN CATCH
			ROLLBACK TRAN
			PRINT 'adaugaPariu2 rollback fan'
		END CATCH

END
GO


SELECT * FROM Fani;
SELECT * FROM Meciuri;
SELECT * FROM Pariu;
GO
/*EXEC adaugaPariu1 @nume = 'Andrei', @echipa_1 = 'Dinamo', @echipa_2 = 'Rapid', @alegere = 'x';*/
EXEC adaugaPariu2 @nume = '', @echipa_1 = 'Dinamo', @echipa_2 = 'Rapid', @alegere = 'x'
EXEC adaugaPariu2 @nume = 'Andrei', @echipa_1 = '', @echipa_2 = 'Rapid', @alegere = 'x'
EXEC adaugaPariu2 @nume = 'Cristi', @echipa_1 = 'Dinamo', @echipa_2 = '', @alegere = 'x'
EXEC adaugaPariu2 @nume = 'Adi', @echipa_1 = 'Dinamo', @echipa_2 = 'Rapid', @alegere = '3'
EXEC adaugaPariu2 @nume = 'Bob', @echipa_1 = 'Dinamo', @echipa_2 = 'Incet', @alegere = 'x'
GO

DELETE FROM Pariu;
DELETE FROM Meciuri;
DELETE FROM Fani;