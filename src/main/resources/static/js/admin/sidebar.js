window.addEventListener('load', function () {
  let navItems = document.querySelectorAll('.nav-item a');
  let currentUrl = window.location.href;

  navItems.forEach(function (item) {
    console.log(item.href);
    if (item.href === currentUrl) {
      item.classList.add('current');
    } else {
      item.classList.remove('current');
    }
  });
});