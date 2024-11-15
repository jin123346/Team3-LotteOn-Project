const reUid = /^[a-z]+[a-z0-9]{5,20}$/;  // 정규표현식 수정
const rePass = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,12}$/;
const reName = /^[가-힣]{2,10}$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp = /^01(?:0|1|[6-9])-(\d{3,4})-\d{4}$/;

let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isEmailOk = false;
// let isHpOk = false;

window.onload = function () {

    // 아이디 유효성 검사
    document.getElementById('btnCheckUid').onclick = function () {
        const uid = document.getElementById('uid').value;
        const uidResult = document.getElementById('uidValidation'); // 결과 표시할 요소

        if (!uid.match(reUid)) {
            uidResult.innerText = '아이디가 유효하지 않습니다.';
            isUidOk = false;
            return;
        }

        console.log("Fetching data for UID:", uid); // 요청 전 로그 출력

        fetch('/user/checkUser?type=uid&value=' + uid)
            .then(resp => {
                console.log("Fetch response:", resp); // 응답 로그 출력
                if (!resp.ok) {
                    throw new Error("서버 응답 에러");
                }
                return resp.json();
            })
            .then(data => {
                console.log("Parsed data:", data); // 파싱된 데이터 로그 출력

                if (data.result > 0) {
                    uidResult.innerText = '이미 사용중인 아이디 입니다.';
                    uidResult.style.color = 'red';
                    isUidOk = false;
                } else {
                    uidResult.innerText = '사용 가능한 아이디 입니다.';
                    uidResult.style.color = 'green';
                    isUidOk = true;
                }
            })
            .catch(err => {
                console.error("Fetch Error:", err); // 에러 로그 출력
                uidResult.innerText = '아이디 확인 중 오류가 발생했습니다.';
            });
    };

    // 비밀번호 유효성 검사
    document.getElementById('passwordConfirm').addEventListener('focusout', function () {
        const passResult = document.getElementById('passConfirmValidation');
        const pass1 = document.getElementById('pass').value.trim();
        const pass2 = document.getElementById('passwordConfirm').value.trim();
        if (!pass1.match(rePass)) {
            passResult.innerText = '비밀번호가 유효하지 않습니다.';
            isPassOk = false;
            return;
        }
        if (pass1 === pass2) {
            passResult.innerText = '비밀번호가 일치합니다.';
            passResult.style.color = 'green';
            isPassOk = true;
        } else {
            passResult.innerText = '비밀번호가 일치하지 않습니다.';
            passResult.style.color = 'red';
            isPassOk = false;
        }
    });

    // 이름 유효성 검사
    document.getElementById('name').addEventListener('focusout', function () {
        const nameResult = document.getElementById('nameValidation'); // 결과 표시할 요소
        const name = document.getElementById('name').value;
        if (!name.match(reName)) {
            nameResult.innerText = '이름이 유효하지 않습니다.';
            isNameOk = false;
        } else {
            nameResult.innerText = '이름이 유효합니다.';
            nameResult.style.color = 'green';
            isNameOk = true;
        }
    });

    // 이메일 유효성 검사
    document.getElementById('btnSendEmail').onclick = function () {
        const email = document.getElementById('email').value;
        const emailResult = document.getElementById('emailValidation'); // 결과 표시할 요소
        if (!email.match(reEmail)) {
            emailResult.innerText = '이메일이 유효하지 않습니다.';
            return;
        }
        fetch('/user/checkUser?type=email&value=' + email)
            .then(resp => resp.json())
            .then(data => {
                if (data.result > 0) {
                    emailResult.innerText = '이미 사용중인 이메일 입니다.';
                    isEmailOk = false;
                } else {
                    emailResult.innerText = '이메일 인증 코드를 확인하세요.';
                    emailResult.style.color = 'green';
                }
            })
            .catch(err => {
                console.error(err);
            });

        // 이메일 인증코드 발급
        fetch('/api/sendVerifyCode', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({email: email}),
        })
            .then(response => response.text())
            .then(data => {
                console.log(data);
                document.getElementById('codeInputRow').style.display = 'table-row';  // 인증번호 입력 칸 보이기
            })
            .catch(err => console.error('Error:', err));
    };

    // 인증 코드 확인
    document.getElementById('btnEmailCode').onclick = function () {
        const code = document.getElementById('emailCode').value;
        const codeResult = document.getElementById('codeValidation');


        fetch('/api/verifyCode', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({code: code}),
        })
            .then(response => response.text())
            .then(data => {
                console.log(data);
                if (data.trim() === '인증 성공!') {
                    codeResult.innerText = '인증 성공!';
                    codeResult.style.color = 'green';
                    isEmailOk = true;
                } else {
                    codeResult.innerText = '인증 실패!';
                    codeResult.style.color = 'red';
                }
            })
            .catch(err => console.error('Error:', err));
    };

    // 휴대폰 유효성 검사
    // document.getElementById('phone').addEventListener('focusout', function () {
    //     const hpResult = document.getElementById('phoneValidation'); // 결과 표시할 요소
    //     const hp = document.getElementById('phone').value;
    //     if (!hp.match(reHp)) {
    //         hpResult.innerText = '휴대폰 번호가 유효하지 않습니다.';
    //         return;
    //     }
    //     fetch('/user/checkUser?type=hp&value=' + hp)
    //         .then(resp => resp.json())
    //         .then(data => {
    //             if (data.result > 0) {
    //                 hpResult.innerText = '이미 사용중인 휴대폰번호 입니다.';
    //                 isHpOk = false;
    //             } else {
    //                 hpResult.innerText = '사용 가능한 휴대폰번호입니다.';
    //                 isHpOk = true;
    //             }
    //         })
    //         .catch(err => {
    //             console.error(err);
    //         });
    // });

    function checkUser(type, value) {
        fetch(`/checkUser?type=${type}&value=${value}`)
            .then(response => response.json())  // 응답을 JSON으로 변환
            .then(data => {
                if (data.result === 1) {
                    alert(`${type}가 이미 존재합니다.`);
                } else {
                    alert(`${type} 사용 가능합니다.`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('중복 확인 중 오류가 발생했습니다.');
            });

    }

    document.querySelector('form').onsubmit = function (event) {
        event.preventDefault(); // 기본 폼 제출 방지

        console.log(isUidOk);
        console.log(isPassOk);
        console.log(isNameOk);
        console.log(isEmailOk);

        // UID 유효성 검사
        if (!isUidOk) {
            alert('아이디가 유효하지 않습니다.');
            return; // UID 유효성 검사 실패 시 회원가입 중단
        }

        // 비밀번호 유효성 검사
                if (!isPassOk) {
                    alert('비밀번호가 유효하지 않습니다.');
                    return; // 비밀번호 유효성 검사 실패 시 회원가입 중단
                }

        // 이름 유효성 검사
                if (!isNameOk) {
                    alert('이름이 유효하지 않습니다.');
                    return; // 이름 유효성 검사 실패 시 회원가입 중단
                }

        // 이메일 유효성 검사
                if (!isEmailOk) {
                    alert('이메일이 유효하지 않습니다.');
                    return; // 이메일 유효성 검사 실패 시 회원가입 중단
                }


        const formData = new FormData(event.target); // 폼 데이터 가져오기

        // 회원가입 API 호출
        fetch('/user/memberregister', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (response.ok) {
                    // 회원가입 성공 시 alert 및 리다이렉트
                    alert('회원가입 되셨습니다.');
                    window.location.href = '/user/login'; // 로그인 페이지로 리다이렉트
                } else {
                    // 회원가입 실패 시 메시지 표시
                    alert('가입 오류.');
                    return response.text(); // 실패 시 메시지 반환
                }
            })
            .then(data => {
                if (data) {
                    alert(data); // 서버에서 반환된 실패 메시지 표시
                }
            })
            .catch(err => console.error('Error:', err));
    };
};
