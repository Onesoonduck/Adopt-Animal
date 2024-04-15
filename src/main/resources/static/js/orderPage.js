
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = ''; // 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

function submitOrder() {
    // 주문 정보를 서버로 전송하는 로직
    var address = document.getElementById("sample6_address").value;
    var detailAddress = document.getElementById("sample6_detailAddress").value;
    var postcode = document.getElementById("sample6_postcode").value;
    var receiverName = document.getElementById("receiverName").value; // 수신자 이름 입력란의 ID를 확인해야 합니다.
    var receiverPhoneNumber = document.getElementById("receiverPhoneNumber").value; // 수신자 전화번호 입력란의 ID를 확인해야 합니다.

    axios.post('/order/delivery/add', {
        address: {
            city: address, // 주소 정보 중 city에는 전체 주소가 들어갈 수 있습니다.
            street: detailAddress, // 상세 주소 정보는 street에 들어갑니다.
            zipcode: postcode // 우편번호 정보는 zipcode에 들어갑니다.
        },
        receiverName: receiverName, // 수신자 이름 정보
        receiverPhoneNumber: receiverPhoneNumber // 수신자 전화번호 정보
    })
        .then(function (response) {
            // 주문 성공 시 처리
            alert("주문 정보가 성공적으로 저장되었습니다.");
        })
        .catch(function (error) {
            // 주문 실패 시 처리
            console.error('Error:', error);
            alert("주문 정보 저장에 실패하였습니다.");
        });
}