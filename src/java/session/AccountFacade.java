/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Account;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author artjo
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> {


    @PersistenceContext(unitName = "PassManagerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AccountFacade() {
        super(Account.class);
    }
    
    public List<Account> findByUser(User user) {
        try {
            return em.createQuery("SELECT a FROM Account a WHERE a.user = :user")
                    .setParameter("user", user)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
