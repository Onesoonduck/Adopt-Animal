const header = new Headers();
header.append('Content-Type', 'application/json');

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
            }

            // 참고항목이 있는 경우 괄호로 묶어준다.
            if (extraAddr !== '') {
                extraAddr = ' (' + extraAddr + ')';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr + extraAddr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}


// 배송 정보 저장
function saveDeliveryInfo() {
    // 우편번호와 주소 정보를 해당 필드에 넣는다.
    var sample6_detailAddress = document.getElementById("sample6_detailAddress").value;
    var sample6_postcode = document.getElementById('sample6_postcode').value = data.zonecode;
    var receiverName = document.getElementById("receiverName").value;
    var receiverPhoneNumber = document.getElementById("receiverPhoneNumber").value;
    var sample6_address = document.getElementById("sample6_address").value;

    // API에 데이터 전송
    var deliveryData = {
        address: {
            city: sample6_address, // 시/도 정보
            street: sample6_detailAddress, // 시/군/구 정보
            zipcode: sample6_postcode // 우편번호 정보
        },
        receiverName: receiverName,
        receiverPhoneNumber: receiverPhoneNumber
    };

    // axios를 사용하여 API에 POST 요청 보내기
    axios.post('/order/delivery/api', deliveryData)
        .then(function(response) {
            // 성공적으로 저장되었을 때의 처리
            console.log("Delivery information successfully saved.");
        })
        .catch(function(error) {
            // 오류 발생 시 처리
            console.error("Error occurred while saving delivery information:", error);
        });
}

// 장바구니에서 상품 가져오기
function getCart() {
    axios.get('/cart/items')
        .then(function(response) {
            const cartItems = response.data;

            // 장바구니에 담긴 상품 개수 표시
            const cartItemCountElement = document.getElementById('cartItemCount');
            cartItemCountElement.textContent = cartItems.length;

            // 장바구니 리스트 테이블
            const cartList = document.getElementById('cartList');
            cartList.innerHTML = ''; // 이전 내용 초기화

            // 장바구니에 담긴 상품이 없을 경우
            if (cartItems.length === 0) {
                let tr = document.createElement('tr');
                let td = document.createElement('td');
                td.colSpan = 5;
                td.innerText = '장바구니에 담긴 상품이 없습니다.';
                tr.appendChild(td);
                cartList.appendChild(tr);
                return;
            }

            // 장바구니에 담긴 상품이 있을 경우
            cartItems.forEach(item => {
                let li = document.createElement('li');
                li.className = 'list-group-item d-flex justify-content-between lh-sm';

                // 상품 이름 및 개수
                let divLeft = document.createElement('div');
                let h6 = document.createElement('h6');
                h6.className = 'my-0';
                h6.textContent = item.productName;
                let small = document.createElement('small');
                small.className = 'text-muted';
                small.textContent = '개수: ' + item.productStock
                divLeft.appendChild(h6);
                divLeft.appendChild(small);

                // 상품 가격
                let spanPrice = document.createElement('span');
                spanPrice.className = 'text-muted';
                spanPrice.textContent = item.productPrice;

                li.appendChild(divLeft);
                li.appendChild(spanPrice);

                cartList.appendChild(li);
            });

            // 총합 가격 업데이트
            updateTotalPrice(cartItems);
        })
        .catch(function(error) {
            console.error(error);
        });
}

// 총합 가격 업데이트
function updateTotalPrice(cartItems) {
    let totalPrice = 0;
    cartItems.forEach(item => {
        totalPrice += item.productPrice * item.productStock + 3000;
    });
    document.getElementById('totalPrice').textContent = totalPrice;
}

// orderItem 추가
function addOrderItemsToCart(cartItems) {
    const orderItemRequests = cartItems.map(item => {
        return {
            productId: item.productId, // 상품 ID
            price: item.productPrice, // 상품 가격
            count: item.productStock // 상품 수량
        };
    });

    axios.post('/orderItem/lists', orderItemRequests)
        .then(function(response) {
            // 주문 목록에 상품 추가 성공
            console.log("Order items added successfully:", response.data);
        })
        .catch(function(error) {
            // 오류 발생 시 처리
            console.error("Error occurred while adding order items:", error);
        });
}

// 주문 생성
function createOrder(deliveryId, orderItemIds) {
    const orderRequest = {
        deliveryId: deliveryId,
        orderItemIds: orderItemIds
    };

    axios.post('/order', orderRequest)
        .then(function(response) {
            console.log("Order created successfully. Order ID:", response.data);
            window.location.href = '/order/orderComplete.html';
        })
        .catch(function(error) {
            console.error("Error occurred while creating order:", error);
        });
}
