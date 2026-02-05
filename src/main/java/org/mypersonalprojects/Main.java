package org.mypersonalprojects;

public class Main {
    public static Integer sum(Integer num1, Integer num2) {
        return num1 + num2;
    }

    public static void main(String[] args) {
        var soma = sum(1,2);
        var resultado = String.format("O resultado da soma é %d", soma);
        System.out.println(resultado);
    }
}