package persistance;

import model.UtilizatorSistem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

public class UtilizatorRepositoryImpl implements UtilizatorRepository{


    private static final Logger logger= LogManager.getLogger();
    public static SessionFactory sessionFactory;


    @Override
    public UtilizatorSistem findOne(Long aLong) {
        UtilizatorSistem findUser = null;
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                // Getting Transaction Object From Session Object
                tx=session.beginTransaction();

                findUser = session.createQuery("from UtilizatorSistem where codUnic =:cod", UtilizatorSistem.class)
                        .setParameter("cod",aLong)
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
        return findUser;
    }

    @Override
    public Iterable<UtilizatorSistem> findAll() {
        List<UtilizatorSistem> userEntities=new ArrayList<>();

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {

                tx = session.beginTransaction();

                userEntities = session.createQuery("select  user FROM UtilizatorSistem user",UtilizatorSistem.class).list();

                tx.commit();
            } catch (Exception sqlException) {
                if (null != tx) {
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");
                    tx.rollback();
                }
                sqlException.printStackTrace();
            }
        }
        return userEntities;
    }

    @Override
    public UtilizatorSistem save(UtilizatorSistem entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                session.save(entity);
                System.out.println("ID USER: "+entity.getCodUnic());
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null){
                    logger.info("\n.......Transaction Is Being Rolled Back.......\n");

                    ex.printStackTrace();
                    tx.rollback();
                }

            }
        }
        return entity;
    }

    @Override
    public UtilizatorSistem delete(Long id) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();
                UtilizatorSistem user= session.createQuery("select c from UtilizatorSistem c where c.codUnic=:cod ",UtilizatorSistem.class).setMaxResults(1).uniqueResult();
                System.out.println("Stergem userul: "+user);

                session.delete(user);

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
    public UtilizatorSistem update(UtilizatorSistem entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();
                //UtilizatorSistem user= session.createQuery("select c from UtilizatorSistem c where c.codUnic=:cod ",UtilizatorSistem.class).setParameter("cod",entity.getCodUnic()).setMaxResults(1).uniqueResult();


                System.out.println(entity.getTip());
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

    @Override
    public UtilizatorSistem findByUsernameAndPassword(String username, String password) {

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                // Getting Session Object From SessionFactory
                tx=session.beginTransaction();

                UtilizatorSistem user = session.createQuery("SELECT user FROM UtilizatorSistem user WHERE user.username = :username and user.parola = :parola",UtilizatorSistem.class)
                        .setParameter("username", username)
                        .setParameter("parola",password)
                        .setMaxResults(1).uniqueResult();


                // Committing The Transactions To The Database
                tx.commit();
                logger.info("\nSuccessfully Deleted All Records From The Database Table!\n");
                return user;
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
}
