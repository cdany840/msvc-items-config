package com.dmiranda.springcloud.msvc.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.dmiranda.libs.msvc.commons.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
