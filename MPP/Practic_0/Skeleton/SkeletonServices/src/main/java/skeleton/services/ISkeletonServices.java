package skeleton.services;

import skeleton.model.Move;
import skeleton.model.User;

public interface ISkeletonServices {
    void login(User user, ISkeletonObserver client) throws SkeletonException;
    void logout(User user, ISkeletonObserver client) throws SkeletonException;
    User findUserByUsername(String username) throws SkeletonException;
    void startGame(User user, String startGameData) throws SkeletonException;
    Move[] findCurrentMoves() throws SkeletonException;
}
