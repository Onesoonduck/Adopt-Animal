//jwt 토큰을 보관하는 함수
let jwtSingleton = (function () {
  let instance;

  function init() {
    function setToken(jwtToken) {
      sessionStorage.setItem('authorization', jwtToken);
    }

    function getToken() {
      return sessionStorage.getItem('authorization');
    }

    function removeToken() {
      return sessionStorage.removeItem('authorization');
    }

    return {
      setToken: setToken,
      getToken: getToken,
      removeToken: removeToken
    };
  }

  return {
    getInstance: function () {
      if (!instance) {
        instance = init();
      }
      return instance;
    }
  };
})();

export default jwtSingleton;