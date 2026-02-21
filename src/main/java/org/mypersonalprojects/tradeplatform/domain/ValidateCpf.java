package org.mypersonalprojects.tradeplatform.domain;

public class ValidateCpf {
    public static boolean isValid(String cpf) {
        if (cpf.isBlank())
            return false;

        cpf = cpf.replaceAll("\\D", "");

        if ( cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            return false;

        var firstDigit = calculateDigit(cpf, 1);
        var secondDigit = calculateDigit(cpf, 2);

        if (firstDigit == Integer.parseInt(cpf.substring(9)) 
            || secondDigit == Integer.parseInt(cpf.substring(10)))
                return false;

        return true;
    }

    private static Integer calculateDigit(String cpf, Integer digit) {
        var total = 0;
        for (String cpfDigit : cpf.substring(0, cpf.length() - digit).split("")) {
            if (digit >= 2)
                total += Integer.parseInt(cpfDigit) * digit--;
        }

        var rest = total % 11;
        return rest < 2 ? 0 : 11 - rest;
    }
}
