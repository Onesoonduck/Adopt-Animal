import {Pagination} from "/static/js/pagination/pagination.js";

let userCnt = await getUserCount();
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

function callTable(page, size) {
  axios.get('/users/api/usersTable', {
    params: {
      page: page,  // 페이지 번호
      size: size  // 페이지 크기
    }
  }) // 백엔드 API URL
  .then(function (response) {
    const userTableDtos = response.data.content;
    const tbody = document.getElementById('table-users');
    tbody.innerHTML = ''; // 초기화.
    let length = userTableDtos.length;
    for (let i = 0; i < length; i++) {
      const userTableDto = userTableDtos[i];
      const row = document.createElement('tr');
      const formattedDate = new Date(
          userTableDto.createdAt).toLocaleDateString();
      row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle">${userTableDto.userId}</td>
        <td class="align-middle">${userTableDto.userName}</td>
        <td class="align-middle">${userTableDto.email}</td>
        <td class="align-middle">${userTableDto.phoneNumber}</td>
        <td class="align-middle">${formattedDate}</td>
        <td class="align-middle">${userTableDto.orderCount}</td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm">삭제</button></td>
      `;

      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}

// 페이지 버튼 클릭 이벤트
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
  callTable(pagination.currentPage - 1, pagination.dataPerPage);
}

// 첫 페이지 시
function renderTable() {
  pagination = new Pagination(10, 5, Math.ceil(userCnt / 10), pageClickEvent);
  // 첫 페이지 로딩
  pagination.renderPagination(1);
  callTable(0, 10);
}

renderTable();