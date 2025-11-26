package com.inventario.alma_jesus.service;

import com.inventario.alma_jesus.model.Usuario;
import com.inventario.alma_jesus.repository.UsuarioRepository;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Map<String, Object> listarUsuarios() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Usuario> usuarios = usuarioRepository.findAll();

            List<Map<String, Object>> usuariosSeguros = usuarios.stream()
                    .map(usuario -> {
                        Map<String, Object> usuarioMap = new HashMap<>();
                        usuarioMap.put("id", usuario.getId());
                        usuarioMap.put("username", usuario.getUsername());
                        usuarioMap.put("rol", usuario.getRol());
                        usuarioMap.put("email", usuario.getEmail());
                        usuarioMap.put("activo", usuario.isActivo());
                        usuarioMap.put("fechaCreacion", usuario.getFechaCreacion());
                        return usuarioMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("message", "Usuarios listados exitosamente");
            response.put("usuarios", usuariosSeguros);
            response.put("total", usuariosSeguros.size());

            System.out.println("Listados " + usuariosSeguros.size() + " usuarios");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al listar usuarios: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> obtenerUsuarioPorId(String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return response;
            }

            Usuario usuario = usuarioOpt.get();

            Map<String, Object> usuarioSeguro = new HashMap<>();
            usuarioSeguro.put("id", usuario.getId());
            usuarioSeguro.put("username", usuario.getUsername());
            usuarioSeguro.put("rol", usuario.getRol());
            usuarioSeguro.put("email", usuario.getEmail());
            usuarioSeguro.put("activo", usuario.isActivo());
            usuarioSeguro.put("fechaCreacion", usuario.getFechaCreacion());

            response.put("success", true);
            response.put("message", "Usuario encontrado exitosamente");
            response.put("usuario", usuarioSeguro);

            System.out.println("Usuario encontrado: " + usuario.getUsername());

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al obtener usuario: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> crearUsuario(Map<String, String> usuarioData) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validar datos requeridos
            if (usuarioData.get("id") == null || usuarioData.get("username") == null ||
                    usuarioData.get("password") == null || usuarioData.get("rol") == null) {
                response.put("success", false);
                response.put("message", "ID, username, password y rol son requeridos");
                return response;
            }

            //VERIFICACIÓN MEJORADA: Checar si ID ya existe
            Optional<Usuario> usuarioConId = usuarioRepository.findById(usuarioData.get("id"));
            if (usuarioConId.isPresent()) {
                response.put("success", false);
                response.put("message", "El ID de usuario ya esta en uso");
                return response;
            }

            // VERIFICACIÓN MEJORADA: Checar si username ya existe
            Optional<Usuario> usuarioConUsername = usuarioRepository.findByUsername(usuarioData.get("username"));
            if (usuarioConUsername.isPresent()) {
                response.put("success", false);
                response.put("message", "El username ya esta en uso");
                return response;
            }

            // Crear nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setId(usuarioData.get("id"));
            nuevoUsuario.setUsername(usuarioData.get("username"));
            nuevoUsuario.setPassword(PasswordUtil.encryptPassword(usuarioData.get("password")));
            nuevoUsuario.setRol(usuarioData.get("rol"));
            nuevoUsuario.setEmail(usuarioData.get("email"));
            nuevoUsuario.setActivo(true);

            boolean creado = usuarioRepository.crearUsuario(nuevoUsuario);

            if (creado) {
                response.put("success", true);
                response.put("message", "Usuario creado exitosamente");
                response.put("usuario", Map.of(
                        "id", nuevoUsuario.getId(),
                        "username", nuevoUsuario.getUsername(),
                        "rol", nuevoUsuario.getRol(),
                        "email", nuevoUsuario.getEmail(),
                        "activo", nuevoUsuario.isActivo()
                ));

                System.out.println("Usuario creado: " + nuevoUsuario.getUsername());
            } else {
                response.put("success", false);
                response.put("message", "Error al crear usuario en la base de datos");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al crear usuario: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> cambiarPassword(String id, String nuevaPassword) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return response;
            }

            if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "La nueva contrasena es requerida");
                return response;
            }

            String passwordEncriptada = PasswordUtil.encryptPassword(nuevaPassword);

            boolean actualizado = usuarioRepository.cambiarPassword(id, passwordEncriptada);

            if (actualizado) {
                response.put("success", true);
                response.put("message", "Contraseña actualizada exitosamente");
                response.put("usuario", usuarioOpt.get().getUsername());

                System.out.println("Contraseña cambiada para: " + usuarioOpt.get().getUsername());
            } else {
                response.put("success", false);
                response.put("message", "Error al actualizar contrasena");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al cambiar contrasena: " + e.getMessage());
        }

        return response;
    }

    public Map<String, Object> eliminarUsuario(String id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);

            if (usuarioOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return response;
            }

            Usuario usuario = usuarioOpt.get();

            // No permitir eliminar usuarios principales
            if (usuario.getId().equals("admin001") || usuario.getId().equals("trab001")) {
                response.put("success", false);
                response.put("message", "No se puede eliminar usuarios principales del sistema");
                return response;
            }

            boolean eliminado = usuarioRepository.eliminarUsuario(id);

            if (eliminado) {
                response.put("success", true);
                response.put("message", "Usuario eliminado exitosamente");
                response.put("usuario", usuario.getUsername());

                System.out.println("Usuario eliminado: " + usuario.getUsername());
            } else {
                response.put("success", false);
                response.put("message", "Error al eliminar usuario");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
        }

        return response;
    }
}