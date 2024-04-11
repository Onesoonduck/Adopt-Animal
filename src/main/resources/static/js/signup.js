function getFormData() {
  const userId = document.getElementById('userId').value;
  const userName = document.getElementById('userName').value;
  const password = document.getElementById('password').value;
  const email = document.getElementById('email').value;
  const phoneNumber = document.getElementById('phoneNumber').value;
  const userRole = "USER";

  return {
    userId: userId,
    userName: userName,
    password: password,
    email: email,
    phoneNumber: phoneNumber,
    userRole: userRole
  };
}

function postSignUp(data) {
  axios.post('/users/signup', data)
  .then(function(response) {
    if (response.status === 200) {
      alert('회원가입이 완료되었습니다.');
      location.href = '/login.html';
    } else {
      alert('회원가입에 실패하였습니다.');
    }
  }).catch(function(error) {
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