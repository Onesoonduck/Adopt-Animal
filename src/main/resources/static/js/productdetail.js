

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
                            장바구니에 추가
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

// 초기화 코드
displayProductDetail();