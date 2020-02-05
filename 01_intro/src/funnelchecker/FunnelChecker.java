package funnelchecker;

public class FunnelChecker {


    public static void main(String[] args) {
        System.out.println(isFunnel("leave", "eave"));
        System.out.println(isFunnel("reset", "rest"));
        System.out.println(isFunnel("dragoon", "dragon"));
        System.out.println(isFunnel("eave", "leave"));
        System.out.println(isFunnel("sleet", "lets"));
        System.out.println(isFunnel("skiff", "ski"));
    }
    public static boolean isFunnel(String str1, String str2) {
        if (str1.length() != str2.length() + 1) {
            return false;
        }
        boolean hasMismatchOccurred = false;
        for (int i = 0, j = 0;
             i < str1.length() && j < str2.length();
             i++, j++) {
            if (str1.charAt(i) != str2.charAt(j)) {
                if (hasMismatchOccurred) {
                    return false;
                }
                j--;
                hasMismatchOccurred = true;
            }
        }
        return true;
    }
}
