package com.grocify.backend.repository;

import com.grocify.backend.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findTopByEmailOrderByIdDesc(String email);

}