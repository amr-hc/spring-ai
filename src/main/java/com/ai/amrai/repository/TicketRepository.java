package com.ai.amrai.repository;

import com.ai.amrai.entity.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUsernameOrderByCreatedAtDescIdDesc(String username);
}
