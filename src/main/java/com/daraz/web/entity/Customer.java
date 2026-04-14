package com.daraz.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private String id;
    private String firstName;
    private String lastName;

    private String email;
    private String mobileNumber; // so i expect it shout +94 format.

    private String nic;
}
