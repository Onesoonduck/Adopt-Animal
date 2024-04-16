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
            <div class="col mb-5"> 
                <a href="#" class="card-link">
                    <!-- Product image-->
                    <img class="card-img-top" src="https://dummyimage.com/450x300/dee2e6/6c757d.jpg" alt="..." />
                    <!-- Product details-->
                    <div class="card-body p-4">
                        <div class="text-center">
                            <!-- Product name-->
                            <h5 class="fw-bolder">${productDto.productName}</h5>
                            <!-- Product price-->
                            ${productDto.productPrice}원
                        </div>
                    </div>
                    <!-- Product actions-->
                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                        <div class="text-center"><a class="btn btn-outline-warning mt-auto" href="#">장바구니 담기</a></div>
                    </div>
                </a>
            </div>
            `;
            divCard.addEventListener('click',function () {
                window.location.href = '/productDetailPage?id=' + productDto.productId;
            })
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
            console.log(product);
        })
        .catch(function (error) {
            console.error(error);
        });
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
