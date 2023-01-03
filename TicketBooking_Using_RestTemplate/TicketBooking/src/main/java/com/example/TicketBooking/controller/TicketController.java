package com.example.TicketBooking.controller;
import com.example.TicketBooking.service.TicketService;
import com.example.TicketBooking.entity.Ticket;
import com.example.TicketBooking.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("railway/tickets")
public class TicketController {



    @Autowired
    TicketService ticketService;
@Autowired
    RestTemplate restTemplate;
@Value("${payement.url}")
String payementurl;
@Value("${booking.status.succes}")
    String bookingstatussucces;
@Value("${booking.status.failure}")
String bookingstatusfailure;
    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping
    public  String UserTicket(@Valid @RequestBody Ticket ticket) {
        //return ticketService.saveTicket(ticket);
        //   return new ResponseEntity(new ApiResponse("User ticket Saved Succesfuylly",true), HttpStatus.OK)
        Ticket payload = null;
        System.out.println("payement url" + payementurl);
        URI uri;
        try {
            uri = new URI(payementurl);
            HttpHeaders headers = new HttpHeaders();
        //    headers.set("sample", "true"); // sample strcuture u can neglect it
         //   headers.set("sample1", "subscribe");// sample strcuture u can neglect it
            HttpEntity<Ticket> request = new HttpEntity<>(ticket,headers);
            ResponseEntity<Ticket> result = restTemplate.postForEntity(uri,request,Ticket.class);
            payload = result.getBody();
            ticketRepository.save(payload);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if(payload.getStatus().equals("Booked"))
        return  bookingstatussucces;
        return bookingstatusfailure;

    }
    @GetMapping("/")
    public List<Ticket> getAllUsersticket() {
        return ticketService.getTicketInfo();
    }
}
