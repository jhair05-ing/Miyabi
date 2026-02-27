package com.miyabi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.miyabi.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller 
public class HomeController {

	@Autowired
    private ReservationService reservationService;
	
    // Ruta para la página principal 
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    @GetMapping("/facilities")
    public String facilities() {
        return "facilities"; 
    }

    @GetMapping("/cuisine")
    public String cuisine() {
        return "cuisine"; 
    }
 // 5. AGREGA ESTA RUTA NUEVA
    @GetMapping("/reservas")
    public String reservas(Model model) {
        try {
            model.addAttribute("reservations", reservationService.findAll());
            return "reservas";
        } catch (Exception e) {
            System.err.println("ERROR AL CARGAR RESERVAS: " + e.getMessage());
            e.printStackTrace();
            return "error"; // Si tienes una página de error, o solo deja que explote para ver la consola
        }
    }
}    