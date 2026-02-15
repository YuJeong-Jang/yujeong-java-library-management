<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>로그인 - 도서관리 시스템</title>

            <!-- Tailwind CSS -->
            <script src="https://cdn.tailwindcss.com"></script>
            <!-- Font Awesome -->
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        </head>

        <body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen flex items-center justify-center">
            <div class="max-w-md w-full mx-4">
                <!-- 로고 섹션 -->
                <div class="text-center mb-8">
                    <div class="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-full mb-4">
                        <i class="fas fa-book text-white text-2xl"></i>
                    </div>
                    <h1 class="text-3xl font-bold text-gray-800">도서관리 시스템</h1>
                    <p class="text-gray-600 mt-2">관리자 로그인</p>
                </div>

                <!-- 로그인 폼 -->
                <div class="bg-white rounded-2xl shadow-xl p-8">
                    <form id="loginForm">
                        <div class="space-y-6">
                            <!-- 아이디 입력 -->
                            <div>
                                <label for="loginId" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-user mr-2"></i>아이디
                                </label>
                                <input type="text" id="loginId" name="loginId" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                                    placeholder="아이디를 입력하세요">
                            </div>

                            <!-- 비밀번호 입력 -->
                            <div>
                                <label for="password" class="block text-sm font-medium text-gray-700 mb-2">
                                    <i class="fas fa-lock mr-2"></i>비밀번호
                                </label>
                                <input type="password" id="password" name="password" required
                                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-colors"
                                    placeholder="비밀번호를 입력하세요">
                            </div>

                            <!-- 서버 에러 메시지 -->
                            <c:if test="${error != null}">
                                <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg mb-4">
                                    <i class="fas fa-exclamation-triangle mr-2"></i>
                                    ${error}
                                </div>
                            </c:if>

                            <!-- 클라이언트 에러 메시지 -->
                            <div id="errorMessage"
                                class="hidden bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg">
                                <i class="fas fa-exclamation-triangle mr-2"></i>
                                <span id="errorText"></span>
                            </div>

                            <!-- 로그인 버튼 -->
                            <button type="submit" id="loginButton"
                                class="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors flex items-center justify-center">
                                <i class="fas fa-sign-in-alt mr-2"></i>
                                로그인
                            </button>
                        </div>
                    </form>

                    <!-- 추가 링크 -->
                    <div class="mt-6 text-center space-y-3">
                        <a href="/register" class="block text-green-600 hover:text-green-800 text-sm font-medium">
                            <i class="fas fa-user-plus mr-1"></i>계정이 없으신가요? 회원가입
                        </a>
                        <a href="/" class="block text-blue-600 hover:text-blue-800 text-sm">
                            <i class="fas fa-arrow-left mr-1"></i>메인 페이지로 돌아가기
                        </a>
                    </div>
                </div>
            </div>

            <script>
                document.addEventListener( 'DOMContentLoaded', function () {
                    const loginForm = document.getElementById( 'loginForm' );
                    const loginButton = document.getElementById( 'loginButton' );
                    const errorMessage = document.getElementById( 'errorMessage' );
                    const errorText = document.getElementById( 'errorText' );

                    // URL 파라미터에서 에러 메시지 확인
                    const urlParams = new URLSearchParams( window.location.search );
                    const errorParam = urlParams.get( 'error' );
                    if ( errorParam === 'login_required' ) {
                        showError( '로그인이 필요한 페이지입니다. 먼저 로그인해주세요.' );
                    }

                    loginForm.addEventListener( 'submit', function ( e ) {
                        e.preventDefault();

                        const formData = new FormData( loginForm );
                        const loginData = {
                            loginId: formData.get( 'loginId' ),
                            password: formData.get( 'password' )
                        };

                        // 로딩 상태
                        loginButton.disabled = true;
                        loginButton.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i>로그인 중...';
                        errorMessage.classList.add( 'hidden' );

                        // 로그인 API 호출
                        fetch( '/login', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            body: JSON.stringify( loginData )
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    console.log( 'Login successful, redirecting...' );
                                    window.location.href = '/';
                                } else {
                                    console.log( 'Login failed:', result.message );
                                    showError( result.message || '로그인에 실패했습니다.' );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Login error:', error );
                                showError( '로그인 중 오류가 발생했습니다.' );
                            } )
                            .finally( () => {
                                // 로딩 상태 해제
                                loginButton.disabled = false;
                                loginButton.innerHTML = '<i class="fas fa-sign-in-alt mr-2"></i>로그인';
                            } );
                    } );

                    function showError( message ) {
                        errorText.textContent = message;
                        errorMessage.classList.remove( 'hidden' );
                    }
                } );
            </script>
        </body>

        </html>