package persistance;

import model.Carte;
import model.Imprumut;
import model.UtilizatorSistem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ImprumutRepositoryImpl implements ImprumutRepository{
    private static final Logger logger= LogManager.getLogger();
    public static SessionFactory sessionFactory;


    @Override
    public Imprumut findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Imprumut> findAll() {
        List<Imprumut> imprumuturi=new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                imprumuturi = session.createQuery("select  c FROM Imprumut c",Imprumut.class).list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return imprumuturi;
    }

    @Override
    public Imprumut save(Imprumut entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                long id = (long) session.save(entity);
                //Long lastId = ((BigInteger) session.createSQLQuery("select MAX(idImprumut) from imprumuturi").uniqueResult()).longValue();

                entity.setIdImprumut(id);
                tx.commit();
                Long lastId = ((BigInteger) session.createSQLQuery("select MAX(idImprumut) from imprumuturi").uniqueResult()).longValue();

                entity.setIdImprumut(lastId);
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return entity;
    }

    @Override
    public Imprumut delete(Long aLong) {
        return null;
    }

    @Override
    public Imprumut update(Imprumut entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();
                Imprumut imp= session.createQuery("select  c FROM Imprumut c where c.idImprumut=:id ",Imprumut.class)
                        .setParameter("id",entity.idImprumut)
                        .setMaxResults(1)
                        .uniqueResult();

                imp.setStareImprumut(entity.getStareImprumut());
                session.update(imp);

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

    @Override
    public Iterable<Imprumut> findByUser(UtilizatorSistem mainUser) {
        List<Imprumut> imprumuturi=new ArrayList<>();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                imprumuturi = session.createQuery("select  c FROM Imprumut c where c.user=:user",Imprumut.class)
                        .setParameter("user",mainUser)
                        .list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return imprumuturi;
    }

    @Override
    public Iterable<Imprumut> findByUserAndBook(Long userId, Long bookId) {
        List<Imprumut> imprumuturi=new ArrayList<>();
        UtilizatorSistem user = new UtilizatorSistem();
        System.out.println("Caut imprumut cu userID: "+userId+" si carteID: "+bookId);
        user.setCodUnic(userId);
        Carte carte = new Carte();
        carte.setISBN(bookId);
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                imprumuturi = session.createQuery("select  c FROM Imprumut c where c.user=:user and c.carte=:carte",Imprumut.class)
                        .setParameter("user",user)
                        .setParameter("carte",carte)
                        .list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return imprumuturi;
    }
}
