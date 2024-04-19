let tableSection = document.getElementById("categoryTable")
let addSection = document.getElementById("categoryAdd")

function callTable() {
  axios.get('/categories')
  .then(function (response) {
    const categories = response.data;
    const tbody = document.querySelector("#table-category");
    tbody.innerHTML = ''; // 초기화
    let length = categories.length;
    for (let i = 0; i < length; i++) {
      const categoryDto = categories[i];
      const row = document.createElement('tr');
      row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle">${categoryDto.categoryName}</td>
        <td class="align-middle">
                <img class="img" src=${categoryDto.categoryImg} alt=""
         id="category-list-img"
         style="width: 100px; height: 100px;"
          onerror="this.onerror=null; this.src='https://dummyimage.com/100x100/808080/FFF.png';">
          </td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm">삭제</button></td>
      `;

      tbody.appendChild(row);
    }
  })
  .catch(function (error) {
    console.log(error);
  });
}


// 카테고리 등록 모달 관련

const uploadModal = new bootstrap.Modal(document.getElementById('uploadModal'));

async function uploadImage(imageFile) {
    let formData = new FormData();
    formData.append("image", imageFile);

    try {
        const response = await axios.post('/image/upload', formData);

        if (response.status === 200) {
            return response.data;
        } else {
            alert('이미지 등록이 실패하였습니다.');
            return null;
        }
    } catch(error) {
        alert(error.response.data);
        console.error(error);
    }
}

async function getCategoryDate() {
    const image = document.getElementById('category-image').files[0];

    const categoryName = document.getElementById('category-name').value;
    const categoryImg = await uploadImage(image);

    return {
        categoryName,
        categoryImg
    }
}

async function postCategoryRegister(data) {
    axios.post('/categories/add', data)
    .then(function (response){
        const formdata = data;
        if(response.status===201) {
            alert('카테고리 등록이 성공했습니다.');
          uploadModal.hide();
          callTable();
        } else {
            alert('카테고리 등록이 실패했습니다.');
        }
    }).catch(function (error) {
        alert(error.response.data);
    })
}

// 카테고리 추가 모달에서 "등록' 버튼을 누르면 카테고리가 등록됩니다.
let categoryForm = document.querySelector('#category_form');
categoryForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    const formData = await getCategoryDate();
    await postCategoryRegister(formData);
});

document.querySelector('.btn-primary.btn-sm').addEventListener('click', function() {
    uploadModal.show();
});

function showImage() {
    let inputImage = document.querySelector('#category-image');
    inputImage.addEventListener('change', function() {
        let file = this.files[0];
        let reader = new FileReader();
        reader.addEventListener('load', function() {
            let img = document.querySelector('#category-img');
            img.src = reader.result;
        });
        reader.readAsDataURL(file);
    });
}

callTable();
showImage();