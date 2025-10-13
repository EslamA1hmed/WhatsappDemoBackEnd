package com.example.whatsappdemo.repo;

import com.example.whatsappdemo.entity.AllMessagesView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllMessagesViewRepository extends JpaRepository<AllMessagesView, Long> {
}
