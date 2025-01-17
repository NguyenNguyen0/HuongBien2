package huongbien.dao;

import huongbien.entity.Payment;
import huongbien.jpa.PersistenceUnit;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PaymentDAO extends GenericDAO<Payment> {
    public PaymentDAO(PersistenceUnit persistenceUnit) {
        super(persistenceUnit);
    }

    public List<Payment> getAll() {
        return findMany("SELECT p FROM Payment p", Payment.class);
    }

    public Payment getById(String id) {
        return findOne("SELECT p FROM Payment p WHERE p.id = ?1", Payment.class, id);
    }
}
