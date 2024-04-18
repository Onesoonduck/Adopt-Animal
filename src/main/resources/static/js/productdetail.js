

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
        const productDto = await getProductById(productId);
        const divCard = document.getElementById('pyt');
        divCard.innerHTML= '';
        divCard.innerHTML = `
        <div class="container px-4 px-lg-5 my-5">
            <div class="row gx-4 gx-lg-5 align-items-center">
                <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." /></div>
                <div class="col-md-6">
                    
                    <h1 class="display-5 fw-bolder" id="productName">${productDto.productName}</h1>
                    <div class="fs-5 mb-5" id="productPrice">
                        <span >${productDto.productPrice}원</span>
                    </div>
                    <div class="small mb-1" id="productStock">재고 : ${productDto.productStock}</div>
                    <p class="lead">상품에 대한 소개 & 별점</p>
                    <div class="d-flex">
                        <input class="form-control text-center me-3" id="inputQuantity" type="num" value="1" style="max-width: 3rem" />
                        <button class="btn btn-outline-dark flex-shrink-0" type="button">
                            <i class="bi-cart-fill me-1"></i>
                            구매하기
                        </button>
                        <button class="btn btn-outline-dark flex-shrink-0" type="button">
                            <i class="bi-cart-fill me-1"></i>
                            장바구니에 추가-
                        </button>

                    </div>
                </div>
            </div>
        </div>
        `;
        divCard.appendChild(divCard); // 추가: divCard를 body에 추가
    } catch (error) {
        console.error(error);
    }
}

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

window.onload = async function () {
    displayProductDetail(getQueryParams());
}

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