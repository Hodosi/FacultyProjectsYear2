USE NationalFootballLeagues;

/*
nr_where = 0
nr_group_by = 0
nr_distinct = 0
nr_having = 0
nr_more_than_2_tables = 0
nr_m-n = 0
*/

--nr_more_than_2_tables = 1, nr_where = 1 
--extract all players and coach for a team
SELECT T.name_team, C.last_name_coach, C.first_name_coach, P.last_name_player FROM Teams T 
INNER JOIN Coaches C ON T.id_coach=C.id_coach
INNER JOIN Players P ON T.id_team=P.id_team
WHERE T.name_team='AC Milan';

--nr_more_than_2_tables = 2, nr_where = 2, nr_distinct = 1 
--extract all players with goals for two teams
SELECT DISTINCT T.name_team, P.last_name_player FROM Teams T
INNER JOIN Players P ON T.id_team = P.id_team
INNER JOIN Goals G On G.id_player = P.id_player
WHERE T.name_team='AC Milan' OR T.name_team='Inter'; 

--nr_more_than_2_tables = 3, nr_group_by = 1, nr_having = 1
--extract all players with more than one goal 
SELECT T.name_team, P.last_name_player, COUNT(P.id_player) AS [nr_goals] FROM Teams T
INNER JOIN Players P ON T.id_team = P.id_team
INNER JOIN Goals G On G.id_player = P.id_player
GROUP BY T.name_team, P.last_name_player
HAVING COUNT(P.id_player) >= 2;

--nr_more_than_2_tables = 4, nr_where = 3
--extract all teams and league name for a country
SELECT L.name_league, L.country_league, T.name_team FROM Leagues L
INNER JOIN Rankings R ON L.id_league=R.id_league
INNER JOIN Teams T On R.id_ranking=T.id_ranking
WHERE L.country_league='ROMANIA';

--nr_more_than_2_tables = 5, nr_distinct = 2
--extract all teams with goals
SELECT DISTINCT L.name_league, L.country_league, T.name_team FROM Leagues L
INNER JOIN Rankings R ON L.id_league=R.id_league
INNER JOIN Teams T ON R.id_ranking=T.id_ranking
INNER JOIN Players P ON T.id_team=P.id_team
INNER JOIN Goals G ON P.id_player=G.id_player;

--nr_more_than_2_tables = 6, nr_where = 4
--extract all fixed games
SELECT TH.name_team , TG.name_team FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
WHERE CONVERT(date, GM.date_time)>GETDATE();

--nr_group_by = 2, nr_having = 2
--extract teams with more than 2 games played
SELECT T.name_team, COUNT(T.id_team) AS [nr_played_games] FROM Games GM
INNER JOIN Teams T ON T.id_team=GM.id_team_host or T.id_team=GM.id_team_guest
WHERE CONVERT(date, GM.date_time)<GETDATE()
GROUP BY T.name_team
HAVING COUNT(T.id_team)>=2;

--nr_more_than_2_tables = 7, nr_where = 5, nr_group_by = 3
--extract all game results with games in which both of the teams has scored
SELECT TH.name_team , TG.name_team,
COUNT(GH.id_goal) AS [goals_host], COUNT(DISTINCT GG.id_goal) AS [goals_guest]
FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
INNER JOIN Players PH ON PH.id_team = TH.id_team
INNER JOIN Players PG ON PG.id_team = TG.id_team
INNER JOIN Goals GH ON GH.id_game = GM.id_game
INNER JOIN Goals GG ON GG.id_game = GM.id_game
WHERE CONVERT(date, Gm.date_time)<GETDATE()
and (GH.id_player = PH.id_player 
and GG.id_player = PG.id_player)
GROUP BY TH.name_team , TG.name_team;


-- nr_more_than_2_tables = 8, nr_where = 6, nr_m-n = 1
-- extract all game for a referee
SELECT TH.name_team , TG.name_team, R.last_name_referee FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
INNER JOIN RefereesGames RG ON RG.id_game=GM.id_game
INNER JOIN Referees R ON RG.id_referee=R.id_referee
WHERE R.last_name_referee = 'Sozza';


-- nr_more_than_2_tables = 9, nr_m-n = 2, nr_group_by = 4 
-- extract number of  referee for all games
SELECT TH.name_team , TG.name_team, COUNT(R.last_name_referee) AS [nr_referees]
FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
INNER JOIN RefereesGames RG ON RG.id_game=GM.id_game
INNER JOIN Referees R ON RG.id_referee=R.id_referee
GROUP BY TH.name_team, TG.name_team;


--extract number of goals for all played games
SELECT TH.name_team , TG.name_team,
COUNT(P.id_player)
FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
LEFT JOIN Goals G ON G.id_game = GM.id_game
LEFT JOIN Players P ON G.id_player = P.id_player
WHERE CONVERT(date, GM.date_time)<GETDATE()
GROUP BY TH.name_team , TG.name_team;

--extract all players and coach for all games
SELECT T.id_team, T.name_team, C.last_name_coach, C.first_name_coach, P.last_name_player FROM Teams T
INNER JOIN Coaches C ON T.id_coach=C.id_coach INNER JOIN Players P ON T.id_team=P.id_team;

------------------------------------------------------------------------------------------------

SELECT T.name_team, P.last_name_player FROM Teams T
INNER JOIN Players P ON T.id_team = P.id_team
INNER JOIN Goals G On G.id_player = P.id_player


SELECT TH.name_team , TG.name_team,
COUNT(GH.id_goal) AS [goals_host], COUNT(DISTINCT GG.id_goal) AS [goals_guest]
FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
INNER JOIN Players PH ON PH.id_team = TH.id_team
INNER JOIN Players PG ON PG.id_team = TG.id_team
INNER JOIN Goals GH ON GH.id_game = GM.id_game
INNER JOIN Goals GG ON GG.id_game = GM.id_game
WHERE CONVERT(date, Gm.date_time)<GETDATE()
and (GH.id_player = PH.id_player 
and GG.id_player = PG.id_player)
GROUP BY TH.name_team , TG.name_team;


SELECT TH.name_team , TG.name_team,
COUNT(GH.id_goal) AS [HHH], COUNT(DISTINCT GG.id_goal) AS [GGG]
FROM Games GM
INNER JOIN Teams TH ON TH.id_team=GM.id_team_host
INNER JOIN Teams TG ON TG.id_team=GM.id_team_guest
INNER JOIN Players PH ON PH.id_team = TH.id_team
INNER JOIN Players PG ON PG.id_team = TG.id_team
right JOIN Goals GH ON PH.id_player = GH.id_player 
right JOIN Goals GG ON PG.id_player = GG.id_player 
WHERE CONVERT(date, Gm.date_time)<GETDATE()
and GH.id_game = GM.id_game 
and GG.id_game = GM.id_game
GROUP BY TH.name_team , TG.name_team;
