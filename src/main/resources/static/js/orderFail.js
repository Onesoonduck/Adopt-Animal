

function handleRetryOrder() {
    location.href = '/cart/cart.html'; // 장바구니 페이지로 이동
}

function handleGoMyPage() {
    location.href = '/users/users.html'; // 마이페이지로 이동
}

const retryOrderButton = document.querySelector('.btn.btn-warning.btn-lg.px-4.gap-3.text-white');
retryOrderButton.addEventListener('click', handleRetryOrder);

const goMyPageButton = document.querySelector('.btn.btn-outline-warning.btn-lg.px-4');
goMyPageButton.addEventListener('click', handleGoMyPage);

