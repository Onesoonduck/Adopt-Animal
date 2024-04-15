import {updateLogin} from './checkLogin.js';
window.addEventListener('load', updateLogin);
// 모든 요청
axios.interceptors.request.use(function (config) {
  const accessKey = sessionStorage.getItem('authorization');
  if (accessKey) {
    config.headers.authorization = accessKey;
  }else {
    config.headers.authorization = '';
  }
  return config;
}, function (error) {
  return Promise.reject(error);
});

// 모든 응답
axios.interceptors.response.use(function (response) {
  const accessKey = response.headers['authorization'];
  if (accessKey) {
    // 임시로 세션 스토리지에 저장
    sessionStorage.setItem('authorization', accessKey);
  } else {
    sessionStorage.removeItem('authorization');
  }
  return response;
}, function (error) {
  return Promise.reject(error);
});

//TODO : jwt 토큰을 보관하는 함수
function jwtCloser() {
  let token = null;

  function setToken(jwtToken) {
  }
<<<<<<< HEAD
}
=======
}
>>>>>>> b354bfa3f5c403542e3288530d3fdd49154531b0
