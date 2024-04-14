import {Pagination} from "/static/js/pagination/pagination.js";

let userCnt = await getUserCount();
let orderCnt = await getOrderCount();
let productCnt = await getProductCount();
let inquireCnt = 18;

let pagination;

async function getUserCount() {
  try {
    const response = await axios.get('/users/api/userCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1; // or throw an error, or return a default value
  }
}

async function getOrderCount() {
  try {
    const response = await axios.get('/order/admin/api/orderCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1; // or throw an error, or return a default value
  }
}

async function getProductCount() {
  try {
    const response = await axios.get('/products/api/productCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1; // or throw an error, or return a default value
  }
}

function callUserTable(page, size) {
  axios.get('/users/api/usersTable', {
    params: {
      page: page,  // 페이지 번호
      size: size  // 페이지 크기
    }
  }) // 백엔드 API URL
  .then(function (response) {
    const userTableDtos = response.data.content;

    // 테이블 타이틀
    document.getElementById('table-title').innerHTML = '회원 목록';

    // 테이블 헤드
    const thead = document.getElementById('table-header');
    thead.innerHTML = `
      <tr className="text-muted">
        <th>idx</th>
        <th>아이디</th>
        <th>이름</th>
        <th>이메일</th>
        <th>연락처</th>
        <th>가입일</th>
        <th>주문 수</th>
      </tr>
    `

    // 테이블 바디
    const tbody = document.getElementById('table-body');
    tbody.innerHTML = ''; // 초기화
    let length = userTableDtos.length;
    for (let i = 0; i < length; i++) {
      const userTableDto = userTableDtos[i];
      const row = document.createElement('tr');
      const formattedDate = new Date(userTableDto.createdAt).toLocaleDateString();
      row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle">${userTableDto.userId}</td>
        <td class="align-middle">${userTableDto.userName}</td>
        <td class="align-middle">${userTableDto.email}</td>
        <td class="align-middle">${userTableDto.phoneNumber}</td>
        <td class="align-middle">${formattedDate}</td>
        <td class="align-middle">${userTableDto.orderCount}</td>
      `;
      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}

// 네비게이션 클릭 이벤트
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
  callUserTable(pagination.currentPage-1,pagination.dataPerPage);
}

function updateUserCount() {
  document.getElementById('user-count').innerHTML = userCnt;
}
function updateOrderCount() {
  document.getElementById('order-count').innerHTML = orderCnt;
}
function updateProductCount() {
  document.getElementById('product-count').innerHTML = productCnt;
}

function renderPage() {
  pagination = new Pagination(10,5, Math.ceil(userCnt/10), pageClickEvent);
  pagination.renderPagination(1);
  callUserTable(0, 10);
  updateUserCount();
  updateOrderCount();
  updateProductCount();
}

renderPage()