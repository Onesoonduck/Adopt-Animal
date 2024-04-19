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


document.addEventListener('DOMContentLoaded',
    function () {
        addOrderItemsToCart();

// 주문 항목 추가
        function addOrderItemsToCart(cartItems) {
            const accessToken = sessionStorage.getItem('authorization');
            axios.get(`/cart/items`)
                .then(response => {
                    const data = response.data;
                    const productTable = document.getElementById('cart-table');
                    productTable.innerHTML = '';
                    let totalPrice = 0;

                    data.forEach(product => {
                        const productId = product.productId;
                        const productPrice = parseFloat(product.productPrice);
                        const productCnt = parseInt(product.cnt);
                        const productTotalPrice = productPrice * productCnt;
                        const listItem = document.createElement('li');

                        listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'lh-sm');
                        listItem.innerHTML = `
                        <div class="product-item">
                                <h6 class="my-0 product-name">${product.productName}</h6>
                                <small class="text-muted product-count">${product.cnt}개</small>
                                <small class="text-muted product-id" style="display: none;">${product.productId}</small>
                            <span class="text-muted product-price" >${product.productPrice}원</span>
                        </div>
                        `;
                        productTable.appendChild(listItem);

                        totalPrice += productTotalPrice;

                    });

                    const deliveryFeeItem = document.createElement('li');
                    deliveryFeeItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'bg-light');
                    deliveryFeeItem.innerHTML = `
                    <div class="text-success">
                        <h6 class="my-0">배송비</h6>
                    </div>
                    <span class="text-success">+ 3000원</span>
                `;
                    productTable.appendChild(deliveryFeeItem);

                    const totalPriceItem = document.createElement('li');
                    const cartTotalPrice = totalPrice + 3000;
                    totalPriceItem.classList.add('list-group-item', 'd-flex', 'justify-content-between');
                    totalPriceItem.innerHTML = `
                    <span>총합</span>
                    <strong id="totalPrice">${cartTotalPrice}원</strong>
                `;
                    productTable.appendChild(totalPriceItem);

                })
        }

    });

async function createOrderItems() {
    const productElements = document.querySelectorAll('.product-item'); // 각 상품을 나타내는 요소들의 리스트

    let orderItems = []; // 주문 항목을 담을 배열

    let productId = '';
    productElements.forEach(productElement => {
        productId = productElement.querySelector('.product-id').innerText;
        const productPriceText = productElement.querySelector('.product-price').innerText;
        const productCntText = productElement.querySelector('.product-count').innerText;

// '원' 또는 '개'를 제거한 값을 가져옴
        const productPrice = parseFloat(productPriceText.replace('원', ''));
        const productCnt = parseInt(productCntText.replace('개', ''));

        orderItems.push({
            productId: productId,
            price: productPrice,
            count: productCnt
        });
    });

    try {
        const response = await axios.post('/orderItem/lists', orderItems);
        console.log('주문 항목이 성공적으로 추가되었습니다. 응답 데이터:', response.data);
        return response.data;
    } catch (error) {
        console.error('주문 항목을 추가하는 동안 오류가 발생했습니다:', error);
    }
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
        return response.data;
    } catch (error) {
        console.error("Error occurred while saving delivery information:", error);
        throw error;
    }
}



// 주문 생성
const orderButton = document.getElementById('orderButton');
orderButton.addEventListener('click', createOrder);

// 주문 생성 함수
async function createOrder(event) {
    try {
        event.preventDefault(); // 기본 동작을 막음

        // 사용자 ID 가져오기
        const userId = await getUsersIdFromAPI();
        // 배송 정보 저장
        const deliveryId = await saveDeliveryInfo();
        // 장바구니 상품 정보
        const orderItemId = await createOrderItems();

        // 주문 요청 객체 생성
        const orderRequest = {
            usersId: userId,
            deliveryId: deliveryId,
            orderItemId: orderItemId
        };
        // 주문 생성
        const response = await axios.post('/order', orderRequest);

        // 주문 완료 페이지로 이동할 때 데이터 전달
        const orderId = response.data;
        const recipient = document.getElementById("receiverName").value; // 수령인 정보
        const deliveryAddress = document.getElementById("sample6_address").value; // 배송지 정보
        const deliveryDetailAddress = document.getElementById("sample6_detailAddress").value; // 상세 배송지 정보
        const totalPrice = document.getElementById("totalPrice").innerText; // 총액 정보
        const productNames = Array.from(document.querySelectorAll('.product-name')).map(productId => productId.innerText); // 상품 ID 배열 가져오기
        const cntValues = Array.from(document.querySelectorAll('.product-count')).map(cntElement => cntElement.innerText.replace('개', '')); // 수정된 부분
        const productPrices = Array.from(document.querySelectorAll('.product-price')).map(priceElement => priceElement.innerText.replace('원', '')); // 상품 가격 배열 가져오기

        // 데이터를 URL에 추가하여 주문 완료 페이지로 이동
        const queryString = `?orderId=${orderId}&totalPrice=${totalPrice}&recipient=${recipient}&productNames=${productNames.join(',')}&cntValues=${cntValues.join(',')}&productPrices=${productPrices.join(',')}`;
        location.href = '/static/order/orderComplete.html' + queryString;
    } catch (error) {
        console.error("주문 생성 중 오류가 발생했습니다:", error);
        location.href = '/static/order/orderFail.html';
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
