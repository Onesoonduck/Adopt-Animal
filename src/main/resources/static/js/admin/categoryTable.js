import axios from "axios";

let tableSection = document.getElementById("categoryTable")
let addSection = document.getElementById("categoryAdd")
let infoSection = document.getElementById("categoryInfo")

function callTable() {
    axios.get('/categories', {
        params: { }
    }).then(function (response) {
        const categoryDtos = response.data.content;
        const tbody = document.getElementById('table-category');
        tbody.innerHTML = ''; // 초기화
        let length = categoryDtos.length;
        for (let i = 0; i < length; i++) {
            const categoryDto = categoryDtos[i];
            const row = document.createElement('tr');
            row.innerHTML = `
        <th class="align-middle">${i}</th>
        <td class="align-middle"></td>
        <td class="align-middle">${categoryDto.categoryName}</td>
        <td class="align-middle">${categoryDto.categoryImg}</td>
        <td class="align-middle"><button type="button" class="btn btn-danger btn-sm">삭제</button></td>
      `;

            tbody.appendChild(row);
        }
    })
        .catch(function (error) {
            console.log(error);
        });
}

async function uploadImage(imageFile) {
    let formData = new FormData();
    formData.append("image", imageFile);

    try {
        const response = await axios.post('/image/upload', formData);

        if (response.status === 200) {
            alert('이미지 등록이 완료되었습니다.');
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

async function getCategoryData() {
    const image = document.getElementById('category-image').files[0];
    // image를 업로드 하고 image의 경로를 가져와야 한다.

    const categoryName = document.getElementById('category-name').value;
    const categoryImg = await uploadImage(image);

    return {
        categoryName,
        categoryImg,
    };
}

async function postCategoryRegister(data) {
    axios.post('/categorys', data)
        .then(function (response){
            const formdata = data;
            if(response.status===201) {
                location.href = '/static/admin/admin_category.html';
                showSection(tableSection);
            } else {
                alert('카테고리 등록이 실패했습니다.');
            }
        }).catch(function (error) {
        alert(error.response.data);
    })
}

function showSection(section) {
    // 모든 섹션을 숨깁니다.
    tableSection.style.display = 'none';
    addSection.style.display = 'none';
    infoSection.style.display = 'none';

    // 지정된 섹션만 보입니다.
    section.style.display = 'block';
}

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

// "추가" 버튼을 누르면 카테고리 추가 섹션으로 전환됩니다.
let addButton = document.querySelector('#category-add');
addButton.addEventListener('click', function(event) {
    event.preventDefault();
    showSection(addSection);
});

// 카테고리 추가 섹션에서 "등록" 버튼을 누르면 카테고리가 등록됩니다.
let categoryForm = document.querySelector('#category_form');
categoryForm.addEventListener('submit', async function (event) {
    event.preventDefault();
    const formData = await getCategoryData();
    await postCategoryRegister(formData);
});

showSection(tableSection);
showImage();