document.addEventListener('DOMContentLoaded', function () {
    fetch('http://localhost:8080/cart/items/lyj')
        .then(response => response.json())
        .then(data => {
            // 테이블 생성
            const productTable = document.getElementById('table-body');
            let totalPrice = 0;
            data.forEach(product => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td><input type="checkbox" class="itemCheckbox" data-cartid="${product.cartId}"></td>
                    <td>${product.productName}</td>
                    <td>${product.productPrice}</td>
                    <td>${product.cnt}</td>
                `;
                productTable.appendChild(row);
                totalPrice += product.productPrice * product.cnt;
            });
            // totalPrice div안에 넣기
            const priceDiv = document.querySelector('.price');
            priceDiv.textContent += `${totalPrice}원`;

            // 체크박스 클릭시 전체 선택
            const selectAllCheckbox = document.getElementById('selectAllCheckbox');
            selectAllCheckbox.addEventListener('change', function() {
                const checkboxes = document.querySelectorAll('.itemCheckbox');
                checkboxes.forEach(checkbox => {
                    checkbox.checked = selectAllCheckbox.checked;
                });
            });

            // 삭제 버튼 클릭시 선택된 항목 삭제
            const deleteButton = document.querySelector('.delete-btn button');
            deleteButton.addEventListener('click', function() {
                const checkedCheckboxes = document.querySelectorAll('.itemCheckbox:checked');
                const cartIds = Array.from(checkedCheckboxes).map(checkbox => checkbox.getAttribute('data-cartid'));
                deleteCartItems(cartIds);
            });
        })
        .catch(error => console.error('Error fetching products:', error));
});

function deleteCartItems(cartIds) {
    fetch('http://localhost:8080/cart/items', {
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
    .catch(error => console.error('Error deleting cart items:', error));
}
