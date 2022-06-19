package skeleton.server;

import skeleton.model.Move;
import skeleton.model.User;
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
    private IMoveRepository moveRepository;
    private IHistoryRepository historyRepository;

    private Map<String, ISkeletonObserver> loggedClients;
    private Map<String, ISkeletonObserver> usersInGame;

//    public SkeletonServicesImplementation(IUserRepository userRepository){
//        this.userRepository = userRepository;
//        loggedClients = new ConcurrentHashMap<>();
//        usersInGame = new ConcurrentHashMap<>();
//    }


    public SkeletonServicesImplementation(IUserRepository userRepository, IMoveRepository moveRepository, IHistoryRepository historyRepository) {
        this.userRepository = userRepository;
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

    @Override
    public synchronized void startGame(User user, String startGameData) throws SkeletonException {
        //save start game data into database

        if(usersInGame.size() < 2){
            usersInGame.put(user.getUsername(), loggedClients.get(user.getUsername()));
        }

        if(usersInGame.size() == 2){
            play();
        }

    }

    @Override
    public synchronized Move[] findCurrentMoves() throws SkeletonException {
        System.out.println("IMPLEMENTATION - FIND ALL TEST DTOs");

        List<Move> moves = (List<Move>) moveRepository.findAll();

        if(moves.size() == 0){
            return new Move[0];
        }

        return moves.toArray(new Move[moves.size()]);
    }

    private synchronized void notifyNewMove() throws SkeletonException {
        System.out.println("IMPLEMENTATION - NOTIFY Start Game");
        for (Map.Entry<String, ISkeletonObserver> entry : usersInGame.entrySet()){
           entry.getValue().newMove();
        }
    }

    private synchronized void play() throws SkeletonException{
        System.out.println("-------------------------");
        System.out.println("PLAY PLAY PLAY");
        System.out.println("-------------------------");
        notifyNewMove();
    }
}
