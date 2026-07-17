package com.daraz.web.converter;

import com.daraz.web.dto.wishlist.WishlistResponseDTO;
import com.daraz.web.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductConverter.class})
public interface WishlistConverter {

    @Mapping(target = "wishlistId", source = "id")
    @Mapping(target = "customerId", source = "customer.id")
    WishlistResponseDTO toDto(Wishlist wishlist);
}
