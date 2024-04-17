import {Pagination} from "/static/js/pagination/pagination.js";

let productCnt = await getProductCount();
let pagination;

async function getProductCount() {
  try {
    const response = await axios.get('/products/api/productCount');
    return response.data;
  } catch (error) {
    console.error(error);
    return -1; // or throw an error, or return a default value
  }
}

function callTable(page, size) {
  axios.get('/products/lists', {
    params: {
      page: page,  // 페이지 번호
      size: size  // 페이지 크기
    }
  }).then(function (response) {
    const productDtos = response.data.content;
    const tbody = document.getElementById('table-products');
    tbody.innerHTML = ''; // 초기화.
    let length = productDtos.length;
    for (let i = 0; i < length; i++) {
      const productDto = productDtos[i];
      const row = document.createElement('tr');
      row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle"></td>
        <td class="align-middle">${productDto.productName}</td>
        <td class="align-middle">${productDto.productPrice.toLocaleString()}</td>
        <td class="align-middle">${productDto.Stock}</td>
        <td class="align-middle">0</td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm">삭제</button></td>
      `;

      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
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
  callTable(pagination.currentPage - 1, pagination.dataPerPage);
}

function controlSection() {
  const tableSection = document.getElementById("productTable")
  const addSection = document.getElementById("productAdd")
  const infoSection = document.getElementById("productInfo")

  tableSection.style.display = 'block';
  addSection.style.display = 'none';
  infoSection.style.display = 'none';

  function showSection(section) {
    // 모든 섹션을 숨깁니다.
    tableSection.style.display = 'none';
    addSection.style.display = 'none';
    infoSection.style.display = 'none';

    // 지정된 섹션만 보입니다.
    section.style.display = 'block';
  }
}

function renderPage() {
  pagination = new Pagination(10, 5, Math.ceil(productCnt / 10),
      pageClickEvent);
  pagination.renderPagination(1);
  callTable(0, 10);
}

controlSection();
renderPage();