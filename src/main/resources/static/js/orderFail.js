

// 실패 페이지에서 이전 페이지의 정보를 받아와서 다시 주문하기 페이지로 이동하는 함수
function retryOrder() {
    // 실패 페이지에서 이전 페이지의 정보를 받아옵니다.
    const queryParams = window.location.search;
    // 이전 페이지의 정보를 유지하면서 주문하기 페이지로 이동합니다.
    window.location.href = '/order/productToOrder.html' + queryParams;
}

