package com.daraz.web.controller;

import com.daraz.web.dto.customer.CustomerDTO;
import com.daraz.web.service.CustomerService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final com.daraz.web.service.EmailService emailService;
    private final com.daraz.web.repo.CustomerRepo customerRepo;

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getCustomer(@PathVariable String id){
        CustomerDTO customerDTO = customerService.viewById(id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        "Customer Account Found!",
                        customerDTO
                ),HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createCustomer(@RequestBody CustomerDTO customerDTO){

        CustomerDTO savedCustomer = customerService.save(customerDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "New Customer Account Created!",
                        savedCustomer
                ), HttpStatus.CREATED
        );

    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<StandardResponse> updateCustomer(@PathVariable String id,@RequestBody CustomerDTO customerDTO){
        CustomerDTO updatedCustomer = customerService.modify(id, customerDTO);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        "Customer Account Updated",
                        updatedCustomer
                ),HttpStatus.OK
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> remove(@PathVariable String id){
        boolean isDeleted = customerService.remove(id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        "Customer Account Deleted",
                        isDeleted
                ),HttpStatus.OK
        );
    }

    @GetMapping("")
    public ResponseEntity<StandardResponse> getAllCustomers(){
        List<CustomerDTO> customerDTOS = customerService.viewAll();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Fetched "+customerDTOS.size()+" customer from database.",
                        customerDTOS
                ),HttpStatus.OK
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<StandardResponse> forgotPassword(@RequestParam String email) {
        if (!customerRepo.existsByEmail(email)) {
            throw new com.daraz.web.exception.custom.EntryNotFoundException("Email is not registered!");
        }
        String mockOtp = String.valueOf((int) (Math.random() * 900000) + 100000); // 6-digit OTP
        emailService.sendPasswordResetEmail(email, mockOtp);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Password reset verification email sent!",
                        mockOtp
                ), HttpStatus.OK
        );
    }

    @PostMapping("/wishlist-reminder/{customerId}")
    public ResponseEntity<StandardResponse> sendWishlistReminder(
            @PathVariable String customerId,
            @RequestParam String productName,
            @RequestParam String price
    ) {
        CustomerDTO customer = customerService.viewById(customerId);
        emailService.sendWishlistReminderEmail(customer.getEmail(), productName, price);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Wishlist reminder email sent successfully!",
                        null
                ), HttpStatus.OK
        );
    }
}
