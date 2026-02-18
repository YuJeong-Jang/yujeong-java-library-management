<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="ko">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>도서관리 시스템</title>
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
                            <c:if test="${not empty loginMember}">
                                <a href="/books"
                                    class="text-gray-600 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium">
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
                                <span class="text-gray-700">${loginMember.name}님</span>
                                <a href="/logout" class="text-red-600 hover:text-red-800">
                                    <i class="fas fa-sign-out-alt mr-1"></i>로그아웃
                                </a>
                            </c:if>
                            <c:if test="${empty loginMember}">
                                <a href="/login" class="text-blue-600 hover:text-blue-800">
                                    <i class="fas fa-sign-in-alt mr-1"></i>로그인
                                </a>
                                <a href="/register"
                                    class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg">
                                    <i class="fas fa-user-plus mr-1"></i>회원가입
                                </a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </nav>

            <!-- Main Content -->
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <!-- Hero Section -->
                <div class="bg-gradient-to-r from-blue-600 to-purple-600 rounded-2xl text-white p-8 mb-8">
                    <div class="text-center">
                        <h1 class="text-4xl md:text-5xl font-bold mb-4">
                            <i class="fas fa-book-open mr-4"></i>도서관리 시스템
                        </h1>
                        <p class="text-xl md:text-2xl mb-8 opacity-90">효율적인 도서 관리와 대여 서비스를 제공합니다</p>
                    </div>
                </div>

                <!-- Quick Actions -->
                <c:if test="${not empty loginMember}">
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                        <div
                            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div class="p-6 text-center">
                                <div
                                    class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                    <i class="fas fa-book text-2xl text-blue-600"></i>
                                </div>
                                <h3 class="text-xl font-semibold text-gray-800 mb-3">도서 관리</h3>
                                <p class="text-gray-600 mb-6">도서 등록, 수정, 삭제 및 검색 기능을 제공합니다.</p>
                                <a href="/books"
                                    class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                                    <i class="fas fa-arrow-right mr-2"></i>도서 관리하기
                                </a>
                            </div>
                        </div>

                        <div
                            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div class="p-6 text-center">
                                <div
                                    class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                    <i class="fas fa-users text-2xl text-green-600"></i>
                                </div>
                                <h3 class="text-xl font-semibold text-gray-800 mb-3">회원 관리</h3>
                                <p class="text-gray-600 mb-6">회원 등록, 정보 수정 및 상태 관리를 할 수 있습니다.</p>
                                <a href="/members"
                                    class="inline-flex items-center px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                                    <i class="fas fa-arrow-right mr-2"></i>회원 관리하기
                                </a>
                            </div>
                        </div>

                        <div
                            class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                            <div class="p-6 text-center">
                                <div
                                    class="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-4">
                                    <i class="fas fa-exchange-alt text-2xl text-yellow-600"></i>
                                </div>
                                <h3 class="text-xl font-semibold text-gray-800 mb-3">대여 관리</h3>
                                <p class="text-gray-600 mb-6">도서 대여, 반납 및 연체 관리를 할 수 있습니다.</p>
                                <a href="/rentals"
                                    class="inline-flex items-center px-6 py-3 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 transition-colors">
                                    <i class="fas fa-arrow-right mr-2"></i>대여 관리하기
                                </a>
                            </div>
                        </div>
                    </div>
                </c:if>

                <c:if test="${empty loginMember}">
                    <div class="bg-white rounded-xl shadow-lg p-8 mb-8 text-center">
                        <div class="w-20 h-20 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-6">
                            <i class="fas fa-sign-in-alt text-3xl text-blue-600"></i>
                        </div>
                        <h3 class="text-2xl font-semibold text-gray-800 mb-4">로그인이 필요합니다</h3>
                        <p class="text-gray-600 mb-6">도서관리, 회원관리, 대여관리 기능을 사용하려면 로그인해주세요.</p>
                        <div class="flex justify-center space-x-4">
                            <a href="/login"
                                class="inline-flex items-center px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
                                <i class="fas fa-sign-in-alt mr-2"></i>로그인
                            </a>
                            <a href="/register"
                                class="inline-flex items-center px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                                <i class="fas fa-user-plus mr-2"></i>회원가입
                            </a>
                        </div>
                    </div>
                </c:if>

                <!-- Statistics -->
                <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
                    <div class="bg-gradient-to-br from-blue-500 to-blue-600 text-white rounded-xl p-6 text-center">
                        <i class="fas fa-book text-3xl mb-3 opacity-80"></i>
                        <h3 class="text-2xl font-bold mb-1">${totalBooks != null ? totalBooks : 0}</h3>
                        <p class="text-blue-100">총 도서 수</p>
                    </div>
                    <div class="bg-gradient-to-br from-green-500 to-green-600 text-white rounded-xl p-6 text-center">
                        <i class="fas fa-users text-3xl mb-3 opacity-80"></i>
                        <h3 class="text-2xl font-bold mb-1">${totalMembers != null ? totalMembers : 0}</h3>
                        <p class="text-green-100">총 회원 수</p>
                    </div>
                    <div class="bg-gradient-to-br from-yellow-500 to-yellow-600 text-white rounded-xl p-6 text-center">
                        <i class="fas fa-exchange-alt text-3xl mb-3 opacity-80"></i>
                        <h3 class="text-2xl font-bold mb-1">${activeRentals != null ? activeRentals : 0}</h3>
                        <p class="text-yellow-100">대여 중인 도서</p>
                    </div>
                    <div class="bg-gradient-to-br from-red-500 to-red-600 text-white rounded-xl p-6 text-center">
                        <i class="fas fa-exclamation-triangle text-3xl mb-3 opacity-80"></i>
                        <h3 class="text-2xl font-bold mb-1">${overdueRentals != null ? overdueRentals : 0}</h3>
                        <p class="text-red-100">연체 도서</p>
                    </div>
                </div>
            </div>
        </body>

        </html>