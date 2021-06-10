package persistance;

import model.Carte;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CarteRepositoryImpl implements CarteRepository{



    private static final Logger logger= LogManager.getLogger();
    public static SessionFactory sessionFactory;


    /**
     * @param nume-numele cartii dupa care se face filtrarea
     * @param autor-autorul dupa care se filtreaza
     * @param codISBN-sau codul dupa care se filtreaza
     * @return
     */
    @Override
    public List<Carte> findFiltered(String nume, String autor, Long codISBN) {

        List<Carte> cartiEntities=new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx=session.beginTransaction();

                Query q = session.createQuery("SELECT carte FROM Carte carte WHERE carte.autor like :autor and carte.nume like :nume or  carte.ISBN = :cod");
                q.setParameter("nume", "%"+nume+"%");
                q.setParameter("autor", "%"+autor+"%");
                q.setParameter("cod", codISBN);
                cartiEntities = q.list();

            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return cartiEntities;
    }

    @Override
    public Carte findOne(Long id) {

        Carte findCarte = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                // Getting Transaction Object From Session Object
                tx=session.beginTransaction();

                findCarte = session.createQuery("from Carte where ISBN =:cod", Carte.class)
                        .setParameter("cod",id)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return findCarte;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<Carte> findAll() {
        List<Carte> cartiEntities=new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                cartiEntities = session.createQuery("select  c FROM Carte c",Carte.class).list();

                tx.commit();
               } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return cartiEntities;
    }


    @Override
    public Carte save(Carte entity) {

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Long id = (Long) session.save(entity);


                tx.commit();
                Long lastId = ((BigInteger) session.createSQLQuery("select MAX(ISBN) from carti").uniqueResult()).longValue();
                System.out.println("Cod isbn: "+lastId);
                entity.setISBN(lastId);
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return entity;
    }

    @Override
    public Carte delete(Long id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();

                Carte carte = session.createQuery("select c from Carte c where c.ISBN=:cod ",Carte.class).setParameter("cod",id).setMaxResults(1).uniqueResult();
                System.out.println("Stergem cartea: "+carte);

                session.delete(carte);

                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Deleted All Records From The Database Table!\n");
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public Carte update(Carte entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();

                /*Carte carte = session.createQuery("select c from Carte c where c.ISBN=:cod ",Carte.class).setParameter("cod",entity.getISBN()).setMaxResults(1).uniqueResult();
                System.out.println("Updatam cartea: "+carte);

                carte.setDisponibilitate(entity.getDisponibilitate());*/
                session.update(entity);

                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Deleted All Records From The Database Table!\n");
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }

        }
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
