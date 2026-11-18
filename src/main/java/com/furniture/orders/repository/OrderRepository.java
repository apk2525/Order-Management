package com.furniture.orders.repository;

import com.furniture.orders.entity.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> id(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id") Long id);

    @Query("Select o from Order o where o.status =:status")
    List<Order> findOrdersByStatus(@Param("status") String status);

    Page<Order> findByStatus(String status, Pageable pageable);

    @Query("""
    SELECT o FROM Order o
    WHERE o.status = :status
      AND o.id > :lastSeenId
    ORDER BY o.id ASC
    """)
    List<Order> findNextOrders(
            @Param("status") String status,
            @Param("lastSeenId") Long lastSeenId,
            Pageable pageable);


}
