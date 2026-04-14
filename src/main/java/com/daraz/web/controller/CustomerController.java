package com.daraz.web.controller;

import com.daraz.web.dto.CustomerDTO;
import com.daraz.web.service.CustomerService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService<CustomerDTO,String> customerService;

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> getCustomer(@PathVariable String id){
        CustomerDTO customerDTO = customerService.viewCustomer(id);
        return new ResponseEntity<StandardResponse>(
                new StandardResponse(
                        200,
                        "Found Customer!",
                        customerDTO
                ),HttpStatus.OK
        );
    }

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createCustomer(@RequestBody CustomerDTO customerDTO){

        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "New Customer Account Created!",
                        savedCustomer
                ), HttpStatus.CREATED
        );

    }

}
