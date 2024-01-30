package com.moviebookingapp.controller;

import com.moviebookingapp.dto.BookedTableDto;
import com.moviebookingapp.excpetion.TicketException;
import com.moviebookingapp.model.Ticket;
import com.moviebookingapp.service.TicketService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1.0/moviebooking/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping("/book/{movieId}")
    public ResponseEntity<Ticket> bookTicket(@RequestBody BookedTableDto bookedTableDto, @PathVariable String movieId) throws TicketException {
        return  new ResponseEntity<Ticket>(ticketService.bookTicket(bookedTableDto,movieId), HttpStatus.OK);
    }

    @GetMapping("/booked_table")
    public ResponseEntity<List<Ticket>> bookedTableShow(){

        return new ResponseEntity<List<Ticket>>(ticketService.getTicketTable(),HttpStatus.OK);
    }

}
