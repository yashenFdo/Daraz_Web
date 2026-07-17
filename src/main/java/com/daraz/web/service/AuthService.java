package com.daraz.web.service;

import com.daraz.web.dto.auth.AuthRequestDTO;
import com.daraz.web.dto.auth.AuthResponseDTO;
import com.daraz.web.dto.customer.CustomerDTO;

public interface AuthService {
    CustomerDTO register(CustomerDTO customerDTO);
    AuthResponseDTO login(AuthRequestDTO dto);
}
