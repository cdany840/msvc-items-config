package com.dmiranda.springcloud.msvc.items.services;

import java.util.List;
import java.util.Optional;

import com.dmiranda.libs.msvc.commons.entities.Product;
import com.dmiranda.springcloud.msvc.items.models.Item;

public interface ItemService {

    List<Item> findAll();

    Optional<Item> findById(Long id);

    Product save(Product product);

    Product update(Product product, Long id);

    void delete(Long id);
}
