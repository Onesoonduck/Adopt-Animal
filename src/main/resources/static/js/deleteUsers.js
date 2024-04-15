
function deleteUsers() {
  axios.delete('/users/usersDelete')
  .then(function(response) {
    if (response.status === 200) {
      alert('회원 탈퇴가 완료되었습니다.');
      location.href = '/static/users/login.html';
    } else {
      alert('회원 탈퇴에 실패하였습니다.');
    }
  }).catch(function(error) {
    console.error('Error:', error);
  });
}
function deleteUsersFunc() {
  document.getElementById('confirmButton').addEventListener('click', function() {
    if (confirm('정말로 회원 탈퇴를 하시겠습니까?')) {
      // 여기서 서버로 회원 탈퇴 요청을 보냄
      deleteUsers();
    }
  });
}

deleteUsersFunc();
