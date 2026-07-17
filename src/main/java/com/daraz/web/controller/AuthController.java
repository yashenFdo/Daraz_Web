package com.daraz.web.controller;

import com.daraz.web.dto.auth.AuthRequestDTO;
import com.daraz.web.dto.auth.AuthResponseDTO;
import com.daraz.web.dto.customer.CustomerDTO;
import com.daraz.web.service.AuthService;
import com.daraz.web.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> register(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO result = authService.register(customerDTO);
        return new ResponseEntity<>(
                new StandardResponse(
                        201,
                        "Registered successfully",
                        result
                ), HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse> login(@RequestBody AuthRequestDTO dto) {
        AuthResponseDTO responseDTO = authService.login(dto);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "Logged in successfully",
                        responseDTO
                ), HttpStatus.OK
        );
    }
}
