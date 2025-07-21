package org.alkemy.alkywallet.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.alkemy.alkywallet.controllers.dto.AuthCreateRequest;
import org.alkemy.alkywallet.controllers.dto.AuthLoginRequest;
import org.alkemy.alkywallet.controllers.dto.AuthResponse;
import org.alkemy.alkywallet.models.Rol;
import org.alkemy.alkywallet.models.Usuario;
import org.alkemy.alkywallet.repositories.RoleRepository;
import org.alkemy.alkywallet.repositories.UsuarioRepository;
import org.alkemy.alkywallet.auth.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final CuentaServiceImpl cuentaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuarioExistente = repository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario con el email: " + username + " no existe"));

        List<GrantedAuthority> authorities = new ArrayList<>();


        usuarioExistente.getRoles()
                .forEach(roles -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(roles.getRole().name()))));

        return new User(
                usuarioExistente.getEmail(),
                usuarioExistente.getContrasenia(),
                true,
                true,
                true,
                true,
                authorities);
    }


    public AuthResponse loginUser(AuthLoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Usuario usuario = repository.findByEmail(username).orElseThrow();

        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(username, "User logged succesfuly", accessToken, true, usuario.getIdUsuario());
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }


    public AuthResponse registerUser(AuthCreateRequest registerRequest) {
        String email = registerRequest.email();
        String password = registerRequest.password();
        List<String> roles = registerRequest.roleRequest().roleListName();

        Set<Rol> roleSet = new HashSet<>(roleRepository.findRolesByRoleIn(roles));

        if (roleSet.isEmpty()) {
            throw new BadCredentialsException("Invalid roles");
        }

        Usuario usuario = Usuario.builder()
                .nombre(registerRequest.nombre())
                .apellido(registerRequest.apellido())
                .email(email)
                .contrasenia(passwordEncoder.encode(password))
                .roles(roleSet)
                .build();

        Usuario usuarioCreado = repository.save(usuario);

        List<GrantedAuthority> authorityList = new ArrayList<>();

        usuarioCreado.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().name())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getContrasenia(), authorityList);

        String token = jwtUtils.createToken(authentication);

        //FIXME: la logica queda acoplada al registro de usuarios
        cuentaService.crearCuentAInicialApartirDeUsuario(usuarioCreado.getIdUsuario());

        return new AuthResponse(email, "User registered succesfuly", token, true, usuarioCreado.getIdUsuario());
    }

    public AuthResponse renewToken(String token) {
        // 1. Validar el token
        if (token != null && token.startsWith("Bearer ")) {

            // 2. Obtener el username desde el token
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);

            String username = jwtUtils.extractUsername(decodedJWT);

            // 3. Cargar el usuario usando el username extraído
            UserDetails userDetails = loadUserByUsername(username);

            // 4. Crear un nuevo token JWT
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

            String newToken = jwtUtils.createToken(authentication);

            // 5. Devolver AuthResponse con el nuevo token
            Usuario usuario = repository.findByEmail(username).orElseThrow();
            return new AuthResponse(username, "Token renovado correctamente", newToken, true, usuario.getIdUsuario());
        }

        throw new BadCredentialsException("Token inválido o expirado");
    }

}
