<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>도서 관리 - 도서관리 시스템</title>
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
                            <a href="/books" class="text-blue-600 px-3 py-2 rounded-md text-sm font-medium bg-blue-50">
                                <i class="fas fa-book mr-1"></i>도서 관리
                            </a>
                            <a href="/members"
                                class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
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
                <div class="flex justify-between items-center mb-6">
                    <h1 class="text-3xl font-bold text-gray-800">
                        <i class="fas fa-book mr-3 text-blue-600"></i>도서 관리
                    </h1>
                    <button onclick="openBookModal()"
                        class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg font-medium transition-colors flex items-center">
                        <i class="fas fa-plus mr-2"></i>도서 등록
                    </button>
                </div>

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
                                        저자</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        출판사</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        상태</th>
                                    <th
                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        관리</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <c:forEach var="book" items="${books}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="flex items-center">
                                                <div class="flex-shrink-0 h-10 w-10">
                                                    <div
                                                        class="h-10 w-10 rounded-full bg-blue-100 flex items-center justify-center">
                                                        <i class="fas fa-book text-blue-600"></i>
                                                    </div>
                                                </div>
                                                <div class="ml-4">
                                                    <div class="text-sm font-medium text-gray-900">${book.title}</div>
                                                    <div class="text-sm text-gray-500">ISBN: ${book.isbn}</div>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${book.author}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm text-gray-900">${book.publisher}</div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <c:choose>
                                                <c:when test="${book.status == 'AVAILABLE'}">
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">
                                                        <i class="fas fa-check-circle mr-1"></i>대여가능
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">
                                                        <i class="fas fa-times-circle mr-1"></i>대여중
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-2">
                                            <button data-book-id="${book.id}"
                                                onclick="editBook(this.getAttribute('data-book-id'))"
                                                class="text-blue-600 hover:text-blue-900 transition-colors">
                                                <i class="fas fa-edit mr-1"></i>수정
                                            </button>
                                            <button data-book-id="${book.id}"
                                                onclick="deleteBook(this.getAttribute('data-book-id'))"
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

            <!-- Modal -->
            <div id="bookModal"
                class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
                <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
                    <div class="mt-3">
                        <h2 class="text-lg font-bold text-gray-900 mb-4">
                            <i class="fas fa-plus mr-2 text-blue-600"></i>도서 등록
                        </h2>
                        <form id="bookForm" class="space-y-4">
                            <div>
                                <label for="title" class="block text-sm font-medium text-gray-700">제목</label>
                                <input type="text" id="title" name="title" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                            </div>
                            <div>
                                <label for="author" class="block text-sm font-medium text-gray-700">저자</label>
                                <input type="text" id="author" name="author" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                            </div>
                            <div>
                                <label for="publisher" class="block text-sm font-medium text-gray-700">출판사</label>
                                <input type="text" id="publisher" name="publisher" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                            </div>
                            <div>
                                <label for="isbn" class="block text-sm font-medium text-gray-700">ISBN</label>
                                <input type="text" id="isbn" name="isbn" required
                                    class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500">
                            </div>
                        </form>
                        <div class="flex justify-end space-x-3 mt-6">
                            <button onclick="closeBookModal()"
                                class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition-colors">
                                <i class="fas fa-times mr-1"></i>취소
                            </button>
                            <button onclick="submitBookForm()"
                                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
                                <i class="fas fa-check mr-1"></i>등록
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                window.openBookModal = function () {
                    document.getElementById( 'bookModal' ).classList.remove( 'hidden' );
                };

                window.closeBookModal = function () {
                    document.getElementById( 'bookModal' ).classList.add( 'hidden' );
                    document.getElementById( 'bookForm' ).reset();
                    document.querySelector( '#bookModal h2' ).innerHTML = '<i class="fas fa-plus mr-2 text-blue-600"></i>도서 등록';
                };

                window.submitBookForm = function () {
                    const formData = new FormData( document.getElementById( 'bookForm' ) );
                    const data = Object.fromEntries( formData );

                    fetch( '/books', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify( data )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '도서가 성공적으로 등록되었습니다.' );
                                location.reload();
                            } else {
                                alert( '도서 등록에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '도서 등록 중 오류가 발생했습니다: ' + error.message );
                        } );
                };

                window.editBook = function ( bookId ) {
                    fetch( '/books/api/' + bookId )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                const book = result.data;
                                document.getElementById( 'title' ).value = book.title || '';
                                document.getElementById( 'author' ).value = book.author || '';
                                document.getElementById( 'publisher' ).value = book.publisher || '';
                                document.getElementById( 'isbn' ).value = book.isbn || '';
                                document.querySelector( '#bookModal h2' ).innerHTML = '<i class="fas fa-edit mr-2 text-blue-600"></i>도서 수정';
                                document.querySelector( '#bookModal button[onclick="submitBookForm()"]' ).setAttribute( 'onclick', 'updateBookForm(' + bookId + ')' );
                                openBookModal();
                            } else {
                                alert( '도서 정보를 불러오는데 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '도서 정보를 불러오는 중 오류가 발생했습니다: ' + error.message );
                        } );
                };

                window.updateBookForm = function ( bookId ) {
                    const formData = new FormData( document.getElementById( 'bookForm' ) );
                    const bookData = Object.fromEntries( formData );

                    fetch( '/books/' + bookId, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify( bookData )
                    } )
                        .then( response => response.json() )
                        .then( result => {
                            if ( result.success ) {
                                alert( '도서 정보가 성공적으로 수정되었습니다.' );
                                location.reload();
                            } else {
                                alert( '도서 수정에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                            }
                        } )
                        .catch( error => {
                            console.error( 'Error:', error );
                            alert( '도서 수정 중 오류가 발생했습니다: ' + error.message );
                        } );
                };

                window.deleteBook = function ( bookId ) {
                    if ( confirm( '정말로 이 도서를 삭제하시겠습니까?' ) ) {
                        fetch( '/books/' + bookId, {
                            method: 'DELETE',
                            headers: { 'Content-Type': 'application/json' }
                        } )
                            .then( response => response.json() )
                            .then( result => {
                                if ( result.success ) {
                                    alert( '도서가 성공적으로 삭제되었습니다.' );
                                    location.reload();
                                } else {
                                    alert( '도서 삭제에 실패했습니다: ' + ( result.message || '알 수 없는 오류' ) );
                                }
                            } )
                            .catch( error => {
                                console.error( 'Error:', error );
                                alert( '도서 삭제 중 오류가 발생했습니다: ' + error.message );
                            } );
                    }
                };

                document.getElementById( 'bookModal' ).addEventListener( 'click', function ( e ) {
                    if ( e.target === this ) closeBookModal();
                } );
            </script>
        </body>

        </html>