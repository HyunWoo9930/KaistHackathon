package org.example.factorial.controller;


import org.example.factorial.repository.UserRepository;
import org.example.factorial.service.MembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

@RestController
@RequestMapping("/api/membership")
@CrossOrigin(origins = "*")
public class MembershipController {

    private final MembershipService membershipService;

    public MembershipController(MembershipService membershipService) {

        this.membershipService = membershipService;
    }

    @PostMapping
    public ResponseEntity<?> joinMembership(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("account") String account
    ) {
        try {
            membershipService.joinMembership(userDetails, account);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/free")
    public ResponseEntity<?> freeMembership(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            membershipService.freeMembership(userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}