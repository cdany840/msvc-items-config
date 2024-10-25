package com.dmiranda.springcloud.msvc.products.controllers;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dmiranda.libs.msvc.commons.entities.Product;
import com.dmiranda.springcloud.msvc.products.services.ProductService;


@RestController
public class ProductController {

    final private ProductService service;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService service) {
        this.service = service;
    }    

    @GetMapping
    public ResponseEntity<?> list(@RequestHeader(name = "message-request", required = false) String message) {
        logger.info("Llamada a método ProductController::List()");
        logger.info("message: {}", message);
        return ResponseEntity.ok(this.service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Long id) throws InterruptedException {

        logger.info("Llamada a método ProductController::Details()");

        if (id.equals(10L)) {
            // throw new IllegalStateException("Producto no encontrado!");
        }

        if (id.equals(7L)) {
            TimeUnit.SECONDS.sleep(3L);
        }

        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        logger.info("Product creando: {}", product);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Product actualizando: {}", product);
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            Product productDb = productOptional.orElseThrow();
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setCreateAt(product.getCreateAt());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(productDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("Product eliminando: {}", id);
        Optional<Product> productOptional = service.findById(id);
        if (productOptional.isPresent()) {
            this.service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    
}
