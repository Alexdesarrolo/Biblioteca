package org.desarrollo.libreria.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.desarrollo.libreria.web.model.entity.Libro;
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
@SessionAttributes("libro")
public class LibroController {
    
    @Autowired
    private LibreriaService libreriaService;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/libroslistar")
    public String libroListar(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model) {
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Libro> libros = libreriaService.buscarLibrosTodos(pagina);

        PageRender<Libro> pageRender = new PageRender<>("/libreria/libroslistar", libros);

        model.addAttribute("titulo", "Listado de libros activos");
        model.addAttribute("libros", libros);
        model.addAttribute("pageRender", pageRender);
        return "libro/listado_libros";
    }

    @GetMapping("/libronuevo")
    public String libroFormNuevo(Model model) {
        model.addAttribute("titulo", "Nuevo Libro");
        model.addAttribute("accion", "Crear");
        model.addAttribute("libro", new Libro());
        model.addAttribute("listaeditoriales", libreriaService.listarTodasLasEditoriales());
        model.addAttribute("listaautores", libreriaService.listarTodosLosAutores());
        return "libro/nuevo_libro";
    }

    @PostMapping("/libroagregar")
    public String libroAgregar(@Valid @ModelAttribute("libro") Libro libro, BindingResult result,
            Model model, @RequestParam("file") MultipartFile caratula, RedirectAttributes flash, SessionStatus status) {

        String accion = (libro.getIdLibro() == null) ? "Crear" : "Modificar";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Libro");
            model.addAttribute("accion", accion);
            model.addAttribute("error", "Por favor corrige los errores");
            model.addAttribute("listaeditoriales", libreriaService.listarTodasLasEditoriales());
            model.addAttribute("listaautores", libreriaService.listarTodosLosAutores());

            return "libro/nuevo_libro";
        }

        if (!caratula.isEmpty()) {

            String nombreUnico = UUID.randomUUID().toString() + "_" + caratula.getOriginalFilename();

            Path rutaUploads = Paths.get("uploads").resolve(nombreUnico);
            Path rutaAbsoluta = rutaUploads.toAbsolutePath();

            log.info("rutaUploads: " + rutaUploads);
            log.info("rutaAbsoluta: " + rutaAbsoluta);

            try {
                Files.copy(caratula.getInputStream(), rutaAbsoluta);

                flash.addFlashAttribute("info", "На cargado correctamente la caractula del libro" +
                        nombreUnico + "]");

                libro.setCaratula(nombreUnico);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        

        libreriaService.actualizarLibro(libro);
        status.setComplete();
        flash.addFlashAttribute("success", accion + " libro, agregado exitosamente");
        return "redirect:/libreria/libroslistar";
    }

    @GetMapping("/libroconsultar/{id}")
    public String libroConsultar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Libro libro = libreriaService.buscarLibroPorId(id);

        if (libro == null) {
            flash.addFlashAttribute("error", "El libro a consultar no existe");
            return "redirect:/libreria/libroslistar";
        }
        model.addAttribute("titulo", "Consulta del libro: " + libro.getTitulo());
        model.addAttribute("libro", libro);

        return "libro/consulta_libro";
    }

    @GetMapping("/libroeliminar/{id}")
    public String libroEliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            libreriaService.eliminarLibroPorId(id);
            flash.addFlashAttribute("success", "Libro eliminado con éxito");
        }
        return "redirect:/libreria/libroslistar";
    }

    @GetMapping("/libromodificar/{id}")
    public String libroFormModificar(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash) {
        Libro libro = null;
        if (id > 0) {
            libro = libreriaService.buscarLibroPorId(id);
            if (libro == null) {
                flash.addFlashAttribute("error", "El libro a modificar no existe");
                return "redirect:/libreria/libroslistar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del libro debe ser positivo");
            return "redirect:/libreria/libroslistar";
        }
        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar Libro");
        model.addAttribute("libro", libro);
        model.addAttribute("listaeditoriales", libreriaService.listarTodasLasEditoriales());
        model.addAttribute("listaautores", libreriaService.listarTodosLosAutores());


        return "libro/nuevo_libro";
    }
}
