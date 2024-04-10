package com.DogFoot.adpotAnimal.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phoneNumber", nullable = false)
    private String receiverPhoneNumber;

    public void setOrder (Order order) {
        this.order = order;
    }

    // protected -> 다른곳에서 수정을 못하도록 지정
    protected void setAddress (Address address) {
        this.address = address;
    }

    protected void setReceiverName (String receiverName) {
        this.receiverName = receiverName;
    }

    protected void setReceiverPhoneNumber (String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public static Delivery createDelivery(Address address, String receiverName, String receiverPhoneNumber) {

        Delivery delivery = new Delivery();
        delivery.setAddress(address);
        delivery.setReceiverName(receiverName);
        delivery.setReceiverPhoneNumber(receiverPhoneNumber);

        return delivery;
    }

    public Delivery update(Address address, String receiverName, String receiverPhoneNumber) {
        this.address = address;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;

        return this;
    }

}
