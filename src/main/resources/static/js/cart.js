import { Pagination } from "/static/js/pagination/pagination.js";
import jwtSingleton from "/static/js/jwtCloser.js";

document.addEventListener('DOMContentLoaded', function () {
    let pagination;
    let allProductsData = []; // 모든 페이지의 상품 데이터를 저장하는 배열

    loadProducts();

    function loadProducts(page = 1) {
        const accessToken = sessionStorage.getItem('authorization');
        const pageSize = 10; // 페이지 크기 설정

        axios.get(`http://localhost:8080/cart/items?page=${page}&size=${pageSize}`)
            .then(response => {
                const data = response.data.content;
                allProductsData = allProductsData.concat(data); // 현재 페이지의 데이터를 배열에 추가

                const productTable = document.getElementById('table-body');

                productTable.innerHTML = ''; // 기존 테이블 내용 지우기

                let totalPrice = 0; // 전체 상품의 총 가격

                // 데이터 가져와서 행 생성
                data.forEach(product => {
                    const row = document.createElement('tr');
                    const productPrice = parseFloat(product.productPrice);
                    const productCnt = parseInt(product.cnt);
                    const productTotalPrice = productPrice * productCnt;

                    row.innerHTML = `
                        <td><input type="checkbox" class="itemCheckbox" data-cartId="${product.cartId}"></td>
                        <td>${product.productName}</td>
                        <td>${product.productPrice}</td>
                        <td>
                            <button class="decrement-btn">-</button>
                            <span class="cnt">${product.cnt}</span>
                            <button class="increment-btn">+</button>
                        </td>
                        <td class="totalPriceCell">${productTotalPrice}</td>
                    `;
                    productTable.appendChild(row);
                    totalPrice += productTotalPrice; // 전체 상품의 총 가격에 현재 상품의 가격을 추가
                });

                // 총 가격 업데이트
                const priceDiv = document.querySelector('.price');
                priceDiv.textContent = `총 금액: ${totalPrice}원`;

                // 전체 선택 체크박스 이벤트 핸들러
                const selectAllCheckbox = document.getElementById('selectAllCheckbox');
                selectAllCheckbox.addEventListener('change', function() {
                    const checkboxes = document.querySelectorAll('.itemCheckbox');
                    checkboxes.forEach(checkbox => {
                        checkbox.checked = selectAllCheckbox.checked;
                    });
                });

                // 페이지네이션 렌더링
                if (!pagination) {
                    pagination = new Pagination(pageSize, 5, response.data.totalPages, onPageChange);
                }
                pagination.renderPagination(page);

                // 페이지 변경 이벤트 핸들러
                function onPageChange(newPage) {
                    loadProducts(newPage);
                }

                // 페이지네이션의 각 페이지 클릭 이벤트 핸들러 추가
                document.querySelectorAll('.page-item').forEach(item => {
                    item.addEventListener('click', () => {
                        const newPage = parseInt(item.textContent);
                        onPageChange(newPage);
                    });
                });

                // 수량 증가 버튼 클릭 이벤트 핸들러
                document.querySelectorAll('.increment-btn').forEach(button => {
                    button.addEventListener('click', (event) => {
                        const cartId = event.target.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');
                        axios.patch(`http://localhost:8080/cart/items/${cartId}/increase`)
                            .then(response => {
                                totalPrice = 0; // 전체 상품의 총 가격 초기화
                                loadProducts(page);
                            })
                            .catch(error => {
                                console.error('수량 증가 오류:', error);
                            });
                    });
                });

                // 수량 감소 버튼 클릭 이벤트 핸들러
                document.querySelectorAll('.decrement-btn').forEach(button => {
                    button.addEventListener('click', (event) => {
                        const cntElement = event.target.closest('tr').querySelector('.cnt');
                        let cnt = parseInt(cntElement.textContent);
                        if (cnt <= 1) {
                            alert("1보다 적게 감소시킬 수 없습니다.");
                            return;
                        }
                        const cartId = event.target.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');
                        axios.patch(`http://localhost:8080/cart/items/${cartId}/decrease`)
                            .then(response => {
                                totalPrice = 0; // 전체 상품의 총 가격 초기화
                                loadProducts(page);
                            })
                            .catch(error => {
                                console.error('수량 감소 오류:', error);
                            });
                    });
                });
            })
            .catch(error => {
                console.error('상품 로드 오류:', error);
            });
    }
});
document.querySelector('.delete-btn button').addEventListener('click', function() {
    const selectedCartIds = [];
    const checkboxes = document.querySelectorAll('.itemCheckbox');
    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            selectedCartIds.push(checkbox.getAttribute('data-cartId'));
        }
    });

    if (selectedCartIds.length === 0) {
        alert('삭제할 상품을 선택하세요.');
        return;
    }

    deleteCartItems(selectedCartIds);
});
function deleteCartItems(cartIds) {
    axios.delete("http://localhost:8080/cart/items", {
        headers: {
            'Content-Type': 'application/json'
        },
        data: cartIds
    })
    .then(response => {
        alert('선택한 물품이 삭제되었습니다.');
        window.location.reload();
    })
    .catch(error => {
        console.error('Failed to delete cart items:', error);
    });
}