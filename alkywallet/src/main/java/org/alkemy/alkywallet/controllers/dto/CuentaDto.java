package org.alkemy.alkywallet.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.alkemy.alkywallet.utils.TipoMoneda;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CuentaDto {

    private Long idCuenta;

    private String cbu;

    private String saldo;

    private LocalDateTime fechaApertura;

    private TipoMoneda moneda;

    private UsuarioDto usuarioDto;

    private Set<TarjetaDto> tarjetasDto = new HashSet<>();

    private Set<DepositoDto> depositoDto = new HashSet<>();

    private Set<TransferenciaDto> transferenciaDto = new HashSet<>();

    private Set<AlmacenmientoDto> almacenmientoDto = new HashSet<>();

    private Boolean estado;

}
