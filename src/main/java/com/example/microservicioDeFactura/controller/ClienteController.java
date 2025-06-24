package com.example.microservicioDeFactura.controller;

import com.example.microservicioDeFactura.model.Cliente;
import com.example.microservicioDeFactura.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con los clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


      // GET: Listar todos los clientes

    @GetMapping("/listarTodos")
    @Operation(summary = "Listar todos los clientes", description = "Obtiene una lista de todos los clientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron clientes")
    })
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    // GET: Obtener cliente por ID

    @GetMapping("/obtenerPorId/{id}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // POST: Crear un nuevo cliente

    @PostMapping("/crearCliente")
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Solicitud inválida")
    })
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        Cliente nuevo = clienteService.guardar(cliente);
        return ResponseEntity.ok(nuevo);
    }

      // PUT: Actualizar cliente por ID

    @PutMapping("/actualizarClientePorId/{id}")
    @Operation(summary = "Actualizar cliente por Id", description = "Actualiza un cliente existente por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.buscarPorId(id)
                .map(existente -> {
                    cliente.setId(id);
                    Cliente actualizado = clienteService.guardar(cliente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Eliminar cliente por ID
    
    @DeleteMapping("/eliminarClientePorId/{id}")
    @Operation(summary = "Eliminar cliente por Id", description = "Elimina un cliente existente por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Void.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (clienteService.buscarPorId(id).isPresent()) {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/obtenerPorRut/{rutEmpresa}")
    @Operation(summary = "Obtener Empresa por RUT", description = "Obtiene una Empresa por su RUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    public ResponseEntity<Cliente> obtenerPorRut(@PathVariable String rutEmpresa) {
        return clienteService.buscarPorRut(rutEmpresa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}