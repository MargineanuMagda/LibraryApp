package persistance;

import model.Carte;

import java.util.List;

public interface CarteRepository extends IRepository<Long, Carte> {
    /**
     * @param nume
     * @param autor
     * @param codISBN
     * @return
     */
    List<Carte> findFiltered(String nume, String autor, Long codISBN);
}
