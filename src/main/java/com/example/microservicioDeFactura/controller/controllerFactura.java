package com.example.microservicioDeFactura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.microservicioDeFactura.model.Factura;
import com.example.microservicioDeFactura.repository.FacturaRepository;
import com.example.microservicioDeFactura.service.facturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/factura/v1")
@Tag(name = "Facturas", description = "Operaciones relacionadas con las facturas")
public class controllerFactura {

    @Autowired
    private facturaService facturaService;

    controllerFactura(FacturaRepository facturaRepository) {
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas las facturas", description = "Obtiene una lista de todas las facturas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron facturas")
    })
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> facturas = facturaService.listarFacturas();
        if(facturas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/buscarPorRut/{rutEmpresa}")
    @Operation(summary = "Buscar facturas por RUT de empresa", description = "Obtiene una lista de facturas emitidas por el RUT de una empresa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Facturas encontradas",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron facturas para el RUT proporcionado")
    })
    public ResponseEntity<List<Factura>> buscarPorRutEmpresa(@PathVariable String rutEmpresa) {
        List<Factura> facturas = facturaService.buscarPorRutEmpresa(rutEmpresa);
        if (facturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/buscarPorId/{id}")
    @Operation(summary = "Buscar factura por Id", description = "Obtiene una factura por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron facturas para el Id proporcionado")
    })
    public ResponseEntity<List<Factura>> buscarPorId(@PathVariable int id) {
        List<Factura> facturas = facturaService.buscarPorId(id);
        if (facturas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(facturas);
    }

    @DeleteMapping("/eliminarPorId/{idEliminar}")
    @Operation(summary = "Eliminar factura por Id", description = "Elimina una factura por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Void.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró la factura para el Id proporcionado")
    })
    public ResponseEntity<Void> eliminarPorId(@PathVariable int idEliminar) {
        facturaService.eliminarPorId(idEliminar);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/guardarFactura")
    @Operation(summary = "Crear nueva factura", description = "Crea una nueva factura en el sistema") 
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura creada exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "Solicitud inválida")
    }) 
    public ResponseEntity<Void> guardarFactura(@RequestBody Factura nuevaFactura) {
        System.out.println("Factura recibida: " + nuevaFactura);
        facturaService.guardarFactura(nuevaFactura);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/actualizarFactura/{id}")
    @Operation(summary = "Actualizar factura por Id", description = "Actualiza una factura existente por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada para el Id proporcionado")
    })
    public ResponseEntity<Void> actualizarFactura(@PathVariable int id, @RequestBody Factura facturaActualizada) {
        if (facturaService.buscarPorId(facturaActualizada.getId())
                .stream()
                .anyMatch(f -> f.getId() == id)) {
                    facturaService.guardarFactura(facturaActualizada);
                    return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}