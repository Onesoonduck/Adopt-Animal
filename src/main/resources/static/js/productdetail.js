

async function getProductById(productId) {
    try {
        const response = await axios.get(`/products/${productId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

async function displayProductDetail(productId) {
    try {
        // 상품 세부 정보 가져오기
        const productDto = await getProductById(productId);

        // 상품 정보를 표시할 요소 가져오기
        const divCard = document.getElementById('pyt');

        // 요소 초기화
        divCard.innerHTML = '';

        // 상품 정보를 표시하는 HTML 생성
        const html = `
            <div class="container px-4 px-lg-5 my-5">
                <div class="row gx-4 gx-lg-5 align-items-center">
                    <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." /></div>
                    <div class="col-md-6">
                        <h1 class="display-5 fw-bolder" id="productName">${productDto.productName}</h1>
                        <div class="fs-5 mb-5" id="productPrice">
                            <span>${productDto.productPrice}원</span>
                        </div>
                        <p class="lead">상품에 대한 소개 & 별점</p>
                        <div class="d-flex">
                            <input class="form-control text-center me-3" id="productStock" type="number" value="1" style="max-width: 3rem"/>
                            <a class="btn btn-outline-dark flex-shrink-0" id="buyButton" href="#" data-product-id="${productDto.productId}" 
                            data-product-name="${productDto.productName}" data-product-price="${productDto.productPrice}" data-product-stock="">
                                <i class="bi-cart-fill me-1"></i>
                                구매하기
                            </a>
                            <button class="btn btn-outline-dark flex-shrink-0" type="button">
                                <i class="bi-cart-fill me-1"></i>
                                장바구니에 추가-
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        // 생성한 HTML을 요소에 추가
        divCard.innerHTML = html;

        // 수량 입력란(input)의 변경 이벤트 처리
        const productStockInput = document.getElementById('productStock');
        productStockInput.value = '1'; // 기본값을 1로 설정
        productStockInput.addEventListener('input', function() {
            const inputQuantity = productStockInput.value;
            const buyButton = document.getElementById('buyButton');
            buyButton.dataset.productStock = inputQuantity; // 입력된 수량을 data-product-stock에 저장
        });

        // 구매 버튼 클릭 이벤트 처리
        const buyButton = document.getElementById('buyButton');
        buyButton.addEventListener('click', function(event) {
            event.preventDefault(); // 기본 동작인 링크 이동을 막음

            // 상품 정보 가져오기
            const productName = productDto.productName;
            const productPrice = productDto.productPrice;
            const productStock = document.getElementById('productStock').value;

            // order/productToOrder.html로 이동하기
            window.location.href = `/order/productToOrder.html?productId=${productId}productName=${productName}&productPrice=${productPrice}&productStock=${productStock}`;
        });

    } catch (error) {
        console.error(error);
    }
}

// 초기화 코드
displayProductDetail(1);
    // async function createProductAndGetId(productData) {
    //     try {
    //         const response = await axios.post('/createProduct', productData);
    //         return response.data;
    //     } catch (error) {
    //         console.error(error);
    //         throw error; // Re-throwing the error to propagate it to the calling function
    //     }
    // }
    //
    // window.onload = async function () {
    //     try {
    //         // 상품 생성 후 생성된 상품 ID 가져오기
    //         const productData = { /* Assuming you have product data here */};
    //         const productId = await createProductAndGetId(productData);
    //         console.log("Received product ID in window.onload:", productId);
    //         // 생성된 상품 ID를 사용하여 상세 정보 표시
    //         await displayProductDetail(productId);
    //     } catch (error) {
    //         console.error(error);
    //     }
    // };

// 버튼 요소 가져오기
// const purchaseButton = document.querySelector('.btn.btn-outline-dark');

// 클릭 이벤트 리스너 추가
// purchaseButton.addEventListener('click', function() {
//     // 주문하기 페이지로 이동
//     window.location.href = '/orderPage?id=' + productDto.id; // 주문하기 페이지의 URL로 대체해야 합니다.
// });

// 버튼 요소 가져오기
// const addToCartButton = document.querySelector('.btn.btn-outline-dark');
//
// // 클릭 이벤트 리스너 추가
// addToCartButton.addEventListener('click', function() {
//     // 여기에 장바구니 페이지로 이동하는 코드를 작성합니다.
//     // 예를 들어, window.location.href를 사용하여 장바구니 페이지의 URL로 이동할 수 있습니다.
//     window.location.href = '/장바구니-페이지-URL'; // 장바구니 페이지의 URL로 대체해야 합니다.
// });