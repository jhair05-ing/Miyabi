package com.miyabi.controller;

import com.miyabi.service.ReportService;
import com.miyabi.service.ReservationService;
import com.miyabi.models.Reservation;
import com.miyabi.models.Guest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boleta-digital")
public class BoletaEmitirController {

    private final ReportService reportService;
    private final ReservationService reservationService;

    public BoletaEmitirController(ReportService reportService, ReservationService reservationService) {
        this.reportService = reportService;
        this.reservationService = reservationService;
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargar(@PathVariable Integer id) {
        try {
            // 1. Buscamos la reserva por su ID
            Reservation res = reservationService.findById(id);
            
            if (res == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Extraemos los datos usando los nombres EXACTOS de tus modelos
            Guest huesped = res.getGuest();
            String nombreCompleto = huesped.getNames() + " " + huesped.getSurnames();
            String fecha = res.getReservationDate().toString();
            String total = String.valueOf(res.getTotalPay());

            // 3. Generamos el PDF con el servicio que ya tienes
            byte[] pdf = reportService.generateBoleta(nombreCompleto, fecha, total);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Esto hace que se abra en el navegador con un nombre de archivo limpio
            headers.setContentDispositionFormData("inline", "Boleta_Reserva_" + id + ".pdf");

            return ResponseEntity.ok().headers(headers).body(pdf);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}