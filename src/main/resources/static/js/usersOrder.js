import {Pagination} from "/static/js/pagination/pagination.js";

let orderCnt = await getOrderUserCount();
let pagination;

async function getOrderUserCount() {
  try {
    const response = await axios.get('/order/api/orderUserCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1;
  }
}

function callTable(page, size) {
  axios.get('/order/api/orderUserTable', {
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
        ordertitle = orderTableDto.firstOrderItem + ' 외 ' + (orderTableDto.orderCount-1);
      } else {
        ordertitle = orderTableDto.firstOrderItem;
      }
      if(orderTableDto.orderStatus == 'ORDER'){
        row.innerHTML = `
        <td class="align-middle">${orderTableDto.id}</td>
        <td class="align-middle">${formattedDate}</td>
        <td class="align-middle">${ordertitle}</td>
        <td class="align-middle">${orderTableDto.totalPrice.toLocaleString()}</td>
        <td class="align-middle">${orderTableDto.orderStatus}</td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm" id="order${orderTableDto.id}" onclick="removeCheck(${orderTableDto.id});">취소</button></td>
      `;
      }else {
        row.innerHTML = `
        <td class="align-middle">${orderTableDto.id}</td>
        <td class="align-middle">${formattedDate}</td>
        <td class="align-middle">${ordertitle}</td>
        <td class="align-middle">${orderTableDto.totalPrice.toLocaleString()}</td>
        <td class="align-middle">${orderTableDto.orderStatus}</td>
        <td class="align-middle">불가능</td>
      `;
      }


      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
    alert('주문목록을 가져오는 중 문제가 발생하였습니다.');
    console.log(error);
  });
}

function pageClickEvent(event) {
  event.preventDefault();
  let pageText = event.target.textContent;
  let page = Number(pageText);
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
  callTable(pagination.currentPage-1,pagination.dataPerPage);
}

function renderPage() {
  pagination = new Pagination(10,5, Math.ceil(orderCnt/10), pageClickEvent);
  pagination.renderPagination(1);
  callTable(0, 10);
}

renderPage();