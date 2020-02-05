package fractionsimplifier;

public class FractionSimplifier {
    public static void main(String[] args) {
        System.out.println(simplify("4/6"));
        System.out.println(simplify("10/11"));
        System.out.println(simplify("100/400"));
        System.out.println(simplify("8/4"));
    }

    public static String simplify(String fraction) {
        int numerator = Integer.parseInt(fraction.substring(0, fraction.indexOf("/")));
        int denominator = Integer.parseInt(fraction.substring(fraction.indexOf("/") + 1));
        int gcd = findGCD(numerator, denominator);
        int simplifiedNum = numerator / gcd;
        int simplifiedDenom = denominator / gcd;

        if (simplifiedDenom == 1) {
            return String.valueOf(simplifiedNum);
        } else {
            return String.format("%d/%d",
                    simplifiedNum,
                    simplifiedDenom);
        }
    }

    private static int findGCD(int number1, int number2) {
        if (number2 == 0) {
            return number1;
        }
        return findGCD(number2, number1 % number2);
    }
}
