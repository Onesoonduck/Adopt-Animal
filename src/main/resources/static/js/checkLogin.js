function isLogIn() {

}

// 로그인 상태에 따라 링크를 변경하는 함수
function updateLogin() {
  const loginDiv = document.querySelector('#login-div');
  const logoutDiv = document.querySelector('#logout-div');
  if (isLogIn()) {
    loginDiv.style.display = "flex";
    logoutDiv.style.display = "none";
  } else {
    loginDiv.style.display = "none";
    logoutDiv.style.display = "flex";
  }
}



window.onload = updateLogin;