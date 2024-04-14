import {Pagination} from "/static/js/pagination/pagination.js";

let orderCnt = await getOrderCount();
let pagination;

async function getOrderCount() {
  try {
    const response = await axios.get('/order/admin/api/orderCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1; // or throw an error, or return a default value
  }
}

function callTable(page, size) {
  axios.get('/orders/api/orderTable', {
    params: {
      page: page,  // 페이지 번호
      size: size  // 페이지 크기
    }
  }).then(function (response) {
    const orderTableDtos = response.data.content;
    const tbody = document.getElementById('table-orders');
    tbody.innerHTML = ''; // 초기화.
    let length = orderTableDtos.length;
    for (let i = 0; i < length; i++) {
      const orderTableDto = orderTableDtos[i];
      const row = document.createElement('tr');
      const formattedDate = new Date(
          orderTableDto.orderDate).toLocaleDateString();
      let ordertitle;
      if(orderTableDto.orderCount>1){
        ordertitle = orderTableDto.firstOrderItem + ' 외 ' + orderTableDto.orderCount;
      }
      row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle">${orderTableDto.id}</td>
        <td class="align-middle">${formattedDate}</td>
        <td class="align-middle">${ordertitle}</td>
        <td class="align-middle">${orderTableDto.totalPrice.toLocaleString()}</td>
        <td class="align-middle">${orderTableDto.orderUserId}</td>
        <td class="align-middle">${orderTableDto.orderStatus}</td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm">삭제</button></td>
      `;

      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}
