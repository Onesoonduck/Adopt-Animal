/*!
* Start Bootstrap - Shop Homepage v5.0.6 (https://startbootstrap.com/template/shop-homepage)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-homepage/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project

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