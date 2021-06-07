package com.pubmed.spring.datajpa.repository;

import com.pubmed.spring.datajpa.constants.ERole;
import com.pubmed.spring.datajpa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}