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


// 장바구니 상품 정보 추가
// URL의 쿼리 매개변수를 객체로 추출하는 함수
function getQueryParams() {
    const params = {};
    const queryString = window.location.search.slice(1); // '?' 제거
    const pairs = queryString.split('&'); // 각 쿼리 스트링 항목 분리

    pairs.forEach(pair => {
        const [key, value] = pair.split('='); // 키와 값 분리
        params[decodeURIComponent(key)] = decodeURIComponent(value || ''); // 디코드 후 객체에 할당
    });

    return params;
}

// 쿼리 매개변수를 가져와서 productName, productPrice, productStock를 추출
async function addCartFromProduct() {
    try {
        const queryParams = getQueryParams();
        const productName = queryParams['productName'];
        const productPrice = queryParams['productPrice'];
        const productStock = queryParams['productStock'];
        const shippingFee = 3000;
        const TotalPrice = parseInt(productPrice) * parseInt(productStock) + shippingFee;

        const cartList = document.getElementById('cartList');
        // cartList.innerHTML = 'cartList';
        // cartList.innerHTML = `
        cartList.innerHTML = '';
        const html = `
                    <h4 class="d-flex justify-content-between align-items-center mb-3">
                    <span class="text-warning">장바구니</span>
                    <span class="badge bg-warning rounded-pill"></span>
                </h4>
                <ul class="list-group mb-3">
                    <li class="list-group-item d-flex justify-content-between lh-sm">
                        <div>
                            <h6 class="my-0" id="productName">${productName}</h6>
                            <small class="text-muted" id="productStock">개수 : ${productStock}</small>
                        </div>
                        <span class="text-muted" id="productPrice">${productPrice}원</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between bg-light">
                        <div class="text-success">
                            <h6 class="my-0">배송비</h6>
                        </div>
                        <span class="text-success">${shippingFee}원</span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between">
                        <span>총합</span>
                        <strong id="totalPrice">${TotalPrice}원</strong>
                    </li>
                </ul>
            `;
        cartList.innerHTML = html;
    } catch (error) {
        console.error(error);
    }
}
    addCartFromProduct();

const queryParams = getQueryParams();
const productId = queryParams['productId'];
const productName = queryParams['productName'];
const productPrice = queryParams['productPrice'];
const productStock = queryParams['productStock'];
async function createOrderItem(productId, productName, productPrice, productStock) {
    try {
        const response = await axios.post('/orderItem/api', {
            productId: productId,
            productName: productName,
            productPrice: productPrice,
            productStock: productStock
        });
        console.log("Order item created successfully. ID:", response.data);
        return response.data; // 생성된 주문 항목의 ID 반환
    } catch (error) {
        console.error("Error occurred while creating order item:", error);
        throw error;
    }
}



// 주문 생성
    async function createOrder() {
        try {
            // 사용자 ID 가져오기
            const userId = await getUsersIdFromAPI();
            // 배송 정보 저장
            const deliveryId = await saveDeliveryInfo();
            // 장바구니 상품 정보 가져오기
            const queryParams = getQueryParams();
            const productId = queryParams['productId'];
            const productName = queryParams['productName'];
            const productPrice = queryParams['productPrice'];
            const productStock = queryParams['productStock'];
            const cartItemId = createOrderItem(productId, productName, productPrice, productStock).get('orderItemId');

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
