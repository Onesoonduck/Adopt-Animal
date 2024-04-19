function getLoginData() {
  const userId = document.getElementById('userId').value;
  const password = document.getElementById('password').value;
  return {
    userId: userId,
    password: password,
  };
}

function postLogin(data) {
  axios.post('/users/login', data)
  .then(function (response) {
    if (response.status === 200) {
      const loginDiv = document.querySelector('#login-div');
      const logoutDiv = document.querySelector('#logout-div');
      loginDiv.style.display = "none";
      logoutDiv.style.display = "flex";
      alert('로그인에 성공하였습니다.');

      const accesskey = response.headers['authorization'];
      const decodedToken = jwt_decode(accesskey);
      const userRoles = decodedToken.auth;
      console.log(userRoles);
      if (userRoles === "ROLE_ADMIN") {
        location.href = '/admin/admin.html'
      } else {
        location.href = '/main.html';
      }
    } else {
      alert('로그인에 실패하였습니다.');
    }
  }).catch(function (error) {
    if (error.response.status === 400) {
      alert(error.response.data);
    }
    console.error('Error:', error);
  });
}

function loginFunc() {
  document.getElementById('login_form').addEventListener('submit', function(event) {
    event.preventDefault();
    const data = getLoginData();
    postLogin(data);
  });
}

loginFunc();

