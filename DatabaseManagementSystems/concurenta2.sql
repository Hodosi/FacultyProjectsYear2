USE NationalFootballLeagues
GO

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
