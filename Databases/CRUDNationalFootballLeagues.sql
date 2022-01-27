USE NationalFootballLeagues
GO

create or alter function validateGame(@id_league int, @id_team_host int, @id_team_guest int, @date_time datetime)
returns int
as
begin
	if not exists(select * from Leagues where id_league = @id_league)
		return 0;
    if not exists(select * from Teams where id_team = @id_team_host)
		return 0;
	if not exists(select * from Teams where id_team = @id_team_guest)
		return 0;
    if @id_team_host = @id_team_guest
		return 0
	return 1;
end
go

create or alter procedure createGame(@id_league int, @id_team_host int, @id_team_guest int, @date_time datetime)
as
begin
	if dbo.validateGame(@id_league, @id_team_host, @id_team_guest, @date_time) = 1
	    if not exists(select * from Games where id_league=@id_league and id_team_host=@id_team_host and id_team_guest=@id_team_guest and date_time=@date_time)
			begin
			insert into Games(id_league, id_team_host, id_team_guest, date_time) values(@id_league, @id_team_host, @id_team_guest, @date_time);
			end
		else
			print 'game already exists';
	else
		print 'invalid input';
end
go

create or alter procedure readGame(@id_league int, @id_team_host int, @id_team_guest int)
as
begin
	select * from Games where id_league=@id_league and id_team_host=@id_team_host and id_team_guest=@id_team_guest;
end
go

create or alter procedure updateGame(@id_game int, @id_league int, @id_team_host int, @id_team_guest int,  @date_time datetime)
as
begin
	if dbo.validateGame(@id_league, @id_team_host, @id_team_guest, @date_time) = 1
		if exists(select * from Games where id_game = @id_game)
			update Games set id_league=@id_league, id_team_host=@id_team_host, id_team_guest=@id_team_guest, date_time=@date_time
			where id_game = @id_game;
		else
			print 'game doesnt exist';
	else
		print 'invalid input'
end
go

create or alter procedure deleteGame(@id_game int)
as
begin
	delete from Games where id_game=@id_game;
end
go

create or alter function validateReferee(@last_name_referee varchar(64), @first_name_referee varchar(64))
returns int
as
begin
    if @last_name_referee = '' or @first_name_referee = ''
		return 0
	return 1
end
go

create or alter procedure createReferee(@last_name_referee varchar(64), @first_name_referee varchar(64))
as
begin
	if dbo.validateReferee(@last_name_referee, @first_name_referee) = 1
	    if not exists(select * from Referees where last_name_referee=@last_name_referee and first_name_referee=@first_name_referee)
			begin
			insert into Referees(last_name_referee, first_name_referee) values(@last_name_referee, @first_name_referee);
			end
		else
			print 'referee already exists';
	else
		print 'invalid input';
end
go

create or alter procedure readReferee(@last_name_referee varchar(64), @first_name_referee varchar(64))
as
begin
	select * from Referees where last_name_referee=@last_name_referee and first_name_referee=@first_name_referee;
end
go

create or alter procedure updateReferee(@id_referee int, @last_name_referee varchar(64), @first_name_referee varchar(64))
as
begin
	if dbo.validateReferee(@last_name_referee, @first_name_referee) = 1
		if exists(select * from Referees where  id_referee = @id_referee)
			update Referees set last_name_referee=@last_name_referee, first_name_referee=@first_name_referee
			where id_referee = @id_referee;
		else
			print 'referee doesnt exist';
	else
		print 'invalid input'
end
go

create or alter procedure deleteReferee(@id_referee int)
as
begin
	delete from Referees where id_referee=@id_referee;
end
go

create or alter procedure createRefereeGame(@id_referee int, @id_game int)
as
begin
	    if not exists(select * from RefereesGames where id_referee=@id_referee and id_game=@id_game)
			begin
			insert into RefereesGames(id_referee, id_game) values(@id_referee, @id_game);
			end
		else
			print 'referee game already exists';
end
go

create or alter procedure readRefereeGame(@id_referee int, @id_game int)
as
begin
	select * from RefereesGames where id_referee=@id_referee and id_game=@id_game;
end
go


