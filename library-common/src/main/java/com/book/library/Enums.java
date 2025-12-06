package com.book.library;

public class Enums {
    public enum BookStatus { AVAILABLE, BORROWED } // AVAILABLE = 0, BORROWED = 1
    public enum MemberRole { USER, ADMIN } // user = 0, admin = 1
    public enum MemberStatus { ACTIVE, INACTIVE } // active = 0, inactive = 1
    // rented : 대여, returned : 반납, overdue : 연체
    public enum RentalStatus { RENTED, RETURNED, OVERDUE } // rented = 0, returned = 1, overdue = 2

}
