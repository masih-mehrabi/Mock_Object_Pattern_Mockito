package renting_vehicles;

import java.time.LocalDateTime;
import java.util.Set;

public interface ReservationService {

    Set<PEV> findAvailablePEVs(LocalDateTime from, LocalDateTime to);
}