create or alter procedure deleteRefereeGame(@id_referee int, @id_game int)
as
begin
	delete from RefereesGames where id_referee=@id_referee and id_game=@id_game;
end
go

--view pe doua tabele care extrage doar arbitrii care au cel putin un meci
CREATE VIEW vw1
AS
SELECT DISTINCT R.id_referee, R.last_name_referee FROM Referees R INNER JOIN RefereesGames RG
ON R.id_referee=RG.id_referee;
GO

--view pe doua tabele care numara cate meciuri are fiecare arbitru
CREATE VIEW vw2
AS
SELECT R.id_referee, R.last_name_referee, COUNT(RG.id_referee) AS nr_games FROM Referees R LEFT JOIN RefereesGames RG
ON R.id_referee=RG.id_referee GROUP BY R.id_referee, R.last_name_referee;
GO

create nonclustered index nonidx_id_referee on Referees(id_referee);
go

create nonclustered index nonidx_last_name_referee on Referees(last_name_referee);
go


select * from Games;
select * from Referees;
select * from RefereesGames;
go

print dbo.validateGame(11, 1, 2, '2021-11-08 21:45:00:00');
print dbo.validateGame(1, 11, 1, '2021-11-08 21:45:00:00');
print dbo.validateGame(1, 1, 11, '2021-11-08 21:45:00:00');
print dbo.validateGame(1, 1, 1, '2021-11-08 21:45:00:00');
print dbo.validateGame(1, 4, 6, '2021-11-08 21:45:00:00');
go

select * from Games;
EXEC createGame @id_league=11, @id_team_host=1, @id_team_guest=2, @date_time='2021-11-08 21:45:00:00';
EXEC createGame @id_league=1, @id_team_host=11, @id_team_guest=1, @date_time='2021-11-08 21:45:00:00';
EXEC createGame @id_league=1, @id_team_host=1, @id_team_guest=11, @date_time='2021-11-08 21:45:00:00';
EXEC createGame @id_league=1, @id_team_host=1, @id_team_guest=1, @date_time='2021-11-08 21:45:00:00';
EXEC createGame @id_league=1, @id_team_host=4, @id_team_guest=6, @date_time='2021-11-08 21:45:00:00';
EXEC createGame @id_league=1, @id_team_host=4, @id_team_guest=6, @date_time='2021-11-08 21:45:00:00';

EXEC readGame @id_league=1, @id_team_host=4, @id_team_guest=6;

select * from Games;
EXEC updateGame @id_game=11, @id_league=23, @id_team_host=6, @id_team_guest=4, @date_time='2021-07-08 21:45:00:00';
EXEC updateGame @id_game=13, @id_league=2, @id_team_host=6, @id_team_guest=4, @date_time='2021-07-08 21:45:00:00';
select * from Games;

EXEC deleteGame @id_game=13
select * from Games;

print dbo.validateReferee('', '')
print dbo.validateReferee('a', '')
print dbo.validateReferee('', 'b')
print dbo.validateReferee('a', 'b')
go

EXEC createReferee @last_name_referee='', @first_name_referee='';
EXEC createReferee @last_name_referee='a', @first_name_referee='';
EXEC createReferee @last_name_referee='', @first_name_referee='b';
EXEC createReferee @last_name_referee='a', @first_name_referee='b';
EXEC createReferee @last_name_referee='a', @first_name_referee='b';

EXEC readReferee @last_name_referee='a', @first_name_referee='b';

select * from Referees;
EXEC updateReferee @id_referee=11, @last_name_referee='', @first_name_referee='';
EXEC updateReferee @id_referee=12, @last_name_referee='b', @first_name_referee='a';
select * from Referees;

EXEC deleteReferee @id_referee=12;
select * from Referees;

select * from RefereesGames;

EXEC createRefereeGame @id_referee=1, @id_game=8;
EXEC createRefereeGame @id_referee=1, @id_game=8;

EXEC readRefereeGame @id_referee=1, @id_game=8;

select * from RefereesGames;
EXEC deleteRefereeGame @id_referee=1, @id_game=8;
select * from RefereesGames;

select * from vw1;
select * from vw2;

select id_referee from vw1;
select last_name_referee from vw2;