package com.DogFoot.adpotAnimal.order;

import com.DogFoot.adpotAnimal.order.entity.Address;
import com.DogFoot.adpotAnimal.order.entity.Delivery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class DeliveryTest {

    private Address address;

    @BeforeAll
    public static void createAddress() {
        String zipCode = "12345";
        String street = "서울로 123";
        String detail = "상세주소";

        Address address = new Address(zipCode, street, detail);
    }

    @DisplayName("배송지 객체 생성 테스트")
    @Test
    public void addDeliveryTest() {
        // given
        String receiverName = "테스터";
        String receiverPhoneNumber = "010-0000-0000";

        // when
        Delivery delivery = Delivery.createDelivery(address, receiverName, receiverPhoneNumber);

        // then
        assertEquals(delivery.getAddress(), address);
        assertEquals(delivery.getReceiverName(), receiverName);
        assertEquals(delivery.getReceiverPhoneNumber(), receiverPhoneNumber);
    }

    @DisplayName("배송지 객체 수정 테스트")
    @Test
    public void updateDeliveryTest() {
        // given
        String receiverName = "테스터";
        String receiverPhone = "010-0000-0000";
        Delivery delivery = Delivery.createDelivery(address, receiverName, receiverPhone);

        // when
        String changeReceiverName = "수정됨";
        String changeReceiverPhone = "010-1111-1111";
        delivery.update(address, changeReceiverName, changeReceiverPhone);

        // then
        assertEquals(delivery.getAddress(), address);
        assertEquals(delivery.getReceiverName(), changeReceiverName);
        assertEquals(delivery.getReceiverPhoneNumber(), changeReceiverPhone);
    }


}
