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

}
