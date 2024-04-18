import {Pagination} from "/static/js/pagination/pagination.js";
let pagination;
let productCnt = await getProductCount();
let categoryId = 99;
async function getProductCount() {
    try {
        const response = await axios.get(`/products/api/productCount`);
        return response.data;
    } catch (error) {
        console.error(error);
        return -1; // or throw an error, or return a default value
    }
}

async function getProductCountByCategory() {
    try {
        const response = await axios.get(`/products/api/productCount/${categoryId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        return -1; // or throw an error, or return a default value
    }
}

function callProductTable(page, size) {
    axios.get('/products/lists', {
        params: {
            page: page,  // 페이지 번호
            size: size  // 페이지 크기
        }
    }) // 백엔드 API URL
        .then(function (response) {
            const productDtos = response.data.content;
            const tbody = document.getElementById('product-card');
            tbody.innerHTML = ''; // 초기화.
            let length = productDtos.length;
            for (let i = 0; i < length; i++) {
                const productDto = productDtos[i];
                const divCard = document.createElement('div');
                divCard.className = 'col mb-7 p-2';
                divCard.innerHTML = `
            <div class="card h-100">
                <!-- Product image-->
                <img class="card-img-top h-60" src=${productDto.productImg} alt="..." />
                <!-- Product details-->
                <div class="card-body p-3 d-flex align-items-center justify-content-center">
                    <div class="text-center">
                        <!-- Product name-->
                        <h5 class="fw-bolder">${productDto.productName}</h5>
                        <!-- Product price-->
                        ${productDto.productPrice.toLocaleString()}
                    </div>
                </div>
                <!-- Product actions-->
                <div class="card-footer p-0 pb-2 border-top-0 bg-transparent">
                    <div class="text-center"><a class="btn btn-sm btn-outline-warning mt-auto" href="#">장바구니 담기</a></div>
                </div>
            </div>
      `;
                tbody.appendChild(divCard);
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}

function callProdutTableByCategory(page, size) {
    axios.get(`/products/lists/${categoryId}`, {
        params: {
            page: page,  // 페이지 번호
            size: size  // 페이지 크기
        }
    }) // 백엔드 API URL
    .then(function (response) {
        const productDtos = response.data.content;
        const tbody = document.getElementById('product-card');
        tbody.innerHTML = ''; // 초기화.
        let length = productDtos.length;
        for (let i = 0; i < length; i++) {
            const productDto = productDtos[i];
            const divCard = document.createElement('div');
            divCard.className = 'col mb-7 p-2';
            divCard.innerHTML = `
            <div class="card h-100">
                <!-- Product image-->
                <img class="card-img-top h-60" src=${productDto.productImg} alt="..." />
                <!-- Product details-->
                <div class="card-body p-3 d-flex align-items-center justify-content-center">
                    <div class="text-center">
                        <!-- Product name-->
                        <h5 class="fw-bolder">${productDto.productName}</h5>
                        <!-- Product price-->
                        ${productDto.productPrice.toLocaleString()}
                    </div>
                </div>
                <!-- Product actions-->
                <div class="card-footer p-0 pb-2 border-top-0 bg-transparent">
                    <div class="text-center"><a class="btn btn-sm btn-outline-warning mt-auto" href="#">장바구니 담기</a></div>
                </div>
            </div>
      `;
            tbody.appendChild(divCard);
        }
    })
    .catch(function (error) {
        console.log(error);
    });
}

// 카테고리 관련 기능
// 카테고리를 불러들여 카테고리 네비를 완성시킨다.
function getCategoryList() {
    axios.get('/categories')
    .then(function (response){
        if(response.status===200){
            const productNav = document.querySelector('#product-nav');
            productNav.innerHTML='';
            const all = document.createElement('a');
            all.className = "nav-link active";
            all.value = "ALL";
            all.text = "모두";
            all.style.cursor = "pointer";
            categoryId = 99;
            // 클릭 이벤트 추가
            all.addEventListener('click', async function (event) {
                event.preventDefault();
                // 여기에 클릭 시 실행할 코드를 작성하세요.
                categoryId = 99;
                productCnt = await getProductCount();
                let ceilproductCnt = Math.ceil(productCnt / 6);
                pagination.setTotalPage(ceilproductCnt);
                pagination.renderPagination(1);
                callProductTable(0, 6);
                let activeNav = document.querySelector('#product-nav .active');
                if (activeNav) {
                    activeNav.classList.remove('active');
                }
                all.classList.add("active");
            });
            productNav.appendChild(all);
            response.data.forEach(category => {
                const nav = document.createElement('a');
                nav.className = "nav-link";
                nav.value = category.categoryId;
                nav.text = category.categoryName;
                nav.style.cursor = "pointer";

                // 클릭 이벤트 추가
                nav.addEventListener('click', async function (event) {
                    event.preventDefault();
                    // 여기에 클릭 시 실행할 코드를 작성하세요.
                    categoryId = nav.value;
                    productCnt = await getProductCountByCategory();
                    let ceilproductCnt = Math.ceil(productCnt / 6);
                    pagination.setTotalPage(ceilproductCnt);
                    pagination.renderPagination(1);
                    callProdutTableByCategory(0, 6);
                    let activeNav = document.querySelector(
                        '#product-nav .active');
                    if (activeNav) {
                        activeNav.classList.remove('active');
                    }
                    nav.classList.add("active");

                });

                productNav.appendChild(nav);
            })
        }
    })
}

// 페이지 클릭 이벤트
function pageClickEvent(event) {
    event.preventDefault();
    let pageText = event.target.textContent;
    let page = Number(pageText);
    let currentPage;
    if (!isNaN(page)) {
        pagination.currentPage = page;
    } else if (pageText === '«') {
        if (pagination.currentPage > 1) {
            pagination.currentPage = pagination.currentPage - 1;
        }
    } else if (pageText === '»') {
        if (pagination.currentPage < pagination.totalPage) {
            pagination.currentPage = pagination.currentPage + 1;
        }
    }
    pagination.renderPagination(pagination.currentPage);

    // 활성화 된 카테고리를 확인 하여 분기
    if(categoryId===99){
        callProductTable(pagination.currentPage - 1, pagination.dataPerPage);
    } else {
        callProdutTableByCategory(pagination.currentPage - 1, pagination.dataPerPage)
    }

}

function renderPage() {
    pagination = new Pagination(6,5, Math.ceil(productCnt/6), pageClickEvent);
    pagination.renderPagination(1);
    callProductTable(0, 6);
}

getCategoryList();
renderPage();