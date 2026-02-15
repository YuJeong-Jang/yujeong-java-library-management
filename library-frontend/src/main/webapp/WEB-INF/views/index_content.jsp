<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>

        <!-- Hero Section -->
        <div class="bg-gradient-to-r from-blue-600 to-purple-600 rounded-2xl text-white p-8 mb-8">
            <div class="text-center">
                <h1 class="text-4xl md:text-5xl font-bold mb-4">
                    <i class="fas fa-book-open mr-4"></i>도서관리 시스템
                </h1>
                <p class="text-xl md:text-2xl mb-8 opacity-90">효율적인 도서 관리와 대여 서비스를 제공합니다</p>

                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
                    <div class="text-center">
                        <i class="fas fa-book text-4xl mb-3 opacity-80"></i>
                        <h3 class="text-lg font-semibold">도서 관리</h3>
                        <p class="text-sm opacity-75">체계적인 도서 등록 및 관리</p>
                    </div>
                    <div class="text-center">
                        <i class="fas fa-users text-4xl mb-3 opacity-80"></i>
                        <h3 class="text-lg font-semibold">회원 관리</h3>
                        <p class="text-sm opacity-75">회원 정보 및 상태 관리</p>
                    </div>
                    <div class="text-center">
                        <i class="fas fa-exchange-alt text-4xl mb-3 opacity-80"></i>
                        <h3 class="text-lg font-semibold">대여 관리</h3>
                        <p class="text-sm opacity-75">대여 및 반납 프로세스 관리</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="mb-8">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                <i class="fas fa-bolt mr-3 text-yellow-500"></i>빠른 메뉴
            </h2>
        </div>

        <!-- 로그인한 사용자용 메뉴 -->
        <c:if test="${sessionScope.loginMember != null}">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <!-- 도서 관리 카드 -->
                <div
                    class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                    <div class="p-6 text-center">
                        <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
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

                <!-- 회원 관리 카드 -->
                <div
                    class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                    <div class="p-6 text-center">
                        <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
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

                <!-- 대여 관리 카드 -->
                <div
                    class="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 transform hover:-translate-y-2">
                    <div class="p-6 text-center">
                        <div class="w-16 h-16 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-4">
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

        <!-- 로그인하지 않은 사용자용 안내 -->
        <c:if test="${sessionScope.loginMember == null}">
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

        <!-- Statistics Section -->
        <div class="mb-6">
            <h2 class="text-2xl font-bold text-gray-800 mb-6 flex items-center">
                <i class="fas fa-chart-bar mr-3 text-purple-500"></i>시스템 현황
            </h2>
        </div>

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