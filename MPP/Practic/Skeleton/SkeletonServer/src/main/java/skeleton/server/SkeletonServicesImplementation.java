package skeleton.server;

import skeleton.model.*;
import skeleton.persistence.IGameDataRepository;
import skeleton.persistence.IHistoryRepository;
import skeleton.persistence.IMoveRepository;
import skeleton.persistence.IUserRepository;
import skeleton.services.ISkeletonObserver;
import skeleton.services.ISkeletonServices;
import skeleton.services.SkeletonException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkeletonServicesImplementation implements ISkeletonServices {
    private IUserRepository userRepository;
    private IGameDataRepository gameDataRepository;
    private IMoveRepository moveRepository;
    private IHistoryRepository historyRepository;

    private Map<String, ISkeletonObserver> loggedClients;
    private Map<String, ISkeletonObserver> usersInGame;

//    public SkeletonServicesImplementation(IUserRepository userRepository){
//        this.userRepository = userRepository;
//        loggedClients = new ConcurrentHashMap<>();
//        usersInGame = new ConcurrentHashMap<>();
//    }


    public SkeletonServicesImplementation(IUserRepository userRepository, IGameDataRepository gameDataRepository, IMoveRepository moveRepository, IHistoryRepository historyRepository) {
        this.userRepository = userRepository;
        this.gameDataRepository = gameDataRepository;
        this.moveRepository = moveRepository;
        this.historyRepository = historyRepository;
        loggedClients = new ConcurrentHashMap<>();
        usersInGame = new ConcurrentHashMap<>();
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
            //notifyUsersLoggedIn(crtUser);
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
//        notifyUsersLoggedOut(user);
    }

    @Override
    public synchronized User findUserByUsername(String username) throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND USER BY USERNAME");
        System.out.println("Find by username : " + username);
        User user = userRepository.findByUsername(username);
        return user;
    }


    private int gameId = -1;
    private int rundaId = -1;
    private int nrPlayers = 2;
    private int nrMoves = 0;
    private boolean gameStarted = false;

    @Override
    public synchronized void start(String username, String startGameData) throws SkeletonException {
        //save start game data into database
        if(gameId == -1){
            gameId = historyRepository.findMaxGameId();
        }

        if(usersInGame.size() == 0){
            gameId++;
        }

        if(usersInGame.size() < nrPlayers){
            GameData gameData = new GameData(username, startGameData);
            gameData.setIdGame(gameId);
            gameDataRepository.save(gameData);
            usersInGame.put(username, loggedClients.get(username));
        }

        if(usersInGame.size() == nrPlayers && !gameStarted){
            gameStarted = true;
            rundaId = 1;
            notifyStartGame();
        }
    }

    @Override
    public synchronized void move(String username, String gameData) throws SkeletonException {
        Move move = moveRepository.findMoveByUsername(username);
        int puncte = move.getPunctaj();
        puncte+=10;
        move.setPunctaj(puncte);
        moveRepository.update(move.getId(), move);

        int idGame = gameId;
        int idRunda = rundaId;
        String idPlayer = username;
        String propunere = "nu zic";
        int punctaj = 10;
        History history = new History(idGame, idRunda, idPlayer, propunere, punctaj);
        historyRepository.save(history);

        nrMoves++;
        if(nrMoves == nrPlayers){
            nrMoves = 0;
            rundaId++;
            notifyNewMove();
            if(rundaId > 3){
                notifyFinishGame();
            }
        }
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
    public synchronized Clasament[] findClasament() throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND CLASAMENT");

        List<Clasament> clasamentList= (List<Clasament>) historyRepository.findClasament(gameId);

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

    private synchronized void notifyNewMove() throws SkeletonException {
        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
           entry.getValue().newMove();
        }
    }

    private synchronized void notifyFinishGame() throws SkeletonException {
        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
            entry.getValue().finishGame();
        }
    }
}
