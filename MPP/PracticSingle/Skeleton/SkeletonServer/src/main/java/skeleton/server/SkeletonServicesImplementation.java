package skeleton.server;

import skeleton.model.*;
import skeleton.persistence.*;
import skeleton.services.ISkeletonObserver;
import skeleton.services.ISkeletonServices;
import skeleton.services.SkeletonException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class SkeletonServicesImplementation implements ISkeletonServices {
    private IUserRepository userRepository;
    private IGameConfigRepository gameConfigRepository;
    private IGameDataRepository gameDataRepository;
    private IGameFinishRepository gameFinishRepository;
    private IMoveRepository moveRepository;
    private IHistoryRepository historyRepository;

    private Map<String, ISkeletonObserver> loggedClients;
    private Map<String, ISkeletonObserver> usersInGame;
    private Map<String, GameConfig> usersGameConfig;
    private Map<String, String> userGameInfo;


    public SkeletonServicesImplementation(IUserRepository userRepository, IGameConfigRepository gameConfigRepository, IGameDataRepository gameDataRepository, IGameFinishRepository gameFinishRepository, IMoveRepository moveRepository, IHistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.gameConfigRepository = gameConfigRepository;
        this.gameDataRepository = gameDataRepository;
        this.gameFinishRepository = gameFinishRepository;
        this.moveRepository = moveRepository;
        this.historyRepository = historyRepository;
        loggedClients = new ConcurrentHashMap<>();
        usersInGame = new ConcurrentHashMap<>();
        usersGameConfig = new ConcurrentHashMap<>();
        userGameInfo = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, ISkeletonObserver client) throws SkeletonException {
        System.out.println("IMPLEMENTATION - LOGIN");
        User crtUser = userRepository.findByUsername(user.getUsername());
        if(crtUser != null) {
            if(loggedClients.get(crtUser.getUsername()) != null){
                throw new SkeletonException("User already logged in.");
            }
            if(!crtUser.getPassword().equals(user.getPassword())){
                throw new SkeletonException("Wrong password.");
            }
            loggedClients.put(crtUser.getUsername(), client);
            System.out.println("Synch login");
        }
        else {
            throw new SkeletonException("Authentication failed.");
        }
    }

    @Override
    public synchronized void logout(User user, ISkeletonObserver client) throws SkeletonException {
        System.out.println("IMPLEMENTATION - LOGOUT");
        ISkeletonObserver localClient=loggedClients.remove(user.getUsername());
        if (localClient==null)
            throw new SkeletonException("User "+user.getId()+" is not logged in.");
    }

    @Override
    public synchronized User findUserByUsername(String username) throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND USER BY USERNAME");
        System.out.println("Find by username : " + username);
        User user = userRepository.findByUsername(username);
        return user;
    }


    @Override
    public synchronized void start(String username, String startGameData) throws SkeletonException {
        //save start game data into database

        String alias = startGameData;
        if(userRepository.findByUsername(alias) == null){
            return;
        }
        int gameId = historyRepository.findMaxGameIdPlayer(username);
        if(gameId < 0){
            gameId = 0;
        }
        gameId++;

        List<GameConfig> gameConfigList = (List<GameConfig>) gameConfigRepository.findAll();

        int randomIndex = ThreadLocalRandom.current().nextInt(0, gameConfigList.size());

        GameConfig gameConfig = gameConfigList.get(randomIndex);


        GameData gameData = new GameData(username, gameConfig.getPozitie());
        gameData.setIdGame(gameId);

        Move move = new Move(username, "-", 0);
        History history = new History(gameId, 0, username, "-", 0);

        gameDataRepository.save(gameData);
        moveRepository.deleteForUser(username);
        moveRepository.save(move);
        historyRepository.save(history);

        usersGameConfig.put(username, gameConfig);
        usersInGame.put(username, loggedClients.get(username));

        notifyStartGame();
        notifyNewMove(username);
    }

    @Override
    public synchronized void move(String username, String gameData) throws SkeletonException {
        String propunere = gameData;
        int gameId = historyRepository.findMaxGameIdPlayer(username);
        int rundaId = historyRepository.findIdRundaGamePlayer(gameId, username);
        rundaId++;

//        String currentState = proceseaza or
        String currentState = gameData;

        String info = String.valueOf(rundaId);
//        String info = "";
        if(userGameInfo.get(username) != null){
            info = userGameInfo.get(username);
            userGameInfo.remove(username);
        }
        //prelucreaza info
        userGameInfo.put(username, info);

//        int punctaj = calculeaza or
        int punctaj = rundaId;


        Move move = new Move(username, currentState, punctaj);
        History history = new History(gameId, rundaId, username, propunere, punctaj);

        moveRepository.save(move);
        historyRepository.save(history);

        notifyNewMove(username);
        if(rundaId == 3){
            finishGame(username, gameId);
            notifyFinishGame();
        }
    }

    private synchronized void finishGame(String username, int idGame){
        List<Move> moves = (List<Move>) moveRepository.findAllPlayer(username);
        int punctajTotal = 0;

        for(Move move : moves){
            punctajTotal += move.getPunctaj();
        }

        GameFinish gameFinish = new GameFinish(username, idGame, punctajTotal);

        String info = userGameInfo.get(username);
        userGameInfo.remove(username);

        gameFinish.setInfo(info);
        gameFinishRepository.save(gameFinish);
    }

    @Override
    public synchronized GameData[] findAllGameData() throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND ALL GAME DATA");

        List<GameData> gameDataList = (List<GameData>) gameDataRepository.findAll();

        if(gameDataList.size() == 0){
            return new GameData[0];
        }

        return gameDataList.toArray(new GameData[gameDataList.size()]);
    }

    @Override
    public synchronized Move[] findCurrentMoves() throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND CURRENT MOVES");

        List<Move> moves = (List<Move>) moveRepository.findAll();

        if(moves.size() == 0){
            return new Move[0];
        }

        return moves.toArray(new Move[moves.size()]);
    }

    @Override
    public Move[] findAllMovesPlayer(String username) throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND CURRENT MOVES PLAYER");

        List<Move> moves = (List<Move>) moveRepository.findAllPlayer(username);

        if(moves.size() == 0){
            return new Move[0];
        }

        return moves.toArray(new Move[moves.size()]);
    }


    @Override
    public synchronized Clasament[] findClasament() throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND CLASAMENT");

//        List<Clasament> clasamentList= (List<Clasament>) historyRepository.findClasament(gameId);
//        List<Clasament> clasamentList= (List<Clasament>) historyRepository.findClasament();
        List<Clasament> clasamentList= (List<Clasament>) gameFinishRepository.findClasament();

        if(clasamentList.size() == 0){
            return new Clasament[0];
        }

        return clasamentList.toArray(new Clasament[clasamentList.size()]);
    }

    private synchronized void notifyStartGame() throws SkeletonException {
        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
            entry.getValue().startGame();
        }
    }

//    private synchronized void notifyNewMove() throws SkeletonException {
//        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
//        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
//           entry.getValue().newMove();
//        }
//    }

    private synchronized void notifyNewMove(String username) throws SkeletonException {
        usersInGame.get(username).newMove();
    }

    private synchronized void notifyFinishGame() throws SkeletonException {
        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
            entry.getValue().finishGame();
        }
    }
}
