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
            const cartItemId = await createOrderItem(productId, productName, productPrice, productStock).get('orderItemId');

            // 주문 요청 객체 생성
            const orderRequest = {
                usersId: userId,
                deliveryId: deliveryId,
                orderItemIds: cartItemId
            };

            // 주문 생성
            const response = await axios.post('/order', orderRequest);

            // 주문 완료 페이지로 리다이렉트
            console.log("주문이 성공적으로 생성되었습니다. 주문 ID:", response.data);
            window.location.href = '/order/orderComplete.html';
        } catch (error) {
            console.error("주문 생성 중 오류가 발생했습니다:", error);
            window.location.href = '/order/orderFail.html';
        }
    }

// 주문하기 페이지에서 이전 페이지의 정보를 추출하여 사용하는 함수
function getPreviousPageInfo() {
    // 현재 URL에서 이전 페이지의 정보를 추출합니다.
    const previousPageInfo = window.location.search;
    // 추출된 정보를 사용하여 주문하기 기능을 실행합니다.
    // 예를 들어, 제품 ID, 제품 이름, 가격 등...
    // 이 함수는 페이지가 로드될 때 자동으로 실행되도록 설정할 수 있습니다.
}

// 주문하기 페이지 로드 시 이전 페이지의 정보를 추출하여 사용합니다.
getPreviousPageInfo();

// 사용자 ID 가져오기 API 호출
    async function getUsersIdFromAPI() {
        try{
            const userId = await axios.get('/users/api/usersId');
            return userId.data;
        } catch {
            console.error("Error occurred while getting users ID:", error);
            throw error;
        }
    }
