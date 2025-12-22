package com.pdl.bookingmanagement.repository;

import com.pdl.bookingmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.phone = :phone")
    User findByPhoneOrEmail(@Param("email") String email, @Param("phone") String phone);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") Integer id);
}
