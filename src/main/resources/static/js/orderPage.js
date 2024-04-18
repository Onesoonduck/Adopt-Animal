function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
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
async function saveDeliveryInfo() {
    try {
        var sample6_detailAddress = document.getElementById("sample6_detailAddress").value;
        var sample6_postcode = document.getElementById('sample6_postcode').value; // 수정: data 객체의 zonecode를 가져오는 부분 수정
        var receiverName = document.getElementById("receiverName").value;
        var receiverPhoneNumber = document.getElementById("receiverPhoneNumber").value;
        var sample6_address = document.getElementById("sample6_address").value;

        var deliveryData = {
            address: {
                city: sample6_address,
                street: sample6_detailAddress,
                zipcode: sample6_postcode
            },
            receiverName: receiverName,
            receiverPhoneNumber: receiverPhoneNumber
        };

        const response = await axios.post('/order/delivery/api', deliveryData);
        console.log("Delivery information successfully saved.");
        return response.data.deliveryId; // deliveryId를 반환
    } catch (error) {
        console.error("Error occurred while saving delivery information:", error);
        throw error;
    }
}

document.addEventListener('DOMContentLoaded', function () {
    getCart();
    // 장바구니에서 상품 가져오기
    function getCart() {
        axios.get('/cart/items')
            .then(function (response) {
                const cartItems = response.data;

                const cartList = document.getElementById('cartList');
                cartList.innerHTML = '';

                cartItems.forEach(product => {
                    const row = document.createElement('tr');
                    const productName = product.productName;
                    const productPrice = parseFloat(product.productPrice);
                    const productStock = parseInt(product.productStock);
                    const shippingFee = 3000;
                    const productTotalPrice = productPrice * productStock + shippingFee;

                    cartList.innerHTML = `
                <div class="col-md-5 col-lg-4 order-md-last">
                    <h4 class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-warning">장바구니</span>
                    <span class="badge bg-warning rounded-pill"></span>
                </h4>
                <ul class="list-group mb-3">
                    <li class="list-group-item d-flex justify-content-between lh-sm">
                        <div>
                            <h6 class="my-0" id="productName">${productName}</h6>
                            <small class="text-muted" id="productStock">${productStock}</small>
                        </div>
                        <span class="text-muted" id="productPrice">${productPrice}</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between bg-light">
                        <div class="text-success">
                            <h6 class="my-0">배송비</h6>
                        </div>
                        <span class="text-success">${shippingFee}원</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>총합</span>
                        <strong id="totalPrice">${productTotalPrice}</strong>
                    </li>
                </ul>
                </div>
                `;
                    cartList.appendChild(row);
                });
            })
            .catch(function (error) {
                console.error(error);
            });
    }

});

// 주문 항목 추가
function addOrderItemsToCart(cartItems) {
    // 주문 항목 요청 객체 생성
    const orderItemRequests = cartItems.map(item => {
        return {
            productId: item.productId,
            price: item.productPrice,
            count: item.productStock
        };
    });

    // 주문 항목을 추가하는 POST 요청
    return axios.post('/orderItem/lists', orderItemRequests)
        .then(function(response) {
            console.log("주문 상품에 성공적으로 등록되었습니다.", response.data);

            // 추가된 주문 항목의 ID 추출
            const orderItemIds = response.data;

            // 각 주문 항목의 ID를 사용하여 주문 항목 조회하는 GET 요청
            const orderItemPromises = orderItemIds.map(orderItemId => {
                return axios.get(`/orderItem/api/${orderItemId}`);
            });

            // 모든 GET 요청이 완료되면 해당 주문 항목들을 반환
            return Promise.all(orderItemPromises)
                .then(responses => {
                    const orderItems = responses.map(response => response.data);
                    return orderItems;
                });
        })
        .catch(function(error) {
            console.error("주문 상품 등록에 실패했습니다.", error);
            throw error; // 오류 처리를 위해 에러를 다시 던집니다.
        });
}

// 주문 생성
// 주문 생성
    async function createOrder() {
        try {
            // 사용자 ID 가져오기
            const userId = await getUsersIdFromAPI();
            // 배송 정보 저장
            const deliveryId = await saveDeliveryInfo();
            // 장바구니 상품 정보 가져오기
            const cartItemId = await addOrderItemsToCart();

            // 주문 요청 객체 생성
            const orderRequest = {
                usersId: userId(),
                deliveryId: deliveryId(),
                orderItemIds: cartItemId()
            };

            // 주문 생성
            const response = await axios.post('/order', orderRequest);

            // 주문 완료 페이지로 리다이렉트
            console.log("주문이 성공적으로 생성되었습니다. 주문 ID:", response.data);
            location.href = '/static/order/orderComplete.html';
        } catch (error) {
            console.error("주문 생성 중 오류가 발생했습니다:", error);
            location.hred = '/static/order/orderFail.html';
        }
    }

// 사용자 ID 가져오기 API 호출
function getUsersIdFromAPI() {
    return axios.get('/users/api/usersId')
        .then((usersDto) => {
            return usersDto.data;
        })
        .catch((error) => {
            console.error("Error occurred while getting users ID:", error);
            throw error; // 오류 처리를 위해 에러를 다시 던집니다.
        });
}
