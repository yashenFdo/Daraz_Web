package com.daraz.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : yashen
 * @created : 4/10/26
 * @project : web
 * @email : yashensavindu@gmail.com
 * @since : 0.1.0
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDTO {
    private String id;
    private String firstName;
    private String lastName;

    private String email;
    private String mobileNumber; // so i expect it shout +94 format.

    private String nic;
}