import { Pagination } from "/static/js/pagination/pagination.js";

let pagination;
let productCnt;

async function getProductCount() {
    try {
        const response = await axios.get('/products/api/productCount');
        return response.data;
    } catch (error) {
        console.error(error);
        return -1; // or throw an error, or return a default value
    }
}

async function callProductTable(page, size) {
    try {
        const response = await axios.get('/products/lists', {
            params: {
                page: page,
                size: size
            }
        });
        const productDtos = response.data.content;
        const tbody = document.getElementById('product-card');
        tbody.innerHTML = ''; // 초기화.

        productDtos.forEach(productDto => {
            const divCard = document.createElement('div');
            divCard.className = 'col mb-5';
            divCard.innerHTML = `
                <div class="card h-100 card-link">
                    <div class="card-body p-4">
                        <div class="text-center">
                            <h5 class="fw-bolder">${productDto.productName}</h5>
                            ${productDto.productPrice}
                        </div>
                    </div>
                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                        <div class="text-center"><a class="btn btn-outline-warning mt-auto" href="#" onclick="getProductById(${productDto.productId})">장바구니 담기</a></div>
                    </div>
                </div>
            `;

            tbody.appendChild(divCard);
        });
    } catch (error) {
        console.error(error);
    }
}

function getProductById(id) {
    axios.get(`/products/${id}`)
        .then(function (response) {
            const product = response.data;
            // 상품 정보를 이용하여 productDetail.html 파일을 보여줌
            showProductDetail(product);
        })
        .catch(function (error) {
            console.error(error);
        });
}

async function showProductDetail(product) {
    try {
        const response = await fetch('/productDetail.html');
        const html = await response.text();
        const parser = new DOMParser();
        const doc = parser.parseFromString(html, 'text/html');

        // product 정보를 페이지에 삽입
        const productIdElement = doc.querySelector('.small');
        productIdElement.textContent = `상품아이디: ${product.productId}`;

        const productNameElement = doc.querySelector('h1.display-5');
        productNameElement.textContent = product.productName;

        const productPriceElement = doc.querySelector('.fs-5 span');
        productPriceElement.textContent = `${product.productPrice}원`;

        const productDescriptionElement = doc.querySelector('p.lead');
        productDescriptionElement.textContent = product.productDescription;

        // 페이지를 보여줌
        document.body.innerHTML = doc.body.innerHTML;
    } catch (error) {
        console.error('Error fetching productDetail.html:', error);
    }
}

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
    callProductTable(pagination.currentPage - 1, pagination.dataPerPage);
}

async function renderPage() {
    productCnt = await getProductCount();
    pagination = new Pagination(6, 5, Math.ceil(productCnt / 10), pageClickEvent);
    pagination.renderPagination(1);
    await callProductTable(0, 6);
}

renderPage();
