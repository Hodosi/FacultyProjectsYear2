package skeleton.persistence.repository.jdbc;

import skeleton.model.User;
import skeleton.persistence.IUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class UserDbRepository implements IUserRepository {
    private final JdbcUtils dbUtils;

    public UserDbRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, User entity) {

    }

    @Override
    public User findOne(Integer id) {
        Connection connection = dbUtils.getConnection();
        String query = "SELECT * from users where id_user = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()) {
                    int idUser = resultSet.getInt("id_user");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(username, password);
                    user.setId(idUser);
                    return user;
                }
            }
        } catch (SQLException exception){
            System.err.println("Error DB " + exception);
        }

        return null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Connection connection = dbUtils.getConnection();
        String query = "SELECT * from users where username = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()) {
                    int idUser = resultSet.getInt("id_user");
                    String usernameUser = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(usernameUser, password);
                    user.setId(idUser);
                    return user;
                }
            }
        } catch (SQLException exception){
            System.err.println("Error DB " + exception);
        }

        return null;
    }
}
