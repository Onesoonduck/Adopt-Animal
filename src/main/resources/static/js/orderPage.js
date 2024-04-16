// 우편 주소 검색
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

// 배송 정보 저장
function saveDeliveryInfo() {
    // 입력된 값 가져오기
    var address = document.getElementById("sample6_address").value;
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var postcode = document.getElementById("sample6_postcode").value;
    var receiverName = document.getElementById("receiverName").value;
    var receiverPhoneNumber = document.getElementById("receiverPhoneNumber").value;

    // API 요청을 위한 데이터 구성
    var requestData = {
        address: {
            city: address,
            street: detailAddress,
            zipcode: postcode
        },
        receiverName: receiverName,
        receiverPhoneNumber: receiverPhoneNumber
    };

    // Axios를 사용하여 POST 요청 보내기
    axios.post('/api/addDelivery', requestData)
        .then(function (response) {
            // 성공적으로 저장된 경우
            console.log('Delivery info saved successfully:', response.data);
            alert('배송 정보가 성공적으로 저장되었습니다.');
        })
        .catch(function (error) {
            // 오류 발생 시
            console.error('Error saving delivery info:', error);
            alert('배송 정보를 저장하는 동안 오류가 발생했습니다.');
        });
}


// 장바구니에서 상품 ID 목록을 가져오는 함수
function fetchCartItems() {
    axios.get('/cart/items')
        .then(function (response) {
            // API 호출 성공 시 실행되는 부분
            const cartItems = response.data;
            const orderItemIds = cartItems.map(item => item.productId); // 장바구니에 담긴 각 상품의 ID 추출
            submitOrder(orderItemIds); // 주문하기 함수 호출하여 상품 ID 목록 전달
        })
        .catch(function (error) {
            // API 호출 실패 시 실행되는 부분
            console.error('Error fetching cart items:', error);
        });
}

let cartItemCount = 0;

// 장바구니 UI 업데이트하는 함수
function updateCartUI(cartItems) {
    const cartList = document.querySelector('.list-group');

    // 기존 리스트 삭제
    while (cartList.firstChild) {
        cartList.removeChild(cartList.firstChild);
    }

    // 새로운 아이템 추가
    cartItems.forEach(function (product) {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item d-flex justify-content-between lh-sm';
        listItem.innerHTML = `
                <div>
                    <h6 class="my-0">${product.productName}</h6>
                    <small class="text-muted">${product.productStock}</small>
                </div>
                <span class="text-muted">${product.productPrice}</span>
            `;
        cartList.appendChild(listItem);
    });

    // 총합 업데이트
    const totalPrice = cartItems.reduce((total, item) => total + (parseInt(item.productStock) * parseInt(item.productPrice)), 0);

// 총 가격에 배송비 추가
    const totalWithShipping = totalPrice + 3000; // 배송비가 3000원

    const totalElement = document.querySelector('.list-group li:last-child strong');
    totalElement.textContent = totalWithShipping;

    // 장바구니 아이템 수 업데이트
    const itemCountElement = document.querySelector('.badge.bg-warning.rounded-pill');
    itemCountElement.textContent = cartItemCount;
}

// orderItem 으로 추가
function createOrderItem(productId, count) {
    const requestData = {
        productId: productId,
        count: count
    };

    axios.post('/order/createOrderItem', requestData)
        .then(function (response) {
            // 주문 상품 생성 성공 시 실행되는 부분
            console.log('주문 상품이 성공적으로 생성되었습니다.');
            const orderItemId = response.data;
            // 필요한 추가 작업 수행 가능
        })
        .catch(function (error) {
            // 주문 상품 생성 실패 시 실행되는 부분
            console.error('주문 상품 생성에 실패하였습니다.', error);
        });
}

// 주문하기 버튼 누를 때 호출되는 함수
function submitOrder(orderItemIds) {
    // 입력된 정보 가져오기
    var receiverName = document.getElementById("receiverName").value;
    var receiverPhoneNumber = document.getElementById("receiverPhoneNumber").value;
    var email = document.getElementById("email").value;
    var address = document.getElementById("sample6_address").value;
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var postcode = document.getElementById("sample6_postcode").value;

    // API 요청을 위한 데이터 구성
    var requestData = {
        deliveryId: 1, // 배송 ID 또는 필요한 정보에 따라 수정
        orderItemId: orderItemIds // 주문 상품 ID 목록
        // 필요한 경우 주문 생성에 필요한 다른 정보들도 추가할 수 있습니다.
    };

    // Axios를 사용하여 POST 요청 보내기
    axios.post('/order', requestData)
        .then(function (response) {
            // 주문 성공 시 처리
            console.log('Order created successfully:', response.data);
            alert('주문이 성공적으로 생성되었습니다.');
            location.href = "/static/order/orderComplete";
            // 주문 생성 후 추가적인 작업을 수행할 수 있습니다.
        })
        .catch(function (error) {
            // 오류 발생 시 처리
            console.error('Error creating order:', error);
            alert('주문을 생성하는 동안 오류가 발생했습니다.');
            location.href="/redicrect:/orderPage.html";
        });
}

// 페이지 로드 시 장바구니 아이템 가져오기
document.addEventListener('DOMContentLoaded', function () {
    fetchCartItems();
});