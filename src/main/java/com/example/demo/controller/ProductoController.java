
package com.example.demo.controller;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    // Listar todos los productos
    @GetMapping
    public List<Producto> listar() {
        return repository.findAll();
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                    ResponseEntity.notFound().build());
    }

    // Registrar producto
    @PostMapping
    public ResponseEntity<Producto> crear(
            @RequestBody Producto producto) {

        producto.setId(null);
        Producto guardado = repository.save(producto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(guardado);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody Producto datos) {

        return repository.findById(id)
                .map(producto -> {
                    producto.setNombre(datos.getNombre());
                    producto.setDescripcion(datos.getDescripcion());
                    producto.setPrecio(datos.getPrecio());
                    producto.setStock(datos.getStock());

                    return ResponseEntity.ok(
                            repository.save(producto));
                })
                .orElseGet(() ->
                    ResponseEntity.notFound().build());
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
