package org.alkemy.alkywallet.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alkemy.alkywallet.controllers.dto.TransferenciaDto;
import org.alkemy.alkywallet.models.Cuenta;
import org.alkemy.alkywallet.models.Tarjeta;
import org.alkemy.alkywallet.models.Transferencia;
import org.alkemy.alkywallet.repositories.CuentaRepository;
import org.alkemy.alkywallet.repositories.TarjetaRepository;
import org.alkemy.alkywallet.repositories.TransferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferenciaServiceImpl {

    private final TransferenciaRepository transferenciaRepository;
    private final CuentaRepository cuentaRepository;
    private final TarjetaRepository tarjetaRepository;

    public List<Transferencia> obtenerTodasLasTransferencias() {
        return transferenciaRepository.findAll();
    }


    public List<Transferencia> obtenerTransferenciasPorEstado(Boolean estado) {
        return transferenciaRepository.findAllByEstado(estado);
    }


    public Transferencia obtenerTransferenciaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la transferencia no puede ser nulo");
        }

        return transferenciaRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La transferencia con el id: " + id + " no existe"));
    }


    @Transactional
    public Transferencia realizarTransferencia(TransferenciaDto transferenciaDto, Long idTarjeta) {
        // Validar que la tarjeta existe
        Tarjeta tarjeta = tarjetaRepository
                .findById(idTarjeta)
                .filter(t -> t.getEstado().equals(true))
                .orElseThrow(() -> new IllegalArgumentException("La tarjeta con el id: " + idTarjeta + " no existe"));


        // Obtener cuenta asociada a la tarjeta
        Cuenta cuenta = cuentaRepository.findByTarjetaId(idTarjeta)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró ninguna cuenta asociada a esta tarjeta"));



        // Validar que la cuenta tiene saldo suficiente
        BigDecimal saldoActual = new BigDecimal(cuenta.getSaldo());
        BigDecimal montoTransferencia = new BigDecimal(transferenciaDto.monto());

        if (saldoActual.compareTo(montoTransferencia) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la transferencia");
        }

        // Crear la transferencia
        Transferencia transferencia = new Transferencia();
        transferencia.setTipoTransferencia(transferenciaDto.tipoTransferencia());
        transferencia.setMotivo(transferenciaDto.motivo());
        transferencia.setNombreDestinatario(transferenciaDto.nombreDestinatario());
        transferencia.setBancoDestino(transferenciaDto.bancoDestino());
        transferencia.setCuentaDestinatario(transferenciaDto.cuentaDestinatario());
        transferencia.setCuentaOrigen(cuenta.getCbu());
        transferencia.setEstado(true);
        transferencia.setFecha(LocalDateTime.now());
        transferencia.setCuenta(cuenta);
        transferencia.setMonto(montoTransferencia.toString());

        tarjeta.getTransferencias().add(transferencia); // relacion entre tarjeta y transferencia

        log.info("Transfer made successfully");
        log.info("Amount transfer : {}", transferencia.getMonto());
        log.info("Transfer date : {}", transferencia.getFecha());
        log.info("Account : {}. Total amount: {}", cuenta.getCbu(), cuenta.getSaldo());


        // Actualizar saldo de la cuenta
        BigDecimal nuevoSaldo = saldoActual.add(montoTransferencia);
        cuenta.setSaldo(nuevoSaldo.toString());
        cuentaRepository.save(cuenta);

        log.info("New account balance after transfer: {}", cuenta.getSaldo());

        // Guardar la transferencia
        return transferenciaRepository.save(transferencia);
    }

    public void cancelarTransferencia(Long id) {
        Transferencia transferencia = obtenerTransferenciaPorId(id);
        transferencia.setEstado(false);
        transferenciaRepository.save(transferencia);
    }
}