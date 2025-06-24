package com.example.microservicioDeFactura.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservicioDeFactura.model.Residuo;
import com.example.microservicioDeFactura.service.ResiduoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/residuos/v1")
@Tag(name = "Residuos", description = "Operaciones relacionadas con los residuos")
public class ResiduoController {

    @Autowired
    private ResiduoService residuoService;

    @GetMapping("/listarResiduos")
    @Operation(summary = "Listar todos los residuos", description = "Obtiene una lista de todos los residuos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de residuos obtenida",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Residuo.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron residuos")
    })
    public ResponseEntity<List<Residuo>> listar() {
        List<Residuo> residuos = residuoService.findAll();
        if (residuos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(residuos);
    }

    @GetMapping("/buscarPorId/{id}")
    @Operation(summary = "Buscar residuo por Id", description = "Obtiene un residuo por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Residuo encontrado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Residuo.class))),
        @ApiResponse(responseCode = "404", description = "Residuo no encontrado")
    })
    public ResponseEntity<Residuo> buscarPorId(@PathVariable Integer id) {
        Optional<Residuo> residuo = residuoService.findById(id);
        return residuo.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/eliminarPorId/{id}")
    @Operation(summary = "Eliminar residuo por Id", description = "Elimina un residuo por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Residuo eliminado",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
        @ApiResponse(responseCode = "404", description = "Residuo no encontrado")
    })
    public ResponseEntity<Void> eliminarPorId(@PathVariable Integer id) {
        Optional<Residuo> residuo = residuoService.findById(id);
        if (residuo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        residuoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/guardarResiduos")
    @Operation(summary = "Crear un nuevo residuo", description = "Crea un nuevo residuo en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Residuo creado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Residuo.class))),
        @ApiResponse(responseCode = "404", description = "Solicitud inválida")
    })
    public ResponseEntity<Void> guardar(@RequestBody Residuo nuevoResiduo) {
        if (nuevoResiduo == null) {
            return ResponseEntity.badRequest().build();
        }
        residuoService.guardar(nuevoResiduo);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/actualizarResiduos/{id}")
    @Operation(summary = "Actualizar residuo por Id", description = "Actualiza un residuo existente por su Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Residuo actualizado exitosamente",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Residuo.class))),
        @ApiResponse(responseCode = "404", description = "Residuo no encontrado")
    })
    public ResponseEntity<Void> actualizar(@PathVariable Integer id, @RequestBody Residuo nuevoResiduo) {
        Optional<Residuo> existente = residuoService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        residuoService.actualizar(id, nuevoResiduo);
        return ResponseEntity.ok().build();
    }
}