package org.alkemy.alkywallet.services;

import lombok.RequiredArgsConstructor;
import org.alkemy.alkywallet.models.Cuenta;
import org.alkemy.alkywallet.models.Usuario;
import org.alkemy.alkywallet.repositories.CuentaRepository;
import org.alkemy.alkywallet.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl {

    private final UsuarioRepository usuarioRepository;

    private final CuentaRepository cuentaRepository;

    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getEstado().equals(true))
                .toList();
    }

    public Usuario obtenerUsuarioPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("El id del usuario no puede ser nulo");
        }
        return usuarioRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con el id: " + id + " no existe"));
    }

    public Usuario actualizarUsuarioPorId(Long id, Usuario usuario) {

        Usuario usuarioActualizado = usuarioRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con el id: " + id + " no existe"));


        usuarioActualizado.setNombre(
                (usuario.getNombre() != null) ? usuario.getNombre() : usuarioActualizado.getNombre()
        );

        usuarioActualizado.setApellido(
                (usuario.getApellido() != null) ? usuario.getApellido() : usuarioActualizado.getApellido()
        );

        usuarioActualizado.setEmail(
                (usuario.getEmail() != null) ? usuario.getEmail() : usuarioActualizado.getEmail()
        );

        usuarioActualizado.setContrasenia(
                (usuario.getContrasenia() != null) ? usuario.getContrasenia() : usuarioActualizado.getContrasenia()
        );

        usuarioActualizado.setRoles(
                usuario.getRoles() != null ? usuario.getRoles() : usuarioActualizado.getRoles()
        );

        usuarioActualizado.setEstado(
                (usuario.getEstado() != null) ? usuario.getEstado() : usuarioActualizado.getEstado()
        );

        return usuarioRepository.save(usuarioActualizado);

    }

    /**
     * Funcion soft-delete del usuario por ID.
     * <br>
     * Al cambiar el estado a 'false', el estado de todos sus productos cambia a 'false'
     *
     * @param id Long
     */
    public void eliminarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con el id: " + id + " no existe"));

        List<Cuenta> cuentas = cuentaRepository.findCuentasByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con el id: " + id + " no tiene cuentas asociadas"));

        cuentas.forEach(c ->
                c.getTarjetas()
                        .forEach(t -> {
                            t.setEstado(false);
                        }));
        cuentas.forEach(cuenta -> cuenta.getDeposito().forEach(d -> d.setEstado(false)));
        cuentas.forEach(cuenta -> cuenta.getAlmacenamientoSaldo().forEach(a -> a.setEstado(false)));
        cuentas.forEach(cuenta -> cuenta.getTransferencia().forEach(t -> t.setEstado(false)));
        cuentas.forEach(c -> c.setEstado(false));
        cuentaRepository.saveAll(cuentas);
        usuario.setEstado(false);

        usuarioRepository.save(usuario);
    }
}