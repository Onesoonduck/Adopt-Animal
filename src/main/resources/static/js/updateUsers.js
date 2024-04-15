function getFormData() {
  const userName = document.getElementById('userName').value;
  const password = document.getElementById('password').value;
  const email = document.getElementById('email').value;
  const phoneNumber = document.getElementById('phoneNumber').value;
  const userRole = "USER";

  return {
    userName: userName,
    password: password,
    email: email,
    phoneNumber: phoneNumber,
    userRole: userRole
  };
}

function postUpdateUsers(data) {
  axios.post('/users/usersEdit', data)
  .then(function(response) {

    if (response.status === 200) {
      alert('회원정보 수정이 완료되었습니다.');
      location.href = '/static/users/users.html';
    } else {
      alert('회원정보 수정에 실패하였습니다.');
    }
  }).catch(function(error) {
    if (error.response.status === 400) {
      alert(error.response.data);
    }
    console.error('Error:', error);
  });
}

 function updateUsersFunc() {
  document.getElementById('updateUsersform').addEventListener('submit', function (event) {
    event.preventDefault();
    const data = getFormData();
    postUpdateUsers(data);
  });
}

updateUsersFunc();
