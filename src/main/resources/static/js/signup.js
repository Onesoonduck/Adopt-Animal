
function getFormData() {
  const userId = document.getElementById('userId').value;
  const userName = document.getElementById('userName').value;
  const password = document.getElementById('password').value;
  const email = document.getElementById('email').value;
  const phoneNumber = document.getElementById('phoneNumber').value;
  const userRole = "USER";

  return {
    userId,
    userName,
    password,
    email,
    phoneNumber,
    userRole
  };
}

function postSignUp(data) {
  axios.post('/users/signup', data)
  .then(function(response) {
    if (response.status === 200) {
        alert('회원가입이 완료되었습니다.');
        location.href = '/static/users/login.html';
    } else {
        alert('회원가입에 실패하였습니다.');
    }
  }).catch(function(error) {
    if (error.response.status === 400) {
      alert(error.response.data);
    }
      console.error('Error:', error);
  });
}

function signupFunc() {
  document.getElementById('signup_form').addEventListener('submit', function(event) {
    event.preventDefault();
    const data = getFormData();
    postSignUp(data);
  });
}

signupFunc();