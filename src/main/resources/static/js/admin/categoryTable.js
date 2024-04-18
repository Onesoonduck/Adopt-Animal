function callTable() {
    axios.get('/categories', {
        params: { }
    }).then(function (response) {
        const productDtos = response.data.content;
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

