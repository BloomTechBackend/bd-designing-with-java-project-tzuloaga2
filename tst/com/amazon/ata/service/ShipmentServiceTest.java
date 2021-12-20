package com.amazon.ata.service;

import com.amazon.ata.activity.PrepareShipmentActivity;
import com.amazon.ata.activity.PrepareShipmentRequest;
import com.amazon.ata.cost.MonetaryCostStrategy;
import com.amazon.ata.dao.PackagingDAO;
import com.amazon.ata.datastore.PackagingDatastore;
import com.amazon.ata.exceptions.NoPackagingFitsItemException;
import com.amazon.ata.exceptions.UnknownFulfillmentCenterException;
import com.amazon.ata.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

class ShipmentServiceTest {

    private Item smallItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1))
            .withWidth(BigDecimal.valueOf(1))
            .withLength(BigDecimal.valueOf(1))
            .withAsin("abcde")
            .build();

    private Item largeItem = Item.builder()
            .withHeight(BigDecimal.valueOf(1000))
            .withWidth(BigDecimal.valueOf(1000))
            .withLength(BigDecimal.valueOf(1000))
            .withAsin("12345")
            .build();

    private FulfillmentCenter existentFC = new FulfillmentCenter("ABE2");
    private FulfillmentCenter nonExistentFC = new FulfillmentCenter("NonExistentFC");
    private Packaging testingmock = new PolyBag(Material.LAMINATED_PLASTIC, BigDecimal.valueOf(100));


    @InjectMocks
    private ShipmentService shipmentService;

   @Mock
   PackagingDAO mock;

   @Mock
   MonetaryCostStrategy mocking;

   @BeforeEach
   void setUp() { openMocks (this);
   }


    @Test
    void findBestShipmentOption_existentFCAndItemCanFit_returnsShipmentOption() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN
        shipmentService = new ShipmentService(mock, new MonetaryCostStrategy());
        ShipmentOption ship = ShipmentOption.builder().withFulfillmentCenter(existentFC).withItem(smallItem).withPackaging(testingmock).build();
        List<ShipmentOption> shipmentOption = new ArrayList<>();
        shipmentOption.add(ship);
        when(mock.findShipmentOptions(smallItem, existentFC)).thenReturn(shipmentOption);

        //WHEN
        ShipmentOption newname = shipmentService.findShipmentOption(smallItem, existentFC);


        // THEN
        assertEquals(newname.getClass(), ShipmentOption.class, "This findShipmentOption should return shipmentOption object");


    }

    @Test
    void findBestShipmentOption_existentFCAndItemCannotFit_returnsShipmentOptionwithNullPackaging() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN
        shipmentService = new ShipmentService(mock, new MonetaryCostStrategy());
        ShipmentOption ship = ShipmentOption.builder().withFulfillmentCenter(existentFC).withItem(largeItem).withPackaging(null).build();
        when(mock.findShipmentOptions(largeItem, existentFC)).thenThrow(new NoPackagingFitsItemException());

        //WHEN
        ShipmentOption newname = shipmentService.findShipmentOption(largeItem, existentFC);

        // THEN
        assertEquals(newname.getClass(), ship.getClass(),"This findBestShipmentOption should equal each other");
        assertEquals(newname.getFulfillmentCenter(), ship.getFulfillmentCenter(), "This should also equal each other");
        assertEquals(newname.getItem(),ship.getItem(),"Size should equal");
        assertNull(newname.getPackaging(),"Should return Null Packaging");

    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCanFit_returnsShipmentOptionNoFcbutItemFits() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException {
        // GIVEN & WHEN
        shipmentService = new ShipmentService(mock, new MonetaryCostStrategy());
        when(mock.findShipmentOptions(smallItem,nonExistentFC)).thenThrow(new UnknownFulfillmentCenterException());

        // THEN
        assertThrows(RuntimeException.class, () -> {
            shipmentService.findShipmentOption(smallItem, nonExistentFC);
        }, "Fc not available throws exception");
    }

    @Test
    void findBestShipmentOption_nonExistentFCAndItemCannotFit_returnsShipmentOptionwithNeither() throws UnknownFulfillmentCenterException, NoPackagingFitsItemException{
        // GIVEN & WHEN
        shipmentService = new ShipmentService(mock, new MonetaryCostStrategy());
        when(mock.findShipmentOptions(largeItem,nonExistentFC)).thenThrow(new UnknownFulfillmentCenterException());

        // THEN
        assertThrows(RuntimeException.class, () -> {
            shipmentService.findShipmentOption(largeItem, nonExistentFC);
        }, "Fc not available and item is too large, so throws exception");
    }

}