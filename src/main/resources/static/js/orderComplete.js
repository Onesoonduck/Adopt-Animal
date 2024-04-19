//
// function render() {
//     // query string에서 주문 ID 가져오기
//     const requestParams = getQueryParams();
//
//     function getQueryParams() {
//         const params = {};
//         const queryString = window.location.search.slice(1); // '?' 제거
//         const pairs = queryString.split('&'); // 각 쿼리 스트링 항목 분리
//
//         pairs.forEach(pair => {
//             const [key, value] = pair.split('='); // 키와 값 분리
//             params[decodeURIComponent(key)] = decodeURIComponent(value || ''); // 디코드 후 객체에 할당
//         });
//
//         return params;
//     }
//
//     // 주문 정보 가져오기
//     async function findOrderById(orderId) {
//         try {
//             const response = await axios.get(`/order/list/${orderId}`);
//             const orderData = response.data;
//             console.log("주문 정보:", orderData);
//             return orderData;
//         } catch (error) {
//             console.error("주문을 검색하는 동안 오류가 발생했습니다:", error);
//             throw error; // 오류 처리
//         }
//     }
//
//     // 상품 테이블 생성 및 주문 정보 채우기
//     async function displayOrderDetails() {
//         const orderId = requestParams.orderId;
//         const orderData = await findOrderById(orderId);
//         const recipient = orderData.recipient;
//
//         // query string에서 상품 ID, 수량, 가격 가져오기
//         const productIdsParam = requestParams['productIds'];
//         const cntValuesParam = requestParams['cntValues'];
//         const productPricesParam = requestParams['productPrices'];
//
//         if (productIdsParam && cntValuesParam && productPricesParam) {
//             const productIds = productIdsParam.split(',');
//             const cntValues = cntValuesParam.split(',');
//             const productPrices = productPricesParam.split(',');
//
//             // 상품 테이블 생성 및 주문 정보 채우기
//             populateOrderItems(productIds, cntValues, productPrices);
//
//             // 추가적인 처리 진행
//         } else {
//             console.error('하나 이상의 쿼리 매개변수가 누락되었습니다.');
//         }
//
//         // 주문 정보 추가
//         const orderInfo = document.createElement('div');
//         orderInfo.innerHTML = `
//             <p class="lead mb-4">수령인 : ${recipient}</p>
//             <p class="lead mb-4">주문 날짜 : ${new Date(orderData.orderDate).toLocaleString()}</p>
//             <p class="lead mb-4">배송지 : ${orderData.deliveryAddress.city} ${orderData.deliveryAddress.street} ${orderData.deliveryAddress.zipcode}</p>
//         `;
//         orderItemsBody.appendChild(orderInfo);
//     }
//
//     // 상품 테이블 생성 및 주문 정보 채우는 함수
//     function populateOrderItems(productIds, cntValues, productPrices) {
//         const orderItemsBody = document.getElementById('orderItemsBody');
//         let totalPrice = 0;
//
//         for (let i = 0; i < productIds.length; i++) {
//             const productId = productIds[i];
//             const cntValue = cntValues[i];
//             const productPrice = productPrices[i];
//
//             const row = document.createElement('tr');
//             row.classList.add('productTable');
//             row.innerHTML = `
//                 <td>상품 ${productId}</td>
//                 <td>${cntValue}</td>
//                 <td>${productPrice}원</td>
//               `;
//             orderItemsBody.appendChild(row);
//
//             totalPrice += parseInt(productPrice) * parseInt(cntValue);
//         }
//
//         // 배송비 추가
//         const deliveryFeeRow = document.createElement('tr');
//         deliveryFeeRow.classList.add('deliveryFee');
//         deliveryFeeRow.innerHTML = `
//             <td>배송비</td>
//             <td></td>
//             <td>3,000원</td>
//         `;
//         orderItemsBody.appendChild(deliveryFeeRow);
//
//         // 총 가격 추가
//         const totalPriceRow = document.createElement('tr');
//         totalPriceRow.classList.add('totalPrice');
//         totalPriceRow.innerHTML = `
//             <td>Totals</td>
//             <td></td>
//             <td>${totalPrice.toLocaleString()}원</td>
//         `;
//         orderItemsBody.appendChild(totalPriceRow);
//     }
//
//     displayOrderDetails();
//
//     // 계속 쇼핑하기와 마이페이지 버튼 이벤트 추가
//     const continueShoppingButton = document.querySelector('.btn-warning');
//     const myPageButton = document.querySelector('.btn-outline-warning');
//
//     continueShoppingButton.addEventListener('click', function() {
//         window.location.href = '/product/productDetail.html';
//     });
//
//     myPageButton.addEventListener('click', function() {
//         window.location.href = '/users/users.html';
//     });
// }
//
// document.addEventListener('DOMContentLoaded', render);


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

const requestParams = getQueryParams();

// 쿼리 문자열에서 필요한 정보들을 가져온다.
const totalPrice = requestParams.totalPrice;
const recipient = requestParams.recipient;
const productNames = requestParams.productNames.split(',');
const productCnts = requestParams.cntValues.split(',').map(cnt => parseInt(cnt)); // 개수 배열
const productPrices = requestParams.productPrices.split(',').map(price => parseInt(price)); // 가격 배열

function printPage() {
    document.getElementById('orderInfo').innerHTML = `
    <h1 class="display-5 fw-bold text-body-emphasis">주문 완료</h1>
    <div class="col-lg-6 mx-auto">
        <p class="lead mb-4">성공적으로 주문이 완료되었습니다.</p>
        <table class="table">
            <thead>
                <tr>
                    <th>상품명</th>
                    <th>개수</th>
                    <th>가격</th>
                </tr>
            </thead>
            <tbody>
                ${productNames.map((productName, index) => `
                    <tr>
                        <td>${productName}</td>
                        <td>${productCnts[index]}</td>
                        <td>${productPrices[index]}원</td>
                    </tr>
                `).join('')}
                <tr>
                    <td>배송비</td>
                    <td></td>
                    <td>3,000원</td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td>Totals</td>
                    <td></td>
                    <td>${totalPrice}</td>
                </tr>
            </tfoot>
        </table>
        <p class="lead mb-4">수령인: ${recipient}</p>
        <p class="lead mb-4">주문 날짜: ${new Date().toLocaleString()}</p>
        <div class="d-grid gap-2 d-sm-flex justify-content-sm-center">
            <a type="button" class="btn btn-warning btn-lg px-4 gap-3 text-white" id="continueShoppingBtn">계속 쇼핑하기</a>
            <a type="button" class="btn btn-outline-warning btn-lg px-4" id="goToMyPage">마이페이지</a>
        </div>
    </div>
`;
}

printPage();

document.getElementById('continueShoppingBtn').addEventListener('click', function() {
    window.location.href = '/product/productList.html';
});

document.getElementById('goToMyPage').addEventListener('click', function() {
    window.location.href = '/users/users.html';
});
