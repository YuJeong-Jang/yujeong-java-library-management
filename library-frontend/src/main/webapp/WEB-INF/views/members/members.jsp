<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                회원 정보</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                연락처</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                상태</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                권한</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
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
                                        <c:when test="${member.role == 'ADMIN' || member.role == 1}">
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
                                    <button onclick="editMember(${member.id})"
                                        class="text-blue-600 hover:text-blue-900 transition-colors">
                                        <i class="fas fa-edit mr-1"></i>수정
                                    </button>
                                    <c:choose>
                                        <c:when test="${member.status == 'ACTIVE'}">
                                            <button onclick="deactivateMember(${member.id})"
                                                class="text-red-600 hover:text-red-900 transition-colors">
                                                <i class="fas fa-ban mr-1"></i>비활성화
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button onclick="activateMember(${member.id})"
                                                class="text-green-600 hover:text-green-900 transition-colors">
                                                <i class="fas fa-check mr-1"></i>활성화
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 회원 등록/수정 모달 -->
        <div id="memberModal" class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
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
            // 회원 관리 JavaScript 함수들 (members.js 내용을 인라인으로 포함)
            window.openMemberModal = function () {
                const modal = document.getElementById( 'memberModal' );
                if ( modal ) {
                    modal.classList.remove( 'hidden' );
                    modal.classList.add( 'flex' );
                }
            };

            window.closeMemberModal = function () {
                const modal = document.getElementById( 'memberModal' );
                const form = document.getElementById( 'memberForm' );
                if ( modal ) {
                    modal.classList.add( 'hidden' );
                    modal.classList.remove( 'flex' );
                }
                if ( form ) {
                    form.reset();
                    document.getElementById( 'loginId' ).readOnly = false;
                    document.getElementById( 'password' ).placeholder = '';
                }

                // 모달 제목과 버튼 초기화
                document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-plus mr-2 text-green-600"></i>회원 등록';
                const submitBtn = document.querySelector( '#memberModal button[onclick*="Form"]' );
                if ( submitBtn ) {
                    submitBtn.innerHTML = '<i class="fas fa-check mr-1"></i>등록';
                    submitBtn.setAttribute( 'onclick', 'submitMemberForm()' );
                }
            };

            window.submitMemberForm = function () {
                const form = document.getElementById( 'memberForm' );
                if ( form ) {
                    const formData = new FormData( form );
                    const data = Object.fromEntries( formData );

                    fetch( '/members', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify( data )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '회원이 성공적으로 등록되었습니다.' );
                                closeMemberModal();
                                location.reload();
                            } else {
                                alert( '회원 등록에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '회원 등록 중 오류가 발생했습니다.' );
                        } );
                }
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
                            document.getElementById( 'password' ).placeholder = '변경하지 않으려면 비워두세요';
                            document.getElementById( 'name' ).value = member.name || '';
                            document.getElementById( 'email' ).value = member.email || '';
                            document.getElementById( 'phone' ).value = member.phone || '';

                            document.querySelector( '#memberModal h2' ).innerHTML = '<i class="fas fa-user-edit mr-2 text-green-600"></i>회원 수정';

                            const submitBtn = document.querySelector( '#memberModal button[onclick="submitMemberForm()"]' );
                            if ( submitBtn ) {
                                submitBtn.innerHTML = '<i class="fas fa-save mr-1"></i>수정';
                                submitBtn.setAttribute( 'onclick', 'updateMemberForm(' + memberId + ')' );
                            }

                            openMemberModal();
                        } else {
                            alert( '회원 정보를 불러오는데 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                        }
                    } )
                    .catch( error => {
                        console.error( 'Error:', error );
                        alert( '회원 정보를 불러오는 중 오류가 발생했습니다.' );
                    } );
            };

            window.updateMemberForm = function ( memberId ) {
                const form = document.getElementById( 'memberForm' );
                if ( form ) {
                    const formData = new FormData( form );
                    const memberData = Object.fromEntries( formData );

                    if ( !memberData.password || memberData.password.trim() === '' ) {
                        delete memberData.password;
                    }

                    fetch( '/members/' + memberId, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify( memberData )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '회원 정보가 성공적으로 수정되었습니다.' );
                                closeMemberModal();
                                location.reload();
                            } else {
                                alert( '회원 수정에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '회원 수정 중 오류가 발생했습니다.' );
                        } );
                }
            };

            window.deactivateMember = function ( memberId ) {
                if ( confirm( '이 회원을 비활성화하시겠습니까?' ) ) {
                    fetch( '/members/' + memberId + '/deactivate', {
                        method: 'PATCH',
                        headers: {
                            'Content-Type': 'application/json',
                        }
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
                            alert( '회원 비활성화 중 오류가 발생했습니다.' );
                        } );
                }
            };

            window.activateMember = function ( memberId ) {
                if ( confirm( '이 회원을 활성화하시겠습니까?' ) ) {
                    fetch( '/members/' + memberId + '/activate', {
                        method: 'PATCH',
                        headers: {
                            'Content-Type': 'application/json',
                        }
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
                            alert( '회원 활성화 중 오류가 발생했습니다.' );
                        } );
                }
            };

            // 모달 외부 클릭 시 닫기
            document.addEventListener( 'DOMContentLoaded', function () {
                const memberModal = document.getElementById( 'memberModal' );
                if ( memberModal ) {
                    memberModal.addEventListener( 'click', function ( e ) {
                        if ( e.target === this ) {
                            closeMemberModal();
                        }
                    } );
                }
            } );
        </script>