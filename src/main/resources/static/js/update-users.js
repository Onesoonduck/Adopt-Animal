/*
function postUpdateUsers(data) {
  axios.post('/users/usersEdit', data, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
.then(function(response) {
    if (response.status === 200) {
      alert('회원정보 수정이 완료되었습니다.');
      location.href = '/main.html';
    } else {
      alert('회원정보 수정에 실패하였습니다.');
    }
  }).catch(function(error) {
    console.error('Error:', error);
  });
}

function updateUsersFunc() {
  document.getElementById('updateUsersform').addEventListener('submit',
      function (e) {
        e.preventDefault();
        const form = e.target;
        const formData = new FormData(form);

        console.log('formData', Array.from(formData.values()),
            Array.from(formData.keys()));
        postUpdateUsers(formData);

      });
}

updateUsersFunc();
*/

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

function postUpdateUsers(data) {
  axios.post('/users/usersEdit', data)
  .then(function(response) {

    if (response.status === 200) {
      alert('회원정보 수정이 완료되었습니다.');
      location.href = '/main.html';
    } else {
      alert('회원정보 수정에 실패하였습니다.');
    }
  }).catch(function(error) {
    console.error('Error:', error);
  });
}

function updateUsersFunc() {
  document.getElementById('updateUsersform').addEventListener('submit', function(event) {
    event.preventDefault();
    const data = getFormData();
    postUpdateUsers(data);
  });
}

updateUsersFunc();

