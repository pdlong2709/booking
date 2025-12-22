package com.pdl.bookingmanagement.repository;

import com.pdl.bookingmanagement.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findBySeller_IdOrderByOrderDateDesc(int sellerId);

    @Query("SELECT o FROM Order o " +
            "WHERE LOWER(o.customer.fullName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "AND o.seller.id = :sellerId")
    List<Order> findByCustomerName(@Param("name") String name,
                                   @Param("sellerId") int sellerId);

}
