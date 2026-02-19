<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>회원 관리 - 도서관리 시스템</title>
            <script src="https://cdn.tailwindcss.com"></script>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        </head>

        <body class="bg-gradient-to-br from-blue-50 to-indigo-100 min-h-screen">
            <!-- Navigation -->
            <nav class="bg-white shadow-lg">
                <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div class="flex justify-between h-16">
                        <div class="flex items-center">
                            <a href="/" class="flex items-center">
                                <i class="fas fa-book text-blue-600 text-2xl mr-3"></i>
                                <span class="text-xl font-bold text-gray-800">도서관리 시스템</span>
                            </a>
                        </div>
                        <div class="flex items-center space-x-4">
                            <a href="/books"
                                class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
                                <i class="fas fa-book mr-1"></i>도서 관리
                            </a>
                            <a href="/members"
                                class="text-blue-600 px-3 py-2 rounded-md text-sm font-medium bg-blue-50">
                                <i class="fas fa-users mr-1"></i>회원 관리
                            </a>
                            <a href="/rentals"
                                class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
                                <i class="fas fa-exchange-alt mr-1"></i>대여 관리
                            </a>
                            <c:choose>
                                <c:when test="${not empty loginMember}">
                                    <span class="text-gray-700">${loginMember.name}님</span>
                                    <a href="/logout" class="text-red-600 hover:text-red-800">
                                        <i class="fas fa-sign-out-alt mr-1"></i>로그아웃
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/login" class="text-blue-600 hover:text-blue-800">
                                        <i class="fas fa-sign-in-alt mr-1"></i>로그인
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </nav>

            <!-- Main Content -->
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <!-- Page Header -->
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold text-gray-800">
                        <i class="fas fa-users mr-3 text-green-600"></i>회원 관리
                    </h1>
                    <button onclick="openMemberModal()"
                        class="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-lg font-medium transition-colors flex items-center">
                        <i class="fas fa-user-plus mr-2"></i>회원 등록
                    </button>
                </div>

                <!-- 회원 목록 테이블 -->
                <div class="bg-white rounded-xl shadow-lg overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        회원 정보</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        연락처</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        상태</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        권한</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        관리</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="member" items="${members}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="flex items-center">
                                                <div class="flex-shrink-0 h-10 w-10">
                                                    <div
                                                        class="h-10 w-10 rounded-full bg-green-100 flex items-center justify-center">
                                                        <i class="fas fa-user text-green-600"></i>
                                                    </div>
                                                </div>
                                                <div class="ml-4">
                                                    <div class="text-sm font-medium text-gray-900">${member.name}</div>
                                                    <div class="text-sm text-gray-500">${member.loginId}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${member.email}</div>
                                            <div class="text-sm text-gray-500">${member.phone}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <c:choose>
                                                <c:when test="${member.status == 'ACTIVE'}">
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                                                        <i class="fas fa-check-circle mr-1"></i>활성
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                                                        <i class="fas fa-times-circle mr-1"></i>비활성
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <c:choose>
                                                <c:when test="${member.role == 'ADMIN'}">
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-purple-100 text-purple-800">
                                                        <i class="fas fa-crown mr-1"></i>관리자
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                                                        <i class="fas fa-user mr-1"></i>일반
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                            <button data-member-id="${member.id}"
                                                onclick="editMember(this.getAttribute('data-member-id'))"
                                                class="text-blue-600 hover:text-blue-900 transition-colors">
                                                <i class="fas fa-edit mr-1"></i>수정
                                            </button>
                                            <c:choose>
                                                <c:when test="${member.status == 'ACTIVE'}">
                                                    <button data-member-id="${member.id}"
                                                        onclick="deactivateMember(this.getAttribute('data-member-id'))"
                                                        class="text-orange-600 hover:text-orange-900 transition-colors">
                                                        <i class="fas fa-ban mr-1"></i>비활성화
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button data-member-id="${member.id}"
                                                        onclick="activateMember(this.getAttribute('data-member-id'))"
                                                        class="text-green-600 hover:text-green-900 transition-colors">
                                                        <i class="fas fa-check mr-1"></i>활성화
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                            <button data-member-id="${member.id}"
                                                onclick="deleteMember(this.getAttribute('data-member-id'))"
                                                class="text-red-600 hover:text-red-900 transition-colors">
                                                <i class="fas fa-trash mr-1"></i>삭제
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 회원 등록/수정 모달 -->
            <div id="memberModal"
                class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
                <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                    <div class="mt-3">
                        <h2 class="text-lg font-bold text-gray-900 mb-4">
                            <i class="fas fa-user-plus mr-2 text-green-600"></i>회원 등록
                        </h2>
                        <form id="memberForm" class="space-y-4">
                            <div>
                                <label for="loginId" class="block text-sm font-medium text-gray-700">아이디</label>
                                <input type="text" id="loginId" name="loginId" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                            </div>
                            <div>
                                <label for="password" class="block text-sm font-medium text-gray-700">비밀번호</label>
                                <input type="password" id="password" name="password" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                            </div>
                            <div>
                                <label for="name" class="block text-sm font-medium text-gray-700">이름</label>
                                <input type="text" id="name" name="name" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                            </div>
                            <div>
                                <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
                                <input type="email" id="email" name="email" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                            </div>
                            <div>
                                <label for="phone" class="block text-sm font-medium text-gray-700">전화번호</label>
                                <input type="tel" id="phone" name="phone" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                            </div>
                            <div>
                                <label for="role" class="block text-sm font-medium text-gray-700">권한</label>
                                <select id="role" name="role"
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500">
                                    <option value="0">일반 사용자</option>
                                    <option value="1">관리자</option>
                                </select>
                            </div>
                        </form>
                        <div class="flex justify-end space-x-3 mt-6">
                            <button onclick="closeMemberModal()"
                                class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition-colors">
                                <i class="fas fa-times mr-1"></i>취소
                            </button>
                            <button onclick="submitMemberForm()"
                                class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                                <i class="fas fa-check mr-1"></i>등록
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                window.openMemberModal = function () {
                    document.getElementById( 'memberModal' ).classList.remove( 'hidden' );
                };

                window.closeMemberModal = function () {
                    document.getElementById( 'memberModal' ).classList.add( 'hidden' );
                    document.getElementById( 'memberForm' ).reset();
                    document.getElementById( 'loginId' ).readOnly = false;
                    document.getElementById( 'password' ).required = true;
                    document.getElementById( 'password' ).placeholder = '';
                    document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-plus mr-2 text-green-600"></i>회원 등록';
                };

                window.submitMemberForm = function () {
                    const formData = new FormData( document.getElementById( 'memberForm' ) );
                    const data = Object.fromEntries( formData );

                    fetch( '/members', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify( data )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '회원이 성공적으로 등록되었습니다.' );
                                location.reload();
                            } else {
                                alert( '회원 등록에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '회원 등록 중 오류가 발생했습니다.' );
                        } );
                };

                window.editMember = function ( memberId ) {
                    fetch( '/members/api/' + memberId )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                const member = result.data;
                                document.getElementById( 'loginId' ).value = member.loginId || '';
                                document.getElementById( 'loginId' ).readOnly = true;
                                document.getElementById( 'password' ).value = '';
                                document.getElementById( 'password' ).required = false;
                                document.getElementById( 'password' ).placeholder = '변경하지 않으려면 비워두세요';
                                document.getElementById( 'name' ).value = member.name || '';
                                document.getElementById( 'email' ).value = member.email || '';
                                document.getElementById( 'phone' ).value = member.phone || '';

                                // role 값 설정 (ADMIN=1, USER=0 또는 문자열 처리)
                                let roleValue = 0;
                                if ( member.role === 1 || member.role === 'ADMIN' ) {
                                    roleValue = 1;
                                }
                                document.getElementById( 'role' ).value = roleValue;

                                document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-edit mr-2 text-green-600"></i>회원 수정';

                                // 버튼 onclick 변경
                                const submitBtn = document.querySelector( '#memberModal button[onclick*="submitMemberForm"]' );
                                if ( submitBtn ) {
                                    submitBtn.setAttribute( 'onclick', 'updateMemberForm(' + memberId + ')' );
                                }

                                openMemberModal();
                            } else {
                                alert( '회원 정보를 불러오는데 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '회원 정보를 불러오는 중 오류가 발생했습니다: ' + error.message );
                        } );
                };

                window.updateMemberForm = function ( memberId ) {
                    const formData = new FormData( document.getElementById( 'memberForm' ) );
                    const memberData = Object.fromEntries( formData );
                    if ( !memberData.password || memberData.password.trim() === '' ) {
                        delete memberData.password;
                    }

                    fetch( '/members/' + memberId, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify( memberData )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '회원 정보가 성공적으로 수정되었습니다.' );
                                location.reload();
                            } else {
                                alert( '회원 수정에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '회원 수정 중 오류가 발생했습니다: ' + error.message );
                        } );
                };

                window.deactivateMember = function ( memberId ) {
                    if ( confirm( '이 회원을 비활성화하시겠습니까?' ) ) {
                        fetch( '/members/' + memberId + '/deactivate', {
                            method: 'PATCH',
                            headers: { 'Content-Type': 'application/json' }
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    alert( '회원이 성공적으로 비활성화되었습니다.' );
                                    location.reload();
                                } else {
                                    alert( '회원 비활성화에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Error:', error );
                                alert( '회원 비활성화 중 오류가 발생했습니다: ' + error.message );
                            } );
                    }
                };

                window.activateMember = function ( memberId ) {
                    if ( confirm( '이 회원을 활성화하시겠습니까?' ) ) {
                        fetch( '/members/' + memberId + '/activate', {
                            method: 'PATCH',
                            headers: { 'Content-Type': 'application/json' }
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    alert( '회원이 성공적으로 활성화되었습니다.' );
                                    location.reload();
                                } else {
                                    alert( '회원 활성화에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Error:', error );
                                alert( '회원 활성화 중 오류가 발생했습니다: ' + error.message );
                            } );
                    }
                };

                window.deleteMember = function ( memberId ) {
                    if ( confirm( '정말로 이 회원을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.' ) ) {
                        fetch( '/members/' + memberId, {
                            method: 'DELETE',
                            headers: { 'Content-Type': 'application/json' }
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    alert( '회원이 성공적으로 삭제되었습니다.' );
                                    location.reload();
                                } else {
                                    alert( '회원 삭제에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Error:', error );
                                alert( '회원 삭제 중 오류가 발생했습니다: ' + error.message );
                            } );
                    }
                };

                document.getElementById( 'memberModal' ).addEventListener( 'click', function ( e ) {
                    if ( e.target === this ) closeMemberModal();
                } );
            </script>
        </body>

        </html>