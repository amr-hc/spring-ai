package com.ai.amrai.service;

import com.ai.amrai.entity.Ticket;
import com.ai.amrai.repository.TicketRepository;
import java.util.List;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Tool(description = "Create a support ticket when the user asks for one. Ask only for a missing issue title or description. The username comes from the request, so never ask for it.")
    public Ticket createTicket(String title, String description, ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        return ticketRepository.save(new Ticket(username, title, description));
    }

    @Tool(description = "Get the current user's previous support tickets, newest first, including their details and status. The username comes from the request, so never ask for it. An empty list means the user has no tickets.")
    public List<Ticket> getPreviousTickets(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        return ticketRepository.findByUsernameOrderByCreatedAtDescIdDesc(username);
    }
}
