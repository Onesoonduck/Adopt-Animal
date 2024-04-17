import {Pagination} from "/static/js/pagination/pagination.js";

let productCnt = await getProductCount();
let pagination;

let tableSection = document.getElementById("productTable")
let addSection = document.getElementById("productAdd")
let infoSection = document.getElementById("productInfo")

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

function uploadImage(data) {
  return axios.post('/image/upload', data)
  .then(function (response) {
    if (response.status === 200) {
      alert('이미지 등록이 완료되었습니다.');
      return response.data;
    } else {
      alert('이미지 등록이 실패하였습니다.');
      return null;
    }
  }).catch(function (error) {
    alert(error.response.data);
  })
}

async function getProductData() {
  const image = document.getElementById('product-image').files[0];
  // image를 업로드 하고 image의 경로를 가져와야 한다.

  const productName = document.getElementById('product-name').value;
  const productImage = await uploadImage(image);
  const productCategory = document.getElementById('product-category').value;
  const productPrice = document.getElementById('product-price').value;
  const productStock = document.getElementById('product-stock').value;
  const productDescription = document.getElementById(
      'product-description').value;

  return {
    productName,
    productImage,
    productCategory,
    productPrice,
    productStock,
  };
}

function postProductRegister(data) {
  axios.post('/products', data)
  .then(function (response){
    if(response.status===200) {
      location.href = '/static/admin/admin-product.html';
    } else {
      alert('상품 등록이 실패했습니다.');
    }
  }).catch(function (error) {
    alert(error.response.data);
  })
}

function getCategoryList() {
  axios.get('/categories')
  .then(function (response) {
    if(response.status===200) {
      const selectBox = document.getElementById('product-category');
      // 기존의 옵션을 모두 제거합니다.
      selectBox.innerHTML = '';
      // 새로운 옵션을 추가합니다.
      response.data.forEach(category => {
        const option = document.createElement('option');
        option.value = category.categoryId;
        option.text = category.categoryName;
        selectBox.appendChild(option);
      });
    }
  })
  .catch(function (error) {
    console.error(error);
  })
}

function renderPage() {
  pagination = new Pagination(10, 5, Math.ceil(productCnt / 10),
      pageClickEvent);
  pagination.renderPagination(1);
  callTable(0, 10);
}

function showSection(section) {
  // 모든 섹션을 숨깁니다.
  tableSection.style.display = 'none';
  addSection.style.display = 'none';
  infoSection.style.display = 'none';

  // 지정된 섹션만 보입니다.
  section.style.display = 'block';
  switch (section) {
    case addSection:
      getCategoryList();
      break
    default:
      break
  }
}

// "추가" 버튼을 선택합니다.
let addButton = document.querySelector('#product-add');

// 클릭 이벤트를 추가합니다.
addButton.addEventListener('click', function() {
  // 이곳에 클릭 시 실행할 코드를 작성합니다.
  showSection(addSection);
});

renderPage();
showSection(tableSection);

