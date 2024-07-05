package org.desarrollo.libreria.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.desarrollo.libreria.web.model.entity.Prestamo;
import org.desarrollo.libreria.web.model.entity.Usuario;
import org.desarrollo.libreria.web.model.services.LibreriaService;
import org.desarrollo.libreria.web.utils.paginator.PageRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/libreria")
@SessionAttributes("usuario")
public class UsuarioController {

    @Autowired
    private LibreriaService libreriaService;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/usuarioslistar")
    public String usuarioListar(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model) {
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Usuario> usuarios = libreriaService.buscarUsuariosTodos(pagina);

        PageRender<Usuario> pageRender = new PageRender<>("/libreria/usuarioslistar", usuarios);

        model.addAttribute("titulo", "Listado de usuarios activos");
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("pageRender", pageRender);
        return "usuario/listado_usuarios";
    }

    @GetMapping("/usuarionuevo")
    public String usuarioFormNuevo(Model model) {
        model.addAttribute("titulo", "Nuevo Usuario");
        model.addAttribute("accion", "Crear");
        model.addAttribute("usuario", new Usuario());
        return "usuario/nuevo_usuario";
    }

    @PostMapping("/usuarioagregar")
    public String usuarioAgregar(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result,
            Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        String accion = (usuario.getId() == null) ? "Crear" : "Modificar";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Usuario");
            model.addAttribute("accion", accion);
            model.addAttribute("error", "Por favor corrige los errores ");
            return "usuario/nuevo_usuario";
        }

        if (!foto.isEmpty()) {

            String nombreUnico = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

            Path rutaUploads = Paths.get("uploads").resolve(nombreUnico);
            Path rutaAbsoluta = rutaUploads.toAbsolutePath();

            log.info("rutaUploads: " + rutaUploads);
            log.info("rutaAbsoluta: " + rutaAbsoluta);

            try {
                Files.copy(foto.getInputStream(), rutaAbsoluta);

                flash.addFlashAttribute("info", "На cargado correctamente la foto 1" +
                        nombreUnico + "]");

                usuario.setFoto(nombreUnico);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        

        libreriaService.actualizarUsuario(usuario);
        status.setComplete();
        flash.addFlashAttribute("success", accion + " usuario, agregado exitosamente");
        return "redirect:/libreria/usuarioslistar";
    }

    @GetMapping("/usuarioconsultar/{id}")
    public String usaurioConsultar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = libreriaService.buscarUsuarioPorId(id);
        
        for(Prestamo p: usuario.getPrestamos()) {
        	p.diferenciaDias();
        }

        if (usuario == null) {
            flash.addFlashAttribute("error", "El usuario a consultar no existe");
            return "redirect:/libreria/usuarioslistar";
        }
        model.addAttribute("titulo", "Consulta del usuario: " + usuario.getNombre()+ " " + usuario.getApellido());
        model.addAttribute("usuario", usuario);

        return "usuario/consulta_usuario";
    }

    @GetMapping("/usuarioeliminar/{id}")
    public String usuarioEliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            libreriaService.eliminarUsuarioPorId(id);
            flash.addFlashAttribute("success", "Usuario eliminado con éxito");
        }
        return "redirect:/libreria/usuarioslistar";
    }

    @GetMapping("/usuariomodificar/{id}")
    public String usuarioFormModificar(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash) {
        Usuario usuario = null;
        if (id > 0) {
            usuario = libreriaService.buscarUsuarioPorId(id);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El usuario a modificar no existe");
                return "redirect:/libreria/usuarioslistar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del usuario debe ser positivo");
            return "redirect:/libreria/usuarioslistar";
        }
        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar Usuario");
        model.addAttribute("usuario", usuario);
        return "usuario/nuevo_usuario";
    }
}
