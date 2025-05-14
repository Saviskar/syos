package main.com.syos.dao;

import main.com.syos.model.Bill;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillDao {
    void save(Bill bill);
    Optional<Bill> findById(Long billId);
    List<Bill> findByDate(LocalDate date);
    List<Bill> findAll();
    void update(Bill bill);
    void delete(Long billId);
}
