package ru.example.webtest.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.example.webtest.entity.Record;
import ru.example.webtest.entity.RecordStatus;

import java.util.List;

@Repository
public class RecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Record> findAll() {
        Query query =  entityManager.createQuery("SELECT r FROM Record r ORDER BY r.id ASC ");
        List<Record> records = query.getResultList();
        return records;
    }

    public List<Record> findAllByStatus(RecordStatus status) {
        TypedQuery<Record> query = entityManager.createQuery("SELECT r FROM Record r WHERE r.status = :status ORDER BY r.id ASC", Record.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public int countByStatus(RecordStatus status) {
        return entityManager.createQuery("SELECT COUNT(r) FROM Record r WHERE r.status = :status", Integer.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    public void save(Record record) {
        entityManager.persist(record);
    }

    public void updateRecordStatus(int id, RecordStatus newStatus) {
        Query query = entityManager.createQuery("UPDATE Record SET status = :status WHERE id = :id");
        query.setParameter("id", id);
        query.setParameter("status", newStatus);
        query.executeUpdate();
    }

    public void delete(int id) {
        Query query = entityManager.createQuery("DELETE FROM Record WHERE id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

}

