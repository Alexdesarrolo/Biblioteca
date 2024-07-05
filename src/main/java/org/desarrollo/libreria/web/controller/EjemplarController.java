package org.desarrollo.libreria.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.desarrollo.libreria.web.model.entity.Ejemplar;
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
@SessionAttributes("ejemplar")
public class EjemplarController {
	@Autowired
    private LibreriaService libreriaService;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/ejemplareslistar")
    public String EjemplarListar(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model) {
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Ejemplar> ejemplares = libreriaService.buscarEjemlaresTodos(pagina);

        PageRender<Ejemplar> pageRender = new PageRender<>("/libreria/ejemplareslistar", ejemplares);

        model.addAttribute("titulo", "Listado de ejemplares activos");
        model.addAttribute("ejemplares", ejemplares);
        model.addAttribute("pageRender", pageRender);
        return "ejemplar/listado_ejemplares";
    }

    @GetMapping("/ejemplarnuevo")
    public String ejemplarFormNuevo(Model model) {
        model.addAttribute("titulo", "Nuevo ejemplar");
        model.addAttribute("accion", "Crear");
        model.addAttribute("ejemplar", new Ejemplar());
        model.addAttribute("listalibros", libreriaService.buscarLibrosTodos());
        return "ejemplar/nuevo_ejemplar";
    }

    @PostMapping("/ejemplaragregar")
    public String ejemplarAgregar(@Valid @ModelAttribute("ejemplar") Ejemplar ejemplar, BindingResult result,
            Model model, RedirectAttributes flash, SessionStatus status) {

        String accion = (ejemplar.getId() == null) ? "Crear" : "Modificar";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Ejemplar");
            model.addAttribute("accion", accion);
            model.addAttribute("error", "Por favor corrige los errores");
            model.addAttribute("listalibros", libreriaService.buscarLibrosTodos());

            return "ejemplar/nuevo_ejemplar";
        }

        ejemplar.setNombreAutor(ejemplar.getLibro().getAutores().get(0).getNombre() + " " + ejemplar.getLibro().getAutores().get(0).getApellido());
        ejemplar.setTitulo(ejemplar.getLibro().getTitulo());
        System.out.println("_-----------------: " + ejemplar.getLibro().getTitulo() );
        libreriaService.actualizarEjemplar(ejemplar);
        status.setComplete();
        flash.addFlashAttribute("success", accion + " ejemplar, agregado exitosamente");
        return "redirect:/libreria/ejemplareslistar";
    }

    @GetMapping("/ejemplarconsultar/{id}")
    public String ejemplarConsultar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Ejemplar ejemplar = libreriaService.buscarEjemplarPorId(id);

        if (ejemplar == null) {
            flash.addFlashAttribute("error", "El ejemplar a consultar no existe");
            return "redirect:/libreria/ejemplareslistar";
        }
        model.addAttribute("titulo", "Consulta del ejemplar: " + ejemplar.getLibro().getTitulo());
        model.addAttribute("ejemplar", ejemplar);

        return "ejemplar/consulta_ejemplar";
    }

    @GetMapping("/ejemplareliminar/{id}")
    public String ejemplarEliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            libreriaService.eliminarEjemplarPorId(id);
            flash.addFlashAttribute("success", "ejemplar eliminado con éxito");
        }
        return "redirect:/libreria/ejemplareslistar";
    }

    @GetMapping("/ejemplarmodificar/{id}")
    public String ejemplarFormModificar(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash) {
        Ejemplar ejemplar = null;
        if (id > 0) {
            ejemplar = libreriaService.buscarEjemplarPorId(id);
            if (ejemplar == null) {
                flash.addFlashAttribute("error", "El ejemplar a modificar no existe");
                return "redirect:/libreria/ejemplareslistar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del ejemplar debe ser positivo");
            return "redirect:/libreria/ejemplareslistar";
        }
        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar Ejemplar");
        model.addAttribute("ejemplar", ejemplar);
        model.addAttribute("listalibros", libreriaService.buscarLibrosTodos());


        return "ejemplar/nuevo_ejemplar";
    }
	
}
