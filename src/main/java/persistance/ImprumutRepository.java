package persistance;

import model.Imprumut;
import model.UtilizatorSistem;

public interface ImprumutRepository extends IRepository<Long, Imprumut> {
    Iterable<Imprumut> findByUser(UtilizatorSistem mainUser);
    Iterable<Imprumut> findByUserAndBook(Long userId, Long bookId);
}
