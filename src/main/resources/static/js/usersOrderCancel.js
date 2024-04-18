function removeCheck(id) {

  if (confirm("정말 삭제하시겠습니까??") == true){    //확인
    axios.put('/order/api/statusChange', null, { params: { id: id } })
    .then(function(response) {

      if (response.status === 200) {
        alert('주문 취소가 완료되었습니다.');
        location.href = '/static/users/usersOrder.html';
      } else {
        alert('주문 취소에 실패하였습니다.');
      }
    }).catch(function(error) {
      alert('주문 취소중 문제가 발생하였습니다.');
      console.error('Error:', error);
    });
  }else{   //취소

    return false;

  }
}