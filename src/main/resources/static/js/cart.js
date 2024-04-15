document.addEventListener('DOMContentLoaded', function () {
    loadProducts(); // 페이지가 로드될 때 상품을 로드

    function loadProducts() {
        fetch('http://localhost:8080/cart/items')
            .then(response => response.json())
            .then(data => {
                const productTable = document.getElementById('table-body');
                let totalPrice = 0;

                productTable.innerHTML = ''; // 기존 테이블 내용 지우기

                //데이터 가져와서 행 생성
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
                    totalPrice += productTotalPrice;
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

                // 상품 증가 버튼 클릭 이벤트 핸들러
                document.querySelectorAll('.increment-btn').forEach(button => {
                    button.addEventListener('click', function() {
                        const cntElement = button.parentElement.querySelector('.cnt');
                        const cnt = parseInt(cntElement.textContent);
                        const cartId = button.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');

                        fetch('http://localhost:8080/cart/items/' + cartId + '/increase', {
                            method: 'PATCH',
                        })
                        .then(response => response.json())
                        .then(data => {
                            loadProducts(); // 상품 다시로드
                        })
                    });
                });

                // 상품 감소 버튼 클릭 이벤트 핸들러
                document.querySelectorAll('.decrement-btn').forEach(button => {
                    button.addEventListener('click', function() {
                        const cntElement = button.parentElement.querySelector('.cnt');
                        const cnt = parseInt(cntElement.textContent);
                        if (cnt > 1) {
                            const cartId = button.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');

                            fetch('http://localhost:8080/cart/items/' + cartId + '/decrease', {
                                method: 'PATCH',
                            })
                            .then(response => response.json())
                            .then(data => {
                                loadProducts(); // 상품 다시로드
                            })
                        } else {
                            alert("1보다 작은 개수로 감소시킬 수 없습니다.");
                        }
                    });
                });
                const deleteButton = document.querySelector('.delete-btn button');
                deleteButton.addEventListener('click', function() {
                    const checkedCheckboxes = document.querySelectorAll('.itemCheckbox:checked');
                    const cartIds = Array.from(checkedCheckboxes).map(checkbox => checkbox.getAttribute('data-cartId'));
                    deleteCartItems(cartIds);
                });
            })
    }
        // 삭제 버튼 이벤트 핸들러

});

// Function to delete cart items
function deleteCartItems(cartIds) {
    fetch("http://localhost:8080/cart/items", {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cartIds)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to delete cart items');
        }
        alert('선택한 물품이 삭제되었습니다.');
        window.location.reload();
    })

}
