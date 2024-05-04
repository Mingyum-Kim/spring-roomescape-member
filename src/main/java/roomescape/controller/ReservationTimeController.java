package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.service.ReservationTimeService;

@Controller
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveTime(
            @RequestBody ReservationTimeRequest reservationTimeRequest
    ) {
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.save(reservationTimeRequest);
        return ResponseEntity.created(URI.create("/times/" + reservationTimeResponse.timeId()))
                .body(reservationTimeResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getTimes() {
        return ResponseEntity.ok(reservationTimeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTime(@PathVariable("id") long id) {
        reservationTimeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}