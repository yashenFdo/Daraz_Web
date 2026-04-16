package com.daraz.web.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author : yashen
 * @created : 4/16/26
 * @project : web
 **/
@Entity
@Table(name = "customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "customer_id", length = 36)
    private String id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mobile_number", nullable = false, unique = true, length = 15)
    private String mobileNumber; // Format: +94712345678

    @Column(name = "nic", nullable = false, unique = true, length = 20)
    private String nic; // Format : 12345678V or 200212233445

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isActive = true;
}