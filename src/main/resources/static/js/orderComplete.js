function displayOrderInfo(orderInfo) {
    // 상품 목록을 표시할 테이블 요소를 가져옵니다.
    const tableBody = document.querySelector('.table tbody');

    // 기존에 표시된 내용을 모두 초기화합니다.
    tableBody.innerHTML = '';

    // 주문 정보의 상품 목록을 순회하면서 각 상품을 테이블에 추가합니다.
    orderInfo.products.forEach(product => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${product.productName}</td>
            <td>${product.productStock}</td>
            <td>${product.productPrice}</td>
        `;
        tableBody.appendChild(row);
    });

    // 기타 주문 정보(수령인, 주문 날짜, 배송지 등)를 화면에 표시합니다.
    document.querySelector('.lead.mb-4:nth-child(2)').textContent += orderInfo.receiverName;
    document.querySelector('.lead.mb-4:nth-child(3)').textContent += orderInfo.orderDate;
    document.querySelector('.lead.mb-4:nth-child(4)').textContent += orderInfo.deliveryAddress;
}
