import {Pagination} from "./pagination.js";

let userCnt = 34;
let pagination;
axios.get('/users/api/userCount').then(function (response) {
  userCnt = response.data;
  pagination = new Pagination(10,5, Math.ceil(userCnt/10), callTable);
  // 첫 페이지 로딩
  pagination.renderPagination(1);
  callTable(0,10);
}).catch(function (error) {
  console.log(error);
})

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

      row.innerHTML = `
        <th class="align-middle">i</th>
        <td class="align-middle">${userTableDto.userId}</td>
        <td class="align-middle">${userTableDto.userName}</td>
        <td class="align-middle">${userTableDto.email}</td>
        <td class="align-middle">${userTableDto.phoneNumber}</td>
        <td class="align-middle">${userTableDto.createAt}</td>
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

