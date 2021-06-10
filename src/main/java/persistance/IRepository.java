package persistance;

import model.Entitate;

public interface IRepository <ID,E extends Entitate<ID>>{

    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity);
    E delete(ID id);
    E update(E entity);
    int size();

}
