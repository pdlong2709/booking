package com.pdl.bookingmanagement.repository;

import com.pdl.bookingmanagement.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrder_Id(int orderId);
}
