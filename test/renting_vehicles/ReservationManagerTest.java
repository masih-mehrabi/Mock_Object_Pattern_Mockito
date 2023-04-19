package renting_vehicles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/*
 * TODO:
 *  To use Mockito in your tests, use @ExtendWith
 *  with the MockitoExtension class.
 */
@ExtendWith(MockitoExtension.class)
class ReservationManagerTest {

    /**
     * You need two instances of LocalDateTime to write your tests.
     * Use these provided instances to make your code more readable.
     */
    private final LocalDateTime from = LocalDateTime.of(2020, 10, 10, 10, 10);
    private final LocalDateTime to = LocalDateTime.of(2020, 10, 10, 10, 11);

    /*
     * TODO:
     *  1. Declare a Rider and ReservationService Mock that will be
     *     used by the SUT ReservationManager
     *  2. Inject the Mocks into the SUT using the method discussed
     *     in the lecture
     *  3. Implement the test as described in the problem statement
     */
    
    @Mock
    private ReservationService reservationService;
    @Mock
    private Rider rider;
    
    @Spy
    private ReservationManager reservationManagerSpy;
    
    @Captor
    private ArgumentCaptor<PEV> pevCaptor;
    
 
    
    @InjectMocks
    private ReservationManager reservationManager;
    
    ReservationManagerTest() {
    }
    
    @Test
    void testLookupAvailablePEVsReturnsValidSet() {
        PEVSearchCriteria pevSearchCriteria = new PEVSearchCriteria(rider);
        
        Set<PEV> pevSet = new HashSet<>();
        pevSet.add(new EBike(100, "200"));
        pevSet = pevSet.stream().filter(pevSearchCriteria::isPEVAllowed).collect(Collectors.toSet());
        
        
        assertEquals(pevSet, reservationManager.lookupAvailablePEVsForTimeFrame(from, to));
        verify(reservationService).findAvailablePEVs(from, to);
    }

    /*
     * TODO:
     *  1. Declare a ReservationManager Spy that stubs the implementation
     *     of lookupAvailablePEVsForTimeFrame()
     *  2. Declare a PEV ArgumentCaptor that is used to check if the
     *     returned PEV matches the one rented by the reserver
     *  3. Implement the test as described in the problem statement
     */
    
 
    @Test
    void testReserveFittingPEVsReturnsRentedPEV() {
        when(rider.getDriversLicense()).thenReturn(new DriversLicense(from.plusYears(2), "rider"));
        when(rider.hasHelmet()).thenReturn(true);
    
        PEVSearchCriteria pevSearchCriteria = new PEVSearchCriteria(rider);
    
        Set<PEV> pevSet = new HashSet<>();
        EBike eBike = new EBike(200, "200");
    
        pevSet.add(eBike);
        
        pevSet = pevSet.stream().filter(pevSearchCriteria::isPEVAllowed).collect(Collectors.toSet());
    
        
        reservationManager.setRider(rider);
        reservationManager.setReservationService(reservationService);
    
        when(reservationService.findAvailablePEVs(from, to)).thenReturn(pevSet);
        reservationService.findAvailablePEVs(from, to);
        
        PEV pev1 = reservationManager.reserveFittingPEV(from, to);
        verify(rider).rent(pevCaptor.capture(), eq(from), eq(to));
        assertEquals(pevCaptor.getValue(), pev1);
        assertTrue(pevSet.contains(pev1));
    }
    
    class TestMock {
        public String processString(String input) {
            if (input == null || input.isEmpty()) {
                return "Input cannot be empty";
            }
        
            input = input.trim();
        
            String output = input.replace(" ", "-");
        
            return output;
        }
    }
    
    @Spy
    private TestMock testMock;
    @InjectMocks
    TestMock testMockReal = new TestMock();
    
    @Test
    void setTesMock() {
        String string = "How are you";
        String string2 = " hello world";
        
        String expectedOutPutNullEmpty = "Input cannot be empty";
        String expectedOutPutHello = "hello-world";
        String expectedOutputHow = "How-are-you";
    
        when(testMock.processString(null)).thenReturn(expectedOutPutNullEmpty);
        when(testMock.processString("")).thenReturn(expectedOutPutNullEmpty);
        when(testMock.processString(string)).thenReturn(expectedOutputHow);
        when(testMock.processString(string2)).thenReturn(expectedOutPutHello);
        
        String outPutEmpty = testMockReal.processString("");
        System.out.println(outPutEmpty);
//        String outPutNull = testMockReal.processString(null);
        String outputHello = testMockReal.processString(string2);
        System.out.println(outputHello);
        String outputHow = testMockReal.processString(string);
        
//        assertEquals(outPutEmpty, expectedOutPutNullEmpty);
        assertEquals(expectedOutPutHello, outputHello);
        assertEquals(expectedOutputHow, outputHow);
//        assertEquals(outPutNull, expectedOutPutNullEmpty);
        
//        verify(testMock).processString(null);
//        verify(testMock).processString("");
        verify(testMock).processString(string);
        verify(testMock).processString(string2);
    
    
    }
    
}
