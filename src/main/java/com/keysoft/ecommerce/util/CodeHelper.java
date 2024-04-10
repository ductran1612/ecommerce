package com.keysoft.ecommerce.util;

import java.time.LocalDateTime;
import java.util.Random;

import static java.time.temporal.ChronoField.YEAR_OF_ERA;

public class CodeHelper {
    public static String spawnCodeFromName(String name) {
        name = replaceVietnameseChars(name.trim());
        return name.toLowerCase().replaceAll("[^a-zA-Z0-9]", "-");
    }

    public static String replaceVietnameseChars(String input) {
        String[] fromChars = {"à", "á", "ả", "ã", "ạ", "ă", "ằ", "ắ", "ẳ", "ẵ", "ặ", "â", "ầ", "ấ", "ẩ", "ẫ", "ậ", "đ", "è", "é", "ẻ", "ẽ", "ẹ", "ê", "ề", "ế", "ể", "ễ", "ệ", "ì", "í", "ỉ", "ĩ", "ị", "ò", "ó", "ỏ", "õ", "ọ", "ô", "ồ", "ố", "ổ", "ỗ", "ộ", "ơ", "ờ", "ớ", "ở", "ỡ", "ợ", "ù", "ú", "ủ", "ũ", "ụ", "ư", "ừ", "ứ", "ử", "ữ", "ự", "ỳ", "ý", "ỷ", "ỹ", "ỵ", "À", "Á", "Ả", "Ã", "Ạ", "Ă", "Ằ", "Ắ", "Ẳ", "Ẵ", "Ặ", "Â", "Ầ", "Ấ", "Ẩ", "Ẫ", "Ậ", "Đ", "È", "É", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ề", "Ế", "Ể", "Ễ", "Ệ", "Ì", "Í", "Ỉ", "Ĩ", "Ị", "Ò", "Ó", "Ỏ", "Õ", "Ọ", "Ô", "Ồ", "Ố", "Ổ", "Ỗ", "Ộ", "Ơ", "Ờ", "Ớ", "Ở", "Ỡ", "Ợ", "Ù", "Ú", "Ủ", "Ũ", "Ụ", "Ư", "Ừ", "Ứ", "Ử", "Ữ", "Ự", "Ỳ", "Ý", "Ỷ", "Ỹ", "Ỵ"};
        String[] toChars =   {"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "d", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "y", "y", "y", "y", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "D", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "Y", "Y", "Y", "Y"};

        for (int i = 0; i < fromChars.length; i++) {
            input = input.replace(fromChars[i], toChars[i]);
        }

        return input;
    }

    public static String spawnCode(String entity, LocalDateTime createdDate) {
        String code = String.valueOf(entity.charAt(0)).toUpperCase() + convertDate(createdDate) + convertNumber(0, true);
        if(code.charAt(code.length() -1) == '-')
            code.substring(0, code.length() - 1);
        return code;
    }

    public static String convertDate(LocalDateTime createdDate) {
        String day = createdDate.getDayOfMonth() < 10 ? "0" + createdDate.getDayOfMonth() : String.valueOf(createdDate.getDayOfMonth());

        return day + convertMonth(createdDate) + convertYear(createdDate);
    }

    public static String convertNumber(long number, boolean isRandom) {
        if (isRandom) {
            // initialize a Random object somewhere; you should only need one
            Random random = new Random();

            // generate a random integer from 0 to 9999
            number = random.nextInt(10000);
        }

        if (number < 10) {
            return "000" + number;
        } else if (number < 100) {
            return "00" + number;
        } else if (number < 1000) {
            return "0" + number;
        } else {
            if (number > 9999) {
                number = number % 10;
            }
            return String.valueOf(number);
        }
    }

    public static String convertMonth(LocalDateTime date) {
        return date.getMonthValue() < 10 ? "0" + date.getMonthValue() : String.valueOf(date.getMonthValue());
    }

    public static String convertYear(LocalDateTime date) {
        return date.get(YEAR_OF_ERA) < 10 ? "0" + (date.get(YEAR_OF_ERA) % 2000) : String.valueOf(date.get(YEAR_OF_ERA) % 2000);
    }
}
