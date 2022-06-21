package skeleton.persistence;

import skeleton.model.User;

public interface IUserRepository extends IRepository<Integer, User> {
    User findByUsername(String username);
}