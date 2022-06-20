package skeleton.services;

import skeleton.model.Clasament;
import skeleton.model.GameData;
import skeleton.model.Move;
import skeleton.model.User;

public interface ISkeletonServices {
    void login(User user, ISkeletonObserver client) throws SkeletonException;
    void logout(User user, ISkeletonObserver client) throws SkeletonException;
    User findUserByUsername(String username) throws SkeletonException;
    void start(String username, String startGameData) throws SkeletonException;
    void move(String username, String gameData) throws SkeletonException;
    GameData[] findAllGameData() throws SkeletonException;
    Move[] findCurrentMoves() throws SkeletonException;
    Clasament[] findClasament() throws SkeletonException;
}
