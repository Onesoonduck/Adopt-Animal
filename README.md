### 유기견 후원 쇼핑몰 사이트

## 개요
전 세계에 불쌍하게 버려지는 유기동물을 위해 후원하고 동물들에게 도움이 되는 물건을 판매할 수 있는 사이트입니다.

기본적으로 Java Spring Boot 기반의 프로젝트이며 프론트파트로는 Javascript, html, css 를 포함하고 있습니다.

개발 과정에서는 GitLab을 활용하였고 이는 미러링을 통해 프로젝트를 가져온 것입니다.

## 개발과정
![image](https://github.com/user-attachments/assets/be97ef3a-7e28-4cd3-819d-d0881df49a5b)

1. 프로젝트 설계하기
2. ERD 설계하기
3. 와이어프레임 제작하기
4. 코드 컨벤션 정하기
5. 코드 구현하기
6. 코드 리뷰받고 수정하기
7. VM 배포하기

# 사용한 기술 스택
- 백엔드 파트
Java, Spring Boot, MySQL, H2, Spring Security, JWT, Daum postcode API

- 프론트 파트
html, CSS, Javascript, Bootstrap

# ERD
![image](https://github.com/user-attachments/assets/a9db0baa-8133-4bc9-a6c8-5837f28aa601)

# 와이어프레임
![image](https://github.com/user-attachments/assets/e9f70e9a-0b52-46fe-a874-eb6aa29d8e3b)

## 내가 개발한 부분
# Order(주문) 도메인
- Java Spring Boot 프레임워크와 JPA를 사용한 CRUD
- 주문 구조 -> 회원과 주문은 다대다의 관계를 가질 수 있기에 주문 시 상품을 OrderItem에 저장을 하고 다대일의 관계로 매핑지어 연결
- 주문 시 OrderItem.id, member.id, delivery.id(주문 이후 배송 상태를 관리하기 위해서 테이블을 따로 저장)을 Order에 저장
- 이후 Order 조회를 통해 주문 내역 조회
- 주문 도중 에러나 자의에 의해 주문을 취소할 시 데이터가 변하면 안되기에 Transaction을 기반으로 서비스 로직 구현
- Daum postcode API를 적극 활용하여 효율적으로 주소 검색

  # 페이지
  - 바닐라Js를 기반으로 한 스토어, 주문 페이지 제작
  - axios API 호출을 기반으로 Json 형식 데이터 화면 호출
![image](https://github.com/user-attachments/assets/a01c1369-4588-4d9e-a22e-52498ab648bf)
![image](https://github.com/user-attachments/assets/6c462bbc-b826-407a-a003-39c3585ef508)
![image](https://github.com/user-attachments/assets/afa5694c-af0f-43da-b041-0ac6136e7fe1)
![image](https://github.com/user-attachments/assets/23c154ab-35f5-415b-b986-50a6fd8b1cce)


## 기억에 남는 코드
```
@PostMapping("")
public ResponseEntity<Long> addOrder(@RequestBody OrderRequest request) {

    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Users users = userDetails.getUser();

    Delivery delivery = deliveryService.findById(request.getDeliveryId());

    List<OrderItem> orderItems = new ArrayList<>();

    for (Long orderItemId : request.getOrderItemId()) {
        orderItems.add(orderItemService.findById(orderItemId));
    }

    Order order = orderService.create(users, delivery, orderItems);

    return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
}


@GetMapping("")
public ResponseEntity<List<OrderResponse>> findOrders(@RequestParam Long usersId) {
    List<OrderResponse> orderResponses = orderService.findAllByUsersId(usersId)
        .stream()
        .map(OrderResponse::new)
        .toList();

    return ResponseEntity.ok().body(orderResponses);
}
```
동시에 member, Delivery, OrderItem을 가지고 와서 객체를 생성하는 복잡한 로직인 만큼 에러도 많았고 많은 걸 얻어갈 수 있었던 코드

## 트러블 슈팅
- 생성시간인 CreatedAt 컬럼이 계속해서 NULL로 반환이 되는 상황
- Audit 기능을 사용해서 시간에 대한 자동으로 넣어주는 기능을 사용하여 해결
- @EnableJpaAuditing 어노테이션을 추가해줌으로써 자동으로 시간을 매핑하여 데이터베이스에 값을 넣어주었습니다.




