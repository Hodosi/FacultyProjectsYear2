package skeleton.services;

public interface ISkeletonObserver {
    void startGame() throws SkeletonException;
    void newMove() throws SkeletonException;
    void finishGame() throws SkeletonException;
}
