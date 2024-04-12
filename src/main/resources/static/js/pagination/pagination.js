
export class Pagination {
  dataPerPage = 10; // 한 화면에 표시되는 데이터 수
  maxPage = 5; // 한 화면에 표시될 페이지
  totalPage = 18;
  currentPage = 1;  // 현재 페이지
  callTable;

  constructor(datePerPage, maxPage, totalPage, callTable) {
    this.dataPerPage = datePerPage;
    this.maxPage = maxPage;
    this.totalPage = totalPage;
    this.callTable = callTable;
  }

// 페이지 버튼 클릭 이벤트
  pageClickEvent(event) {
    event.preventDefault();
    let pageText = event.target.textContent;
    let page = Number(pageText);
    if (!isNaN(page)) {
      this.currentPage = page;
    } else if (pageText === '«') {
      if (this.currentPage > 1) {
        this.currentPage = this.currentPage - 1;
      }
    } else if (pageText === '»') {
      if (this.currentPage < this.totalPage) {
        this.currentPage = this.currentPage + 1;
      }
    }
    this.renderPagination(this.currentPage);
    this.callTable(this.currentPage-1, this.dataPerPage);
  }

  renderPagination(currentPage) {
    let startPage;
    if (currentPage <= 3) {
      startPage = 1;
    } else if (currentPage >= this.totalPage - 2) {
      startPage = this.totalPage - 4;
    } else {
      startPage = currentPage - 2;
    }

    let endPage = startPage + this.maxPage - 1;
    if (endPage > this.totalPage) endPage = this.totalPage;
    let pageDoc = document.getElementById('pagination');
    let html = '<ul class="pagination justify-content-center">';

    if(currentPage > 1){
      html += '<li class="page-item"><a href="#" class="page-link py-2 px-3"><span>&laquo;</span></a></li>';
    }
    for (let i = startPage; i <= endPage; i++) {
      if (i === currentPage) {
        html += `<li class="page-item active my"><a href="#" class="page-link py-2 px-3">${i}</a></li>`;
      } else {
        html += `<li class="page-item my"><a href="#" class="page-link py-2 px-3">${i}</a></li>`;
      }
    }

    if(currentPage < this.totalPage) {
      html += '<li class="page-item"><a href="#" class="page-link py-2 px-3"><span>&raquo;</span></a></li>';
    }
    html += '</ul>';

    pageDoc.innerHTML = html;

    Array.from(document.getElementsByClassName('page-link')).forEach((element) => {
      element.addEventListener('click', this.pageClickEvent.bind(this));
    });
  }
}

