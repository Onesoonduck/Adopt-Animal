
function postLogout() {
  sessionStorage.removeItem('authorization');
  axios.post('/users/logout')
  .then(function(response) {
    if (response.status === 200) {
      alert('로그아웃에 성공하였습니다.');
      location.href = '/static/main.html';
    } else {
      alert('로그아웃에 실패하였습니다.');
    }
  }).catch(function(error) {
    if (error.response.status === 400) {
      alert(error.response.data);
    }
    console.error('Error:', error);
  });
}

function logoutFunc() {
  document.getElementById('logoutIcon').addEventListener('click', function(event) {
    event.preventDefault();
    postLogout();
  });
}

logoutFunc();

