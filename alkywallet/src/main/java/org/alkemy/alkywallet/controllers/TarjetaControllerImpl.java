package org.alkemy.alkywallet.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.alkemy.alkywallet.controllers.dto.TarjetaDto;
import org.alkemy.alkywallet.models.Tarjeta;
import org.alkemy.alkywallet.services.TarjetaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controllador Rest para la entidad Tarjeta
 * <br>
 *
 * @Autor: Facundo Martinez
 */

@RestController
@RequestMapping("/api/v1/tarjetas")
@RequiredArgsConstructor
@Tag(name = "TarjetaController", description = "Controlador Rest para la entidad Tarjeta")
public class TarjetaControllerImpl {

    private final TarjetaServiceImpl tarjetaService;


    /**
     * Obtiene todas las tarjetas registradas en la base de datos
     *
     * @return ResponseEntity con el listado de tarjetas
     */
    @Operation(summary = "Metodo que retorna a todas las tarjetas registradas en sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @GetMapping
    public ResponseEntity<List<TarjetaDto>> obtenerTodosTarjetas() {
        return new ResponseEntity<>(tarjetaService.obtenerTodos(), HttpStatus.OK);
    }


    /**
     * Funcion para obtener todas las tarjetas activas en la base de datos
     *
     * @return ResponseEntity con el listado de tarjetas activas
     */
    @Operation(summary = "Metodo que retorna a todas las cuentas registradas y activas en sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @GetMapping("/activas")
    public ResponseEntity<List<TarjetaDto>> obtenerPorEstado() {
        return new ResponseEntity<>(tarjetaService.obtenerPorEstado(), HttpStatus.OK);
    }


    /**
     * Funcion para obtener una tarjeta por su id en la base de datos
     *
     * @param id
     * @return ResponseEntity con la tarjeta encontrada o HttpStatus.NOT_FOUND si no se encuentra ninguna tarjeta con ese id
     */
    @Operation(summary = "Metodo que retorna a todas las cuentas registradas en sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tarjeta> obtenerTarjetaPorId(@PathVariable Long id) {
        return new ResponseEntity<>(tarjetaService.obtenerPorId(id), HttpStatus.OK);
    }


    /**
     * Funcion para crear una tarjeta en la base de datos
     *
     * @param tarjeta
     * @return ResponseEntity con la tarjeta creada o HttpStatus.BAD_REQUEST si se proporcionaron datos erroneos
     */

    @Operation(summary = "Metodo que registradas a una tarjeta en sistema. A partir de una cuenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @PostMapping("/crear/{idCuenta}")
    public ResponseEntity<Tarjeta> crearTarjeta(@RequestBody Tarjeta tarjeta, @PathVariable Long idCuenta) {
        return new ResponseEntity<>(tarjetaService.agregarNuevaTarjetaACuenta(tarjeta, idCuenta), HttpStatus.CREATED);
    }

    /**
     * Funcion para actualizar una tarjeta registrada en la base de datos
     *
     * @param tarjeta
     * @param id      Long
     * @return ResponseEntity con la tarjeta actualizada o HttpStatus.BAD_REQUEST si se proporcionaron datos erroneos
     */

    @Operation(summary = "Metodo que actualiza a una tarjeta segun su ID en sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Tarjeta> actualizarTarjetaPorId(@PathVariable Long id, @RequestBody Tarjeta tarjeta) {
        return new ResponseEntity<>(tarjetaService.actualizar(tarjeta, id), HttpStatus.OK);
    }

    /**
     * Funcion que actualiza el 'estado' una tarjeta registrada en la base de datos.
     *
     * @param id Long
     * @return Map<String, String>
     */
    @Operation(summary = "Metodo que actualiza el 'estado' a una tarjeta segun su ID en sistema. Soft-delete.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "OK"
                    , content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Forbidden"
                    , content = {
                    @Content(mediaType = "application/json")
            })
    })
    @PatchMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, String>> eliminarTarjetaPorId(@PathVariable Long id) {
        tarjetaService.eliminar(id);
        return new ResponseEntity<>(Map.of("message", "Tarjeta ID: " + id), HttpStatus.NO_CONTENT);
    }
}
