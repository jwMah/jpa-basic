package helloJpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Parent parent = new Parent();
            Child child1 = new Child();
            Child child2 = new Child();

            parent.addChild(child1);
            parent.addChild(child2);
            List<Member> result =  em.createQuery(
                    "select m From Member m where m.username like '%kim%'")
                    .getResultList();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);
            
            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m);

            String username = "abcde";
            if(username != null) {
                cq = cq.where(cb.equal(m.get("username"),"kim"));
            }
            result = em.createQuery(cq).getResultList();
            System.out.println(result);

            em.persist("result : " + parent);
            //em.persist(child1);
            //em.persist(child2);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();

    }
}
