package org.desarrollo.libreria.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.desarrollo.libreria.web.model.entity.Ejemplar;
import org.desarrollo.libreria.web.model.entity.Libro;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/libreria")
@SessionAttributes("prestamo")
public class PrestamoController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    Date fechaActual = new Date();

    @Autowired
    private LibreriaService libreriaService;

    @GetMapping("/prestamoslistar")
    public String prestamosListar(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model) {
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Prestamo> prestamos = libreriaService.buscarPrestamosTodos(pagina);

        PageRender<Prestamo> pageRender = new PageRender<>("/libreria/prestamoslistar", prestamos);

        model.addAttribute("titulo", "Listado de prestamos activos");
        model.addAttribute("prestamos", prestamos);
        model.addAttribute("pageRender", pageRender);
        return "prestamo/listado_prestamos";
    }

    @GetMapping("/prestamonuevo/{id}")
    public String prestamoNuevo(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = libreriaService.buscarUsuarioPorId(id);

        if (usuario == null) {
            flash.addFlashAttribute("error", "El usuario no existe en la base de datos !!");
            return "redirect:/libreria/usuarioslistar";
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);

        model.addAttribute("titulo", "Nuevo Préstamo");
        model.addAttribute("btn_accion", "Guardar Préstamo");
        model.addAttribute("prestamo", prestamo);

        return "prestamo/prestamo_nuevo";
    }

    @GetMapping(value = "/cargarlibros/{term}", produces = "application/json")
    public @ResponseBody List<Ejemplar> buscaLibros(@PathVariable(name = "term") String term) {
        return libreriaService.buscarLibrosPorTitulo(term);
    }

    @PostMapping("/agregarprestamo")
    public String guardarPrestamo(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
            BindingResult result, Model model,
            @RequestParam(name = "detalle_id[]", required = false) Long[] ejemplarId,
            RedirectAttributes flash, SessionStatus status) {
        prestamo.setFechaAlquiler(fechaActual);
        System.out.println("----------------------: " + prestamo.getFechaAlquiler());

        /*
         * // Crear objetos Calendar y establecer las fechas
         * Calendar cal1 = Calendar.getInstance();
         * cal1.setTime(prestamo.getFechaAlquiler());
         * 
         * Calendar cal2 = Calendar.getInstance();
         * cal2.setTime(prestamo.getFechaDevolucion());
         * 
         * // Obtener la diferencia entre las fechas
         * long diferencia = cal2.getTimeInMillis() - cal1.getTimeInMillis();
         * //Convertimos las dos fechas en milisegundos
         * 
         * // Calcular el resto de días
         * long diasRestantes = (diferencia+(24 * 60 * 60 * 1000)) / (24 * 60 * 60 *
         * 1000); //calcula el número de días restantes a partir de la diferencia en
         * milisegundos entre dos fechas
         * prestamo.setDiferenciaFechas(diasRestantes);
         */

        // System.out.println("----------------------: " + diasRestantes);

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Préstamo");
            model.addAttribute("btn_accion", "Guardar Préstamo");
            model.addAttribute("error", "Los campos son obligatorios");

            return "prestamo/prestamo_nuevo";
        }

        if (ejemplarId == null || ejemplarId.length == 0) {
            model.addAttribute("titulo", "Nuevo Préstamo");
            model.addAttribute("btn_accion", "Guardar Préstamo");
            model.addAttribute("error", "El préstamo debe tener al menos un ejemplar");
            return "prestamo/prestamo_nuevo";
        }
        System.out.println("------------------------ID Ptesr: " + prestamo.getIdPrestamo());
        for (int i = 0; i < ejemplarId.length; i++) {
            prestamo.setIdPrestamo(null);// Establecemos el id en +1, para que en la siguiente iteración agregue el
                                         // libro prestado y no se quede modificando

            Ejemplar ejemplar = libreriaService.buscarEjemplarPorId(ejemplarId[i]);
            ejemplar.setEstado(true);
            // prestamo.addEjemplar(ejemplar);
            // log.info("------------------------ID: " + ejemplarId[i]);
            System.out.println("------------------------ID: " + ejemplarId[i]);
            // prestamo.setEjemplar(ejemplar);
            // libreriaService.guardarPrestamo(prestamo);

            // Vericamos si hay existencia disponible del libro
            if (ejemplar.getNumeroEjemplar() > 0) {
                ejemplar.setNumeroEjemplar(ejemplar.getNumeroEjemplar() - 1);
                ejemplar.setEstado(false);
            } else {
                model.addAttribute("titulo", "Nuevo Préstamo");
                model.addAttribute("btn_accion", "Guardar Préstamo");
                model.addAttribute("error", "No hay libros disponibles para prestar '" + ejemplar.getTitulo() + "'");
                return "prestamo/prestamo_nuevo";
            }

            // descontamos 1 al ejemplar

            libreriaService
                    .guardarPrestamo(new Prestamo(prestamo.getEstado(), new Date(), prestamo.getFechaDevolucion(),
                            prestamo.getObservacion(), prestamo.diferenciaDias(), prestamo.getUsuario(), ejemplar));

            // Verificar si la fecha de devolución ya ha pasado
            /*
             * if (prestamo.getFechaDevolucion().before(fechaActual)) {
             * // Incrementar el contador de ejemplares vencidos del usuario
             * int ejemplaresVencidosActualizados =
             * prestamo.getUsuario().getEjemplaresVencidos() + 1;
             * prestamo.getUsuario().setEjemplaresVencidos(ejemplaresVencidosActualizados);
             * }
             */

        }
        // prestamo.setFechaAlquiler(fechaActual);
        // libreriaService.guardarPrestamo(prestamo);
        status.setComplete();

        flash.addFlashAttribute("success", "Préstamo guardado correctamente");

        return "redirect:/libreria/usuarioconsultar/" + prestamo.getUsuario().getId();
    }

    @GetMapping("/prestamoconsultar/{idPrestamo}")
    public String prestamoConsultar(@PathVariable(value = "idPrestamo") Long idPrestamo, Model model,
            RedirectAttributes flash) {
        Prestamo prestamo = libreriaService.buscarPrestamoPorIdConUsuarioConEjemplarConLibro(idPrestamo);
        if (prestamo == null) {
            flash.addFlashAttribute("error", "El préstamo no está registrado en la base de datos.");
            return "redirect:/libreria/usuarioslistar/";
        }

        model.addAttribute("prestamo", prestamo);
        model.addAttribute("titulo", "Prestamo No. " + prestamo.getIdPrestamo() + " - " + prestamo.getUsuario().getNombre());

        // int numEjemplaresPrestados = prestamo.getEjemplar();

        int numEjemplaresVencidos = prestamo.getUsuario().getEjemplaresVencidos();

        // model.addAttribute("numEjemplaresPrestados", numEjemplaresPrestados);
        model.addAttribute("numEjemplaresVencidos", numEjemplaresVencidos);

        return "prestamo/consulta_prestamo";
    }

    @GetMapping("/prestamoeliminar/{idPrestamo}")
    public String prestamoEliminar(@PathVariable(value = "idPrestamo") Long idPrestamo, RedirectAttributes flash) {
        Prestamo prestamo = libreriaService.buscarPrestamoPorIdPrestamo(idPrestamo);
        prestamo.getEjemplar().setNumeroEjemplar(prestamo.getEjemplar().getNumeroEjemplar() + 1); //Sumamos 1 a la cantidad de ese ejemplar cuando se elimina el prestamo

        if (prestamo != null) {
        	
            libreriaService.eliminarPrestamo(idPrestamo);
            flash.addFlashAttribute("success", "El prestamo número" + prestamo.getIdPrestamo() + " fue eliminada");
        } else {
            flash.addFlashAttribute("error", "El prestamo con número " + idPrestamo + "no existe");
        }
        return "redirect:/libreria/usuarioconsultar/" + prestamo.getUsuario().getId();
    }
}
