document.addEventListener('DOMContentLoaded', function () {
    loadProducts();

    function loadProducts(page = 1) {
        const accessToken = sessionStorage.getItem('authorization');
        const pageSize = 10;

        axios.get(`http://localhost:8080/cart/items`)
            .then(response => {
                const data = response.data;

                const productTable = document.getElementById('table-body');

                productTable.innerHTML = '';

                let totalPrice = 0;

                data.forEach(product => {
                    const row = document.createElement('tr');
                    const productPrice = parseFloat(product.productPrice);
                    const productCnt = parseInt(product.cnt);
                    const productTotalPrice = productPrice * productCnt;

                    row.innerHTML = `
                        <td><input type="checkbox" class="itemCheckbox" data-cartId="${product.cartId}"></td>
                        <td>${product.productName}</td>
                        <td><img class="productImg" src=${product.productImg} alt="..." /></td>
                        <td>${product.productPrice}원</td>
                        <td>
                            <button type="button" class="decrement-btn btn btn-secondary">-</button>
                            <span class="cnt">${product.cnt}</span>
                            <button type="button" class="increment-btn btn btn-secondary">+</button>
                        </td>
                        <td class="totalPriceCell">${productTotalPrice}원</td>
                    `;
                    productTable.appendChild(row);
                    totalPrice += productTotalPrice;
                });

                updateTotalPrice(totalPrice);

                const selectAllCheckbox = document.getElementById('selectAllCheckbox');
                selectAllCheckbox.addEventListener('change', function() {
                    const checkboxes = document.querySelectorAll('.itemCheckbox');
                    checkboxes.forEach(checkbox => {
                        checkbox.checked = selectAllCheckbox.checked;
                    });
                });

                incrementButtons();
                decrementButtons();
            })
            .catch(handleError);
    }

    function updateTotalPrice(totalPrice) {
        const priceDiv = document.querySelector('.price');
        priceDiv.textContent = `총 금액: ${totalPrice}원`;
    }

    function incrementButtons() {
        document.querySelectorAll('.increment-btn').forEach(button => {
            button.addEventListener('click', handleIncrement);
        });
    }

    function handleIncrement(event) {
        const cartId = event.target.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');
        axios.patch(`http://localhost:8080/cart/items/${cartId}/increase`)
            .then(response => {
                loadProducts();
            })
            .catch(handleError);
    }

    function decrementButtons() {
        document.querySelectorAll('.decrement-btn').forEach(button => {
            button.addEventListener('click', handleDecrement);
        });
    }

    function handleDecrement(event) {
        const cntElement = event.target.closest('tr').querySelector('.cnt');
        let cnt = parseInt(cntElement.textContent);
        if (cnt <= 1) {
            alert("1보다 적게 감소시킬 수 없습니다.");
            return;
        }
        const cartId = event.target.closest('tr').querySelector('.itemCheckbox').getAttribute('data-cartId');
        axios.patch(`http://localhost:8080/cart/items/${cartId}/decrease`)
            .then(response => {
                loadProducts();
            })
            .catch(handleError);
    }

    function handleError(error) {
        console.error('에러 발생:', error);
    }

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
        .catch(handleError);
    }
});
