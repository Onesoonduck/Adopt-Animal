function renderUser() {
  axios.get('/users/api/users') // 백엔드 API URL
  .then(function (response) {
    const userId = document.getElementById('userId');
    const userName = document.getElementById('userName');
    const email = document.getElementById('email');
    const phoneNumber = document.getElementById('phoneNumber');

    userId.innerText = response.data.userId;
    userName.innerText = response.data.userName;
    email.innerText = response.data.email;
    phoneNumber.innerText = response.data.phoneNumber;

  })
  .catch(function (error) {
    console.log(error);
  });
}

renderUser();