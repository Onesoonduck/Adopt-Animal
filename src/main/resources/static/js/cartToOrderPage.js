document.addEventListener('DOMContentLoaded', function () {
    // 상품 정보를 HTML에 추가합니다.
    const productToOrder = document.getElementById('product-to-order');

    // 상품 이름, 개수, 가격을 정적으로 지정합니다.
    const productName = "상품 이름";
    const productStock = "개수";
    const productPrice = "가격";

    // 상품 정보를 표시할 li 요소를 생성합니다.
    const li = document.createElement('li');
    li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'lh-sm');
    li.innerHTML = `
        <div>
            <h6 class="my-0">${productName}</h6>
            <small class="text-muted">${productStock}</small>
        </div>
        <span class="text-muted">${productPrice}</span>
    `;

    // li 요소를 상품 정보를 담을 위치에 추가합니다.
    productToOrder.querySelector('ul.list-group').appendChild(li);

    // 총합을 계산하여 추가합니다.
    const totalPrice = parseFloat(productPrice) * parseFloat(productStock);
    const totalLi = document.createElement('li');
    totalLi.classList.add('list-group-item', 'd-flex', 'justify-content-between');
    totalLi.innerHTML = `
        <span>총합</span>
        <strong>${totalPrice}</strong>
    `;
    productToOrder.querySelector('ul.list-group').appendChild(totalLi);
});