package com.example.whatsappdemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whatsappdemo.entity.MessageButton;

public interface MessageButtonsRepo extends JpaRepository<MessageButton,Long> {

}
