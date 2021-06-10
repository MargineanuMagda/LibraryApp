package persistance;

import model.UtilizatorSistem;

public interface UtilizatorRepository extends IRepository<Long, UtilizatorSistem> {
    /**
     * @param username-String
     * @param password-String
     * @return
     */
    UtilizatorSistem findByUsernameAndPassword(String username, String password);
}
