package skeleton.model;

public class StartGameDTO extends Entity<Integer> {
    private final User user;
    private final String startGameData;

    public StartGameDTO(User user, String startGameData) {
        this.user = user;
        this.startGameData = startGameData;
    }

    public User getUser() {
        return user;
    }

    public String getStartGameData() {
        return startGameData;
    }
}
