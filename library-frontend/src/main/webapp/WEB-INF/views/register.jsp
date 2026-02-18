<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>회원가입 - 도서관리 시스템</title>

            <!-- Tailwind CSS -->
            <script src="https://cdn.tailwindcss.com"></script>
            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        </head>

        <body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center py-8">
            <div class="max-w-md w-full mx-4">
                <!-- 로고 섹션 -->
                <div class="text-center mb-8">
                    <div class="inline-flex items-center justify-center w-16 h-16 bg-green-600 rounded-full mb-4">
                        <i class="fas fa-user-plus text-white text-2xl"></i>
                    </div>
                    <h1 class="text-3xl font-bold text-gray-800">회원가입</h1>
                    <p class="text-gray-600 mt-2">도서관리 시스템 계정 만들기</p>
                </div>

                <!-- 회원가입 폼 -->
                <div class="bg-white rounded-2xl shadow-xl p-8">
                    <form id="registerForm">
                        <div class="space-y-5">
                            <!-- 아이디 입력 -->
                            <div>
                                <label for="loginId" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-user mr-2"></i>아이디
                                </label>
                                <input type="text" id="loginId" name="loginId" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
                                    placeholder="아이디를 입력하세요">
                            </div>

                            <!-- 비밀번호 입력 -->
                            <div>
                                <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-lock mr-2"></i>비밀번호
                                </label>
                                <input type="password" id="password" name="password" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
                                    placeholder="비밀번호를 입력하세요">
                            </div>

                            <!-- 비밀번호 확인 -->
                            <div>
                                <label for="passwordConfirm" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-lock mr-2"></i>비밀번호 확인
                                </label>
                                <input type="password" id="passwordConfirm" name="passwordConfirm" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
                                    placeholder="비밀번호를 다시 입력하세요">
                            </div>

                            <!-- 이름 입력 -->
                            <div>
                                <label for="name" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-id-card mr-2"></i>이름
                                </label>
                                <input type="text" id="name" name="name" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
                                    placeholder="이름을 입력하세요">
                            </div>

                            <!-- 이메일 입력 -->
                            <div>
                                <label for="email" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-envelope mr-2"></i>이메일
                                </label>
                                <input type="email" id="email" name="email" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent transition-colors"
                                    placeholder="email@example.com">
                            </div>

                            <!-- 에러 메시지 -->
                            <div id="errorMessage"
                                class="hidden bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
                                <i class="fas fa-exclamation-triangle mr-2"></i>
                                <span id="errorText"></span>
                            </div>

                            <!-- 성공 메시지 -->
                            <div id="successMessage"
                                class="hidden bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded-lg">
                                <i class="fas fa-check-circle mr-2"></i>
                                <span id="successText"></span>
                            </div>

                            <!-- 회원가입 버튼 -->
                            <button type="submit" id="registerButton"
                                class="w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors flex items-center justify-center">
                                <i class="fas fa-user-plus mr-2"></i>
                                회원가입
                            </button>
                        </div>
                    </form>

                    <!-- 추가 링크 -->
                    <div class="mt-6 text-center space-y-3">
                        <a href="/login" class="block text-blue-600 hover:text-blue-800 text-sm font-medium">
                            <i class="fas fa-sign-in-alt mr-1"></i>이미 계정이 있으신가요? 로그인
                        </a>
                        <a href="/" class="block text-gray-600 hover:text-gray-800 text-sm">
                            <i class="fas fa-arrow-left mr-1"></i>메인 페이지로 돌아가기
                        </a>
                    </div>
                </div>
            </div>

            <script>
                document.addEventListener( 'DOMContentLoaded', function () {
                    const registerForm = document.getElementById( 'registerForm' );
                    const registerButton = document.getElementById( 'registerButton' );
                    const errorMessage = document.getElementById( 'errorMessage' );
                    const errorText = document.getElementById( 'errorText' );
                    const successMessage = document.getElementById( 'successMessage' );
                    const successText = document.getElementById( 'successText' );

                    registerForm.addEventListener( 'submit', function ( e ) {
                        e.preventDefault();

                        const formData = new FormData( registerForm );
                        const password = formData.get( 'password' );
                        const passwordConfirm = formData.get( 'passwordConfirm' );

                        // 비밀번호 확인
                        if ( password !== passwordConfirm ) {
                            showError( '비밀번호가 일치하지 않습니다.' );
                            return;
                        }

                        const registerData = {
                            loginId: formData.get( 'loginId' ),
                            password: password,
                            name: formData.get( 'name' ),
                            email: formData.get( 'email' )
                        };

                        // 로딩 상태
                        registerButton.disabled = true;
                        registerButton.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>가입 중...';
                        errorMessage.classList.add( 'hidden' );
                        successMessage.classList.add( 'hidden' );

                        // 회원가입 API 호출
                        fetch( '/register', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify( registerData )
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    showSuccess( '회원가입이 완료되었습니다. 로그인 페이지로 이동합니다...' );
                                    setTimeout( () => {
                                        window.location.href = '/login';
                                    }, 2000 );
                                } else {
                                    showError( result.message || '회원가입에 실패했습니다.' );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Register error:', error );
                                showError( '회원가입 중 오류가 발생했습니다.' );
                            } )
                            .finally( () => {
                                // 로딩 상태 해제
                                registerButton.disabled = false;
                                registerButton.innerHTML = '<i class="fas fa-user-plus mr-2"></i>회원가입';
                            } );
                    } );

                    function showError( message ) {
                        errorText.textContent = message;
                        errorMessage.classList.remove( 'hidden' );
                        successMessage.classList.add( 'hidden' );
                    }

                    function showSuccess( message ) {
                        successText.textContent = message;
                        successMessage.classList.remove( 'hidden' );
                        errorMessage.classList.add( 'hidden' );
                    }
                } );
            </script>
        </body>

        </html>