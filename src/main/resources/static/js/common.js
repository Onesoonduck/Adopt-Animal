import {updateLogin} from './checkLogin.js';
import jwtSingleton from "/static/js/jwtCloser.js";

window.addEventListener('load', updateLogin);
// 모든 요청
axios.interceptors.request.use(function (config) {
  const jwt= jwtSingleton.getInstance();
  const accessKey = jwt.getToken();
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
  const jwt= jwtSingleton.getInstance();
  const accessKey = response.headers['authorization'];
  if (accessKey) {
    jwt.setToken(accessKey);
  } else {
    // jwt.removeToken();
  }
  return response;
}, function (error) {
  return Promise.reject(error);
});

