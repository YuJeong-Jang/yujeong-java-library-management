<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>대여 관리 - 도서관리 시스템</title>
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
                                class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
                                <i class="fas fa-users mr-1"></i>회원 관리
                            </a>
                            <a href="/rentals"
                                class="text-blue-600 px-3 py-2 rounded-md text-sm font-medium bg-blue-50">
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
                        <i class="fas fa-exchange-alt mr-3 text-yellow-600"></i>대여 관리
                    </h1>
                    <button onclick="openRentalModal()"
                        class="bg-yellow-600 hover:bg-yellow-700 text-white px-6 py-3 rounded-lg font-medium transition-colors flex items-center">
                        <i class="fas fa-plus mr-2"></i>대여 등록
                    </button>
                </div>

                <!-- 대여 목록 테이블 -->
                <div class="bg-white rounded-xl shadow-lg overflow-hidden">
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        도서 정보</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        회원 정보</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        대여일</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        반납예정일</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        상태</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        관리</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="rental" items="${rentals}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="flex items-center">
                                                <div class="flex-shrink-0 h-10 w-10">
                                                    <div
                                                        class="h-10 w-10 rounded-full bg-yellow-100 flex items-center justify-center">
                                                        <i class="fas fa-book text-yellow-600"></i>
                                                    </div>
                                                </div>
                                                <div class="ml-4">
                                                    <div class="text-sm font-medium text-gray-900">${rental.bookTitle}
                                                    </div>
                                                    <div class="text-sm text-gray-500">${rental.bookAuthor}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${rental.memberName}</div>
                                            <div class="text-sm text-gray-500">${rental.memberEmail}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${rental.rentalDate}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${rental.dueDate}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <c:choose>
                                                <c:when test="${rental.rentalStatus == 'RENTED'}">
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                                                        <i class="fas fa-clock mr-1"></i>대여중
                                                    </span>
                                                </c:when>
                                                <c:when test="${rental.rentalStatus == 'RETURNED'}">
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                                                        <i class="fas fa-check-circle mr-1"></i>반납완료
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                                                        <i class="fas fa-exclamation-triangle mr-1"></i>연체
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                            <c:if
                                                test="${rental.rentalStatus == 'RENTED' || rental.rentalStatus == 'OVERDUE'}">
                                                <button data-rental-id="${rental.id}"
                                                    onclick="returnBook(this.getAttribute('data-rental-id'))"
                                                    class="text-green-600 hover:text-green-900 transition-colors">
                                                    <i class="fas fa-undo mr-1"></i>반납
                                                </button>
                                            </c:if>
                                            <button data-rental-id="${rental.id}"
                                                onclick="deleteRental(this.getAttribute('data-rental-id'))"
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

                <!-- 대여 등록 모달 -->
                <div id="rentalModal"
                    class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
                    <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                        <div class="mt-3">
                            <h2 class="text-lg font-bold text-gray-900 mb-4">
                                <i class="fas fa-plus mr-2 text-yellow-600"></i>대여 등록
                            </h2>
                            <form id="rentalForm" class="space-y-4">
                                <div>
                                    <label for="bookId" class="block text-sm font-medium text-gray-700">도서 선택</label>
                                    <select id="bookId" name="bookId" required
                                        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-500 focus:border-yellow-500">
                                        <option value="">도서를 선택하세요</option>
                                        <c:forEach var="book" items="${availableBooks}">
                                            <option value="${book.id}">${book.title} - ${book.author}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <label for="memberId" class="block text-sm font-medium text-gray-700">회원 선택</label>
                                    <select id="memberId" name="memberId" required
                                        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-500 focus:border-yellow-500">
                                        <option value="">회원을 선택하세요</option>
                                        <c:forEach var="member" items="${activeMembers}">
                                            <option value="${member.id}">${member.name} (${member.loginId})</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div>
                                    <label for="dueDate" class="block text-sm font-medium text-gray-700">반납예정일</label>
                                    <input type="date" id="dueDate" name="dueDate" required
                                        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-yellow-500 focus:border-yellow-500">
                                </div>
                            </form>
                            <div class="flex justify-end space-x-3 mt-6">
                                <button onclick="closeRentalModal()"
                                    class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition-colors">
                                    <i class="fas fa-times mr-1"></i>취소
                                </button>
                                <button onclick="submitRentalForm()"
                                    class="px-4 py-2 bg-yellow-600 text-white rounded-md hover:bg-yellow-700 transition-colors">
                                    <i class="fas fa-check mr-1"></i>등록
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <script>
                    // 대여 관리 JavaScript 함수들
                    window.openRentalModal = function () {
                        const modal = document.getElementById( 'rentalModal' );
                        if ( modal ) {
                            modal.classList.remove( 'hidden' );
                            modal.classList.add( 'flex' );

                            // 기본 반납예정일 설정 (2주 후)
                            const today = new Date();
                            const dueDate = new Date( today.getTime() + ( 14 * 24 * 60 * 60 * 1000 ) );
                            document.getElementById( 'dueDate' ).value = dueDate.toISOString().split( 'T' )[ 0 ];
                        }
                    };

                    window.closeRentalModal = function () {
                        const modal = document.getElementById( 'rentalModal' );
                        const form = document.getElementById( 'rentalForm' );
                        if ( modal ) {
                            modal.classList.add( 'hidden' );
                            modal.classList.remove( 'flex' );
                        }
                        if ( form ) {
                            form.reset();
                        }
                    };

                    window.submitRentalForm = function () {
                        const form = document.getElementById( 'rentalForm' );
                        if ( form ) {
                            const formData = new FormData( form );
                            const data = Object.fromEntries( formData );

                            fetch( '/rentals', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json',
                                },
                                body: JSON.stringify( data )
                            } )
                                .then( response => response.json() )
                                .then( result => {
                                    if ( result.success ) {
                                        alert( '대여가 성공적으로 등록되었습니다.' );
                                        closeRentalModal();
                                        location.reload();
                                    } else {
                                        alert( '대여 등록에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                    }
                                } )
                                .catch( error => {
                                    console.error( 'Error:', error );
                                    alert( '대여 등록 중 오류가 발생했습니다.' );
                                } );
                        }
                    };

                    window.returnBook = function ( rentalId ) {
                        if ( confirm( '이 도서를 반납 처리하시겠습니까?' ) ) {
                            fetch( '/rentals/' + rentalId + '/return', {
                                method: 'PATCH',
                                headers: {
                                    'Content-Type': 'application/json',
                                }
                            } )
                                .then( response => response.json() )
                                .then( result => {
                                    if ( result.success ) {
                                        alert( '도서가 성공적으로 반납되었습니다.' );
                                        location.reload();
                                    } else {
                                        alert( '반납 처리에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                    }
                                } )
                                .catch( error => {
                                    console.error( 'Error:', error );
                                    alert( '반납 처리 중 오류가 발생했습니다.' );
                                } );
                        }
                    };

                    window.deleteRental = function ( rentalId ) {
                        if ( confirm( '정말로 이 대여 기록을 삭제하시겠습니까?' ) ) {
                            fetch( '/rentals/' + rentalId, {
                                method: 'DELETE',
                                headers: {
                                    'Content-Type': 'application/json',
                                }
                            } )
                                .then( response => response.json() )
                                .then( result => {
                                    if ( result.success ) {
                                        alert( '대여 기록이 성공적으로 삭제되었습니다.' );
                                        location.reload();
                                    } else {
                                        alert( '대여 기록 삭제에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                    }
                                } )
                                .catch( error => {
                                    console.error( 'Error:', error );
                                    alert( '대여 기록 삭제 중 오류가 발생했습니다.' );
                                } );
                        }
                    };

                    // 모달 외부 클릭 시 닫기
                    document.addEventListener( 'DOMContentLoaded', function () {
                        const rentalModal = document.getElementById( 'rentalModal' );
                        if ( rentalModal ) {
                            rentalModal.addEventListener( 'click', function ( e ) {
                                if ( e.target === this ) {
                                    closeRentalModal();
                                }
                            } );
                        }
                    } );
                </script>
            </div>
        </body>

        </html>